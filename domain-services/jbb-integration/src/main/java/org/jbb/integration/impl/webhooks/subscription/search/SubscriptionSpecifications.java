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

import org.jbb.integration.impl.webhooks.event.model.WebhookEventTypeValue;
import org.jbb.integration.impl.webhooks.subscription.model.WebhookSubscriptionEntity;
import org.jbb.integration.impl.webhooks.subscription.model.WebhookSubscriptionEntity_;
import org.springframework.data.jpa.domain.Specification;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class SubscriptionSpecifications {

    public static Specification<WebhookSubscriptionEntity> withEnabled(Boolean enabled) {
        if (enabled != null) {
            return (root, cq, cb) ->
                    cb.equal(root.get(WebhookSubscriptionEntity_.enabled), enabled);
        } else {
            return null;
        }
    }

    public static Specification<WebhookSubscriptionEntity> likeUrl(String url) {
        if (url != null) {
            return (root, cq, cb) ->
                    cb.like(cb.upper(root.get(WebhookSubscriptionEntity_.url)),
                            "%" + url.toUpperCase() + "%");
        } else {
            return null;
        }
    }

    public static Specification<WebhookSubscriptionEntity> withEventType(WebhookEventTypeValue eventType) {
        if (eventType != null) {
            return (root, cq, cb) -> cb.and(root.get(WebhookSubscriptionEntity_.subscribedEventTypes).in(eventType));
        } else {
            return null;
        }
    }

}
