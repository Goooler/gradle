/*
 * Copyright 2020 the original author or authors.
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
package org.gradle.internal.component.model;

import org.gradle.api.attributes.Attribute;
import org.gradle.api.internal.attributes.AttributeContainerInternal;
import org.gradle.api.internal.attributes.ImmutableAttributes;
import org.gradle.api.internal.attributes.ImmutableAttributesEntry;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;

import java.util.Collection;

public class LoggingAttributeMatchingExplanationBuilder implements AttributeMatchingExplanationBuilder {
    private final static AttributeMatchingExplanationBuilder INSTANCE = new LoggingAttributeMatchingExplanationBuilder();
    private final static Logger LOGGER = Logging.getLogger(LoggingAttributeMatchingExplanationBuilder.class);

    static AttributeMatchingExplanationBuilder logging() {
        if (LOGGER.isDebugEnabled()) {
            return INSTANCE;
        }
        return AttributeMatchingExplanationBuilder.NO_OP;
    }

    @Override
    public void noCandidates(ImmutableAttributes requested) {
        LOGGER.debug("No candidates for {}. Select nothing.", requested);
    }

    @Override
    public void singleMatch(ImmutableAttributes candidate, Collection<ImmutableAttributes> candidates, AttributeContainerInternal requested) {
        LOGGER.debug("Selected match {} from candidates {} for {}", candidate, candidates, requested);
    }

    @Override
    public void candidateDoesNotMatchAttributes(ImmutableAttributes candidate, AttributeContainerInternal requested) {
        LOGGER.debug("Candidate {} doesn't match attributes {}", candidate, requested);
    }

    @Override
    public void candidateAttributeDoesNotMatch(ImmutableAttributes candidate, Attribute<?> attribute, Object requestedValue, ImmutableAttributesEntry<?> candidateEntry) {
        LOGGER.debug("Candidate {} attribute {} value {} doesn't requested value {}", candidate, attribute, candidateEntry, requestedValue);
    }

    @Override
    public void candidateAttributeMissing(ImmutableAttributes candidate, Attribute<?> attribute, Object requestedValue) {
        LOGGER.debug("Candidate {} doesn't have attribute {}", candidate, attribute);
    }

    @Override
    public void candidateIsSuperSetOfAllOthers(ImmutableAttributes candidate) {
        LOGGER.debug("Candidate {} selected because its attributes are a superset of all other candidate attributes", candidate);
    }
}
