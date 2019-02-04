/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.integration.impl.webhooks.subscription;

import org.jbb.integration.api.webhooks.event.EventType;
import org.jbb.integration.api.webhooks.subscription.CreateUpdateWebhookSubscription;
import org.jbb.integration.api.webhooks.subscription.SubscribedEventTypesPolicy;
import org.jbb.integration.api.webhooks.subscription.WebhookSubscription;
import org.jbb.integration.impl.webhooks.event.model.WebhookEventTypeValue;
import org.jbb.integration.impl.webhooks.subscription.model.WebhookSubscriptionEntity;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SubscriptionDomainTranslator {

    public WebhookSubscriptionEntity toEntity(CreateUpdateWebhookSubscription subscription) {
        return WebhookSubscriptionEntity.builder()
                .enabled(subscription.getEnabled())
                .url(subscription.getUrl())
                .allSubscribed(subscription.getSubscribedEventTypes().getAllSubscribed())
                .subscribedEventTypes(toEventTypeValue(subscription.getSubscribedEventTypes().getEventTypes()))
                .headers(subscription.getHeaders())
                .build();
    }

    private Set<WebhookEventTypeValue> toEventTypeValue(Set<EventType> eventTypes) {
        return eventTypes.stream()
                .map(type -> new WebhookEventTypeValue(type.getName(), type.getVersion()))
                .collect(Collectors.toSet());
    }

    public WebhookSubscription toModel(WebhookSubscriptionEntity entity) {
        return WebhookSubscription.subscriptionBuilder()
                .subscriptionId(entity.getId())
                .enabled(entity.getEnabled())
                .url(entity.getUrl())
                .subscribedEventTypes(SubscribedEventTypesPolicy.builder()
                        .allSubscribed(entity.getAllSubscribed())
                        .eventTypes(toEventType(entity.getSubscribedEventTypes()))
                        .build())
                .headers(entity.getHeaders())
                .build();
    }

    private Set<EventType> toEventType(Set<WebhookEventTypeValue> subscribedEventTypes) {
        return subscribedEventTypes.stream()
                .map(type -> new EventType(type.getName(), type.getVersion()))
                .collect(Collectors.toSet());
    }

    public WebhookSubscriptionEntity updateEntity(WebhookSubscriptionEntity entity, CreateUpdateWebhookSubscription updateSubscription) {
        entity.setEnabled(updateSubscription.getEnabled());
        entity.setUrl(updateSubscription.getUrl());
        entity.setAllSubscribed(updateSubscription.getSubscribedEventTypes().getAllSubscribed());
        entity.setSubscribedEventTypes(toEventTypeValue(updateSubscription.getSubscribedEventTypes().getEventTypes()));
        entity.setHeaders(updateSubscription.getHeaders());
        return entity;
    }
}
