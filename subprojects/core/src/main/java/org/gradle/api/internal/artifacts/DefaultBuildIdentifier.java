/*
 * Copyright 2018 the original author or authors.
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

package org.gradle.api.internal.artifacts;

import org.gradle.api.artifacts.component.BuildIdentifier;
import org.gradle.util.Path;

public class DefaultBuildIdentifier implements BuildIdentifier {

    public static final BuildIdentifier ROOT = new DefaultBuildIdentifier(Path.ROOT);
    private final Path buildPath;

    public DefaultBuildIdentifier(Path buildPath) {
        if (!buildPath.isAbsolute()) {
            throw new IllegalArgumentException("Build path must be absolute: " + buildPath);
        }

        this.buildPath = buildPath;
    }

    @Override
    public String getBuildPath() {
        return buildPath.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DefaultBuildIdentifier)) {
            return false;
        }
        DefaultBuildIdentifier that = (DefaultBuildIdentifier) o;
        return buildPath.equals(that.buildPath);
    }

    @Override
    public int hashCode() {
        return buildPath.hashCode();
    }

    @Override
    public String toString() {
        return "build '" + buildPath + "'";
    }

}
