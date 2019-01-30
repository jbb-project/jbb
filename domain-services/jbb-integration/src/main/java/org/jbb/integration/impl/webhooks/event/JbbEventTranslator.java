/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.integration.impl.webhooks.event;

import com.google.common.collect.Lists;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.jbb.integration.impl.webhooks.event.model.WebhookEventDetailValue;
import org.jbb.integration.impl.webhooks.event.model.WebhookEventEntity;
import org.jbb.lib.eventbus.JbbEvent;
import org.jbb.lib.eventbus.webhooks.WebhookEvent;
import org.jbb.lib.eventbus.webhooks.WebhookField;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JbbEventTranslator {

    public WebhookEventEntity toEntity(JbbEvent event) {
        return WebhookEventEntity.builder()
                .eventId(event.getEventId())
                .eventName(findEventName(event))
                .eventVersion(findEventVersion(event))
                .createdAt(event.getCreationDateTime())
                .publishedAt(event.getPublishDateTime())
                .sourceRequestId(event.getSourceRequestId().orElse(null))
                .sourceMemberId(event.getSourceMemberId().orElse(null))
                .sourceOAuthClientId(event.getSourceOAuthClientId().orElse(null))
                .sourceIpAddress(event.getSourceIpAddress().orElse(null))
                .sourceSessionId(event.getSourceSessionId().orElse(null))
                .details(toDetailsEntity(event))
                .build();
    }

    private String findEventName(JbbEvent event) {
        WebhookEvent webhookAnnotation = event.getClass().getAnnotation(WebhookEvent.class);
        return webhookAnnotation.name();
    }

    private String findEventVersion(JbbEvent event) {
        WebhookEvent webhookAnnotation = event.getClass().getAnnotation(WebhookEvent.class);
        // FIXME add support for many versions (event -> List<WebhookEventEntity>)
        return webhookAnnotation.versions()[0];
    }

    private Map<String, WebhookEventDetailValue> toDetailsEntity(JbbEvent event) {
        List<Field> fields = Lists.newArrayList(FieldUtils.getFieldsWithAnnotation(event.getClass(), WebhookField.class));
        return fields.stream()
                .collect(Collectors.toMap(Field::getName,
                        field -> new WebhookEventDetailValue(field, event)));
    }

}
