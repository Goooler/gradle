/*
 * Copyright 2011 the original author or authors.
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

package org.gradle.tooling.model;

import org.jspecify.annotations.Nullable;

/**
 * Represents an element which belongs to some hierarchy.
 *
 * @since 1.0-milestone-5
 */
public interface HierarchicalElement extends Element {

    /**
     * Returns the parent of this element, or {@code null} if there is no parent.
     *
     * @return The parent of this element, or {@code null} if there is no parent.
     * @since 1.0-milestone-5
     */
    @Nullable
    HierarchicalElement getParent();

    /**
     * Returns the child elements, or the empty set if there are no child elements.
     *
     * @return The child elements, or the empty set if there are no child elements.
     * @since 1.0-milestone-5
     */
    DomainObjectSet<? extends HierarchicalElement> getChildren();

}
