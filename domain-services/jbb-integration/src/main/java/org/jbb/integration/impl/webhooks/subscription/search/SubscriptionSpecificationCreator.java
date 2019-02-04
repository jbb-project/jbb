/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.integration.impl.webhooks.subscription.search;

import org.jbb.integration.api.webhooks.subscription.SubscriptionSearchCriteria;
import org.jbb.integration.impl.webhooks.event.model.WebhookEventTypeValue;
import org.jbb.integration.impl.webhooks.subscription.model.WebhookSubscriptionEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import static org.jbb.integration.impl.webhooks.subscription.search.SubscriptionSpecifications.likeUrl;
import static org.jbb.integration.impl.webhooks.subscription.search.SubscriptionSpecifications.withEnabled;
import static org.jbb.integration.impl.webhooks.subscription.search.SubscriptionSpecifications.withEventType;

@Component
public class SubscriptionSpecificationCreator {

    public Specification<WebhookSubscriptionEntity> createSpecification(SubscriptionSearchCriteria criteria) {
        return Specification.where(withEnabled(criteria.getEnabled().orElse(null)))
                .and(likeUrl(criteria.getUrl().orElse(null)))
                .and(withEventType(criteria.getEventType()
                        .map(x -> new WebhookEventTypeValue(x.getName(), x.getVersion()))
                        .orElse(null)));
    }
}
