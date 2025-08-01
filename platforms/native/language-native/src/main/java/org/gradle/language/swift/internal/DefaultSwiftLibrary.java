/*
 * Copyright 2017 the original author or authors.
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

package org.gradle.language.swift.internal;

import org.gradle.api.Action;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.provider.Property;
import org.gradle.internal.Describables;
import org.gradle.internal.DisplayName;
import org.gradle.language.LibraryDependencies;
import org.gradle.language.cpp.internal.NativeVariantIdentity;
import org.gradle.language.internal.DefaultLibraryDependencies;
import org.gradle.language.swift.SwiftBinary;
import org.gradle.language.swift.SwiftLibrary;
import org.gradle.language.swift.SwiftPlatform;
import org.gradle.language.swift.SwiftSharedLibrary;
import org.gradle.language.swift.SwiftStaticLibrary;
import org.gradle.nativeplatform.Linkage;
import org.gradle.nativeplatform.toolchain.internal.NativeToolChainInternal;
import org.gradle.nativeplatform.toolchain.internal.PlatformToolProvider;

import javax.inject.Inject;
import java.util.Collections;

public abstract class DefaultSwiftLibrary extends DefaultSwiftComponent<SwiftBinary> implements SwiftLibrary {
    private final ConfigurationContainer configurations;
    private final DefaultLibraryDependencies dependencies;

    @Inject
    public DefaultSwiftLibrary(String name, ConfigurationContainer configurations) {
        super(name);
        this.configurations = configurations;
        getLinkage().convention(Collections.singleton(Linkage.SHARED));
        dependencies = getObjectFactory().newInstance(DefaultLibraryDependencies.class, getNames().withSuffix("implementation"), getNames().withSuffix("api"));
    }

    @Override
    public DisplayName getDisplayName() {
        return Describables.withTypeAndName("Swift library", getName());
    }

    @Override
    public Configuration getImplementationDependencies() {
        return dependencies.getImplementationDependencies();
    }

    @Override
    public LibraryDependencies getDependencies() {
        return dependencies;
    }

    public void dependencies(Action<? super LibraryDependencies> action) {
        action.execute(dependencies);
    }

    public SwiftStaticLibrary addStaticLibrary(NativeVariantIdentity identity, boolean testable, SwiftPlatform targetPlatform, NativeToolChainInternal toolChain, PlatformToolProvider platformToolProvider) {
        SwiftStaticLibrary result = getObjectFactory().newInstance(DefaultSwiftStaticLibrary.class, getNames().append(identity.getName()), getModule(), testable, getSwiftSource(), getImplementationDependencies(), targetPlatform, toolChain, platformToolProvider, identity);
        getBinaries().add(result);
        return result;
    }

    public SwiftSharedLibrary addSharedLibrary(NativeVariantIdentity identity, boolean testable, SwiftPlatform targetPlatform, NativeToolChainInternal toolChain, PlatformToolProvider platformToolProvider) {
        SwiftSharedLibrary result = getObjectFactory().newInstance(DefaultSwiftSharedLibrary.class, getNames().append(identity.getName()), getModule(), testable, getSwiftSource(), configurations, getImplementationDependencies(), targetPlatform, toolChain, platformToolProvider, identity);
        getBinaries().add(result);
        return result;
    }

    @Override
    public Configuration getApiDependencies() {
        return dependencies.getApiDependencies();
    }

    @Override
    public abstract Property<SwiftBinary> getDevelopmentBinary();
}
