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
package org.gradle.kotlin.dsl

import org.gradle.api.Incubating
import org.gradle.plugin.management.internal.autoapply.AutoAppliedGradleEnterprisePlugin
import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependencySpec


/**
 * The `gradle-enterprise` plugin.
 *
 * Visit the [Develocity Plugin User Manual](https://docs.gradle.com/enterprise/gradle-plugin/) for additional information.
 *
 * By default, the applied plugin version will be the same as the one used by the `--scan` command line option.
 *
 * You can also use e.g. `` `gradle-enterprise` version "3.0" `` to request a different version.
 *
 * @since 6.0
 */
val PluginDependenciesSpec.`gradle-enterprise`: PluginDependencySpec
    get() = this.id(AutoAppliedGradleEnterprisePlugin.GRADLE_ENTERPRISE_PLUGIN_ID.id).version(AutoAppliedGradleEnterprisePlugin.VERSION)


/**
 * The `develocity` plugin.
 *
 * Visit the [Develocity Plugin User Manual](https://docs.gradle.com/enterprise/gradle-plugin/) for additional information.
 *
 * By default, the applied plugin version will be the same as the one used by the `--scan` command line option.
 *
 * You can also use e.g. `` `develocity` version "3.17" `` to request a different version.
 *
 * @since 8.8
 */
@get:Incubating
val PluginDependenciesSpec.develocity: PluginDependencySpec
    get() = this.id(AutoAppliedGradleEnterprisePlugin.ID.id).version(AutoAppliedGradleEnterprisePlugin.VERSION)
