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

import org.jbb.integration.api.webhooks.EventType;
import org.jbb.integration.impl.webhooks.model.WebhookEventEntity;
import org.jbb.integration.impl.webhooks.model.WebhookEventEntity_;
import org.springframework.data.jpa.domain.Specification;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class EventSpecifications {

    public static Specification<WebhookEventEntity> withEventType(EventType eventType) {
        if (eventType != null) {
            return withEventName(eventType.getName()).and(withEventVersion(eventType.getVersion()));
        } else {
            return null;
        }
    }

    private static Specification<WebhookEventEntity> withEventName(String eventName) {
        if (eventName != null) {
            return (root, cq, cb) ->
                    cb.equal(root.get(WebhookEventEntity_.eventName), eventName);
        } else {
            return null;
        }
    }

    private static Specification<WebhookEventEntity> withEventVersion(String eventVersion) {
        if (eventVersion != null) {
            return (root, cq, cb) ->
                    cb.equal(root.get(WebhookEventEntity_.eventVersion), eventVersion);
        } else {
            return null;
        }
    }

}
