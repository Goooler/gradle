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

plugins {
    id("gradlebuild.distribution.implementation-java")
    id("gradlebuild.publish-public-libraries")
}

description = "Produces traces from build operations"

dependencies {
    api(projects.buildOperations)
    api(projects.concurrent)
    api(projects.coreApi)
    api(projects.stdlibJavaExtensions)

    api(libs.guava)
    api(libs.jspecify)

    implementation(projects.baseServices)
    implementation(projects.buildOption)

    implementation(libs.jacksonCore)
    implementation(libs.jacksonDatabind)
    implementation(libs.jacksonDatatypeJdk8)
    implementation(libs.jacksonDatatypeJsr310)
}
