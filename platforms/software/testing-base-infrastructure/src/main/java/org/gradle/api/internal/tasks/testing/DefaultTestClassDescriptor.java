/*
 * Copyright 2010 the original author or authors.
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

package org.gradle.api.internal.tasks.testing;

import org.gradle.internal.scan.UsedByScanPlugin;
import org.jspecify.annotations.Nullable;

@UsedByScanPlugin("test-distribution")
public class DefaultTestClassDescriptor extends DefaultTestSuiteDescriptor {
    private final String classDisplayName;

    public DefaultTestClassDescriptor(Object id, String className) {
        this(id, className, null);
    }

    @UsedByScanPlugin("test-distribution")
    public DefaultTestClassDescriptor(Object id, String className, @Nullable String classDisplayName) {
        super(id, className);
        this.classDisplayName = classDisplayName == null ? className : classDisplayName;
    }

    @Override
    public String getClassName() {
        return getName();
    }

    @Override
    public String getDisplayName() {
        return getClassDisplayName();
    }

    @Override
    public String getClassDisplayName() {
        return classDisplayName;
    }

    @Override
    public String toString() {
        return "Test class " + getClassName();
    }
}
