/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.api.internal.tasks.compile.incremental;

import org.gradle.api.file.FileTree;
import org.gradle.api.internal.file.FileOperations;
import org.gradle.api.internal.file.FileTreeInternal;
import org.gradle.api.internal.tasks.compile.CleaningJavaCompiler;
import org.gradle.api.internal.tasks.compile.JavaCompileSpec;
import org.gradle.api.internal.tasks.compile.incremental.analyzer.CachingClassDependenciesAnalyzer;
import org.gradle.api.internal.tasks.compile.incremental.analyzer.ClassAnalysisCache;
import org.gradle.api.internal.tasks.compile.incremental.analyzer.ClassDependenciesAnalyzer;
import org.gradle.api.internal.tasks.compile.incremental.analyzer.DefaultClassDependenciesAnalyzer;
import org.gradle.api.internal.tasks.compile.incremental.cache.TaskScopedCompileCaches;
import org.gradle.api.internal.tasks.compile.incremental.cache.BuildScopedCompileCaches;
import org.gradle.api.internal.tasks.compile.incremental.deps.LocalClassSetAnalysisStore;
import org.gradle.api.internal.tasks.compile.incremental.jar.CachingJarSnapshotter;
import org.gradle.api.internal.tasks.compile.incremental.jar.ClasspathJarFinder;
import org.gradle.api.internal.tasks.compile.incremental.jar.JarClasspathSnapshotFactory;
import org.gradle.api.internal.tasks.compile.incremental.jar.JarClasspathSnapshotMaker;
import org.gradle.api.internal.tasks.compile.incremental.jar.JarSnapshotCache;
import org.gradle.api.internal.tasks.compile.incremental.jar.JarSnapshotter;
import org.gradle.api.internal.tasks.compile.incremental.jar.LocalJarClasspathSnapshotStore;
import org.gradle.api.internal.tasks.compile.incremental.processing.AnnotationProcessorPathStore;
import org.gradle.api.tasks.WorkResult;
import org.gradle.api.tasks.incremental.IncrementalTaskInputs;
import org.gradle.internal.hash.FileHasher;
import org.gradle.internal.hash.StreamHasher;
import org.gradle.internal.operations.BuildOperationExecutor;
import org.gradle.language.base.internal.compile.Compiler;

public class IncrementalCompilerFactory {

    private final FileOperations fileOperations;
    private final StreamHasher streamHasher;
    private final FileHasher fileHasher;
    private final BuildScopedCompileCaches buildScopedCompileCaches;
    private final BuildOperationExecutor buildOperationExecutor;

    public IncrementalCompilerFactory(FileOperations fileOperations, StreamHasher streamHasher, FileHasher fileHasher, BuildScopedCompileCaches buildScopedCompileCaches, BuildOperationExecutor buildOperationExecutor) {
        this.fileOperations = fileOperations;
        this.streamHasher = streamHasher;
        this.fileHasher = fileHasher;
        this.buildScopedCompileCaches = buildScopedCompileCaches;
        this.buildOperationExecutor = buildOperationExecutor;
    }

    public Compiler<JavaCompileSpec> makeIncremental(CleaningJavaCompiler cleaningJavaCompiler, String compileDisplayName, IncrementalTaskInputs inputs, FileTree sources) {
        TaskScopedCompileCaches compileCaches = createCompileCaches(compileDisplayName);
        Compiler<JavaCompileSpec> rebuildAllCompiler = createRebuildAllCompiler(cleaningJavaCompiler, sources);
        ClassDependenciesAnalyzer analyzer = new CachingClassDependenciesAnalyzer(new DefaultClassDependenciesAnalyzer(), compileCaches.getClassAnalysisCache());
        JarSnapshotter jarSnapshotter = new CachingJarSnapshotter(streamHasher, fileHasher, analyzer, compileCaches.getJarSnapshotCache());
        JarClasspathSnapshotMaker jarClasspathSnapshotMaker = new JarClasspathSnapshotMaker(compileCaches.getLocalJarClasspathSnapshotStore(), new JarClasspathSnapshotFactory(jarSnapshotter, buildOperationExecutor), new ClasspathJarFinder(fileOperations));
        CompilationSourceDirs sourceDirs = new CompilationSourceDirs((FileTreeInternal) sources);
        SourceToNameConverter sourceToNameConverter = new SourceToNameConverter(sourceDirs);
        RecompilationSpecProvider recompilationSpecProvider = new RecompilationSpecProvider(sourceToNameConverter, fileOperations);
        ClassSetAnalysisUpdater classSetAnalysisUpdater = new ClassSetAnalysisUpdater(compileCaches.getLocalClassSetAnalysisStore(), fileOperations, analyzer, fileHasher);
        IncrementalCompilationInitializer compilationInitializer = new IncrementalCompilationInitializer(fileOperations, sources);
        IncrementalCompilerDecorator incrementalSupport = new IncrementalCompilerDecorator(jarClasspathSnapshotMaker, compileCaches, compilationInitializer, cleaningJavaCompiler, compileDisplayName, recompilationSpecProvider, classSetAnalysisUpdater, sourceDirs, rebuildAllCompiler);
        return incrementalSupport.prepareCompiler(inputs);
    }

    private TaskScopedCompileCaches createCompileCaches(String path) {
        final LocalClassSetAnalysisStore localClassSetAnalysisStore = buildScopedCompileCaches.createLocalClassSetAnalysisStore(path);
        final LocalJarClasspathSnapshotStore localJarClasspathSnapshotStore = buildScopedCompileCaches.createLocalJarClasspathSnapshotStore(path);
        final AnnotationProcessorPathStore annotationProcessorPathStore = buildScopedCompileCaches.createAnnotationProcessorPathStore(path);
        return new TaskScopedCompileCaches() {
            @Override
            public ClassAnalysisCache getClassAnalysisCache() {
                return buildScopedCompileCaches.getClassAnalysisCache();
            }

            @Override
            public JarSnapshotCache getJarSnapshotCache() {
                return buildScopedCompileCaches.getJarSnapshotCache();
            }

            @Override
            public LocalJarClasspathSnapshotStore getLocalJarClasspathSnapshotStore() {
                return localJarClasspathSnapshotStore;
            }

            @Override
            public LocalClassSetAnalysisStore getLocalClassSetAnalysisStore() {
                return localClassSetAnalysisStore;
            }

            @Override
            public AnnotationProcessorPathStore getAnnotationProcessorPathStore() {
                return annotationProcessorPathStore;
            }
        };
    }

    private Compiler<JavaCompileSpec> createRebuildAllCompiler(final CleaningJavaCompiler cleaningJavaCompiler, final FileTree sourceFiles) {
        return new Compiler<JavaCompileSpec>() {
            @Override
            public WorkResult execute(JavaCompileSpec spec) {
                spec.setSourceFiles(sourceFiles);
                return cleaningJavaCompiler.execute(spec);
            }
        };
    }
}
