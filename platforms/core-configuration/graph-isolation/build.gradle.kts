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
    id("gradlebuild.distribution.implementation-kotlin")
    id("gradlebuild.kotlin-dsl-sam-with-receiver")
    id("gradlebuild.kotlin-experimental-contracts")
}

description = "Configuration Cache serialization based isolation for object graphs"

dependencies {
    api(projects.graphSerialization)

    api(libs.kotlinStdlib)

    implementation(projects.baseServices)
    implementation(projects.configurationCacheBase)
    implementation(projects.configurationProblemsBase)
    implementation(projects.loggingApi)
    implementation(projects.serialization)
    implementation(projects.stdlibKotlinExtensions)
}
