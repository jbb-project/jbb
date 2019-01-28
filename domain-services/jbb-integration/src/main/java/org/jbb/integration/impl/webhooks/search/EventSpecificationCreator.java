/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.integration.impl.webhooks.search;

import org.jbb.integration.api.webhooks.EventSearchCriteria;
import org.jbb.integration.impl.webhooks.model.WebhookEventEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import static org.jbb.integration.impl.webhooks.search.EventSpecifications.withEventType;

@Component
public class EventSpecificationCreator {

    public Specification<WebhookEventEntity> createSpecification(EventSearchCriteria criteria) {
        return Specification.where(withEventType(criteria.getEventType().orElse(null)));
    }
}
