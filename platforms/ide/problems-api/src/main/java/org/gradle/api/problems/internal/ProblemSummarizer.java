/*
 * Copyright 2024 the original author or authors.
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

package org.gradle.api.problems.internal;

import org.gradle.internal.operations.OperationIdentifier;
import org.gradle.internal.service.scopes.Scope;
import org.gradle.internal.service.scopes.ServiceScope;
import org.gradle.problems.buildtree.ProblemReporter;
import org.jspecify.annotations.Nullable;

@ServiceScope(Scope.BuildTree.class)
public interface ProblemSummarizer extends ProblemReporter {
    /**
     * Emits the given problem in an implementation specific way.
     * <p>
     * The problem will be associated with the given operation identifier.
     *
     * @param problem The problem to emit.
     */
    void emit(InternalProblem problem, @Nullable OperationIdentifier id);
}
