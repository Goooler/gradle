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

package org.gradle.api.internal.plugins.software;

import org.gradle.api.Incubating;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method as exposing a software type.  This should be used in plugin classes to communicate which software types they provide.
 *
 * @since 8.8
 */
@Incubating
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SoftwareType {
    /**
     * The name of the software type.  This will correspond to the DSL element that is exposed to configure the software type.
     *
     * @since 8.8
     */
    String name();

    /**
     * The model type used to configure the software type.  Note that the actual return type of the method may be a subtype of this type.
     *
     * @since 8.8
     */
    Class<?> modelPublicType();
}
