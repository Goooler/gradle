/*
 * Copyright 2021 the original author or authors.
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

package org.gradle.internal.cc.impl.services

import org.gradle.initialization.Environment
import org.gradle.internal.event.ListenerManager
import org.gradle.internal.extensions.core.getBroadcaster
import org.gradle.internal.extensions.stdlib.filterKeysByPrefix
import org.gradle.internal.extensions.stdlib.uncheckedCast
import org.gradle.internal.resource.local.FileResourceListener
import org.gradle.internal.service.scopes.EventScope
import org.gradle.internal.service.scopes.Scope
import org.gradle.util.internal.GUtil
import java.io.File


/**
 * Augments the [DefaultEnvironment] to track access to properties files, environment variables and system properties.
 **/
class ConfigurationCacheEnvironment(
    private val listenerManager: ListenerManager
) : DefaultEnvironment() {

    @EventScope(Scope.BuildTree::class)
    interface Listener {
        fun systemPropertiesPrefixedBy(prefix: String, snapshot: Map<String, String?>)
        fun systemProperty(name: String, value: String?)
        fun envVariablesPrefixedBy(prefix: String, snapshot: Map<String, String?>)
        fun envVariable(name: String, value: String?)
    }

    private
    val fileResourceListener: FileResourceListener by lazy(listenerManager::getBroadcaster)

    private
    val listener: Listener by lazy(listenerManager::getBroadcaster)

    override fun propertiesFile(propertiesFile: File): Map<String, String>? {
        fileResourceListener.fileObserved(propertiesFile)
        return super.propertiesFile(propertiesFile)
    }

    override fun getSystemProperties(): Environment.Properties =
        TrackingProperties(
            System.getProperties().uncheckedCast(),
            { prefix, snapshot -> listener.systemPropertiesPrefixedBy(prefix, snapshot) },
            { name, value -> listener.systemProperty(name, value) }
        )

    override fun getVariables(): Environment.Properties =
        TrackingProperties(
            System.getenv(),
            { prefix, snapshot -> listener.envVariablesPrefixedBy(prefix, snapshot) },
            { name, value -> listener.envVariable(name, value) }
        )

    private
    class TrackingProperties(
        map: Map<String, String>,
        val onByNamePrefix: (String, Map<String, String?>) -> Unit,
        val onByName: (String, String?) -> Unit
    ) : DefaultProperties(map) {
        override fun byNamePrefix(prefix: String): Map<String, String> =
            super.byNamePrefix(prefix).also { snapshot ->
                onByNamePrefix(prefix, snapshot)
            }

        override fun get(name: String): String? =
            super.get(name).also { value ->
                onByName(name, value)
            }
    }
}


/**
 * Gives direct access to system resources.
 */
open class DefaultEnvironment : Environment {

    override fun propertiesFile(propertiesFile: File): Map<String, String>? = when {
        propertiesFile.isFile -> GUtil.loadProperties(propertiesFile).uncheckedCast()
        else -> null
    }

    override fun getSystemProperties(): Environment.Properties =
        DefaultProperties(System.getProperties().uncheckedCast())

    override fun getVariables(): Environment.Properties =
        DefaultProperties(System.getenv())

    internal
    open class DefaultProperties(val map: Map<String, String>) : Environment.Properties {
        override fun byNamePrefix(prefix: String): Map<String, String> =
            map.filterKeysByPrefix(prefix)

        override fun get(name: String): String? =
            map[name]
    }
}
