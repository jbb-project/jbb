/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.integration.impl.webhooks;

import org.jbb.integration.api.webhooks.EventType;
import org.jbb.integration.api.webhooks.WebhookEvent;
import org.jbb.integration.api.webhooks.WebhookEventSummary;
import org.jbb.integration.impl.webhooks.model.WebhookEventDetailValue;
import org.jbb.integration.impl.webhooks.model.WebhookEventEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

@Component
public class EventDomainTranslator {

    public WebhookEvent toModel(WebhookEventEntity entity) {
        return WebhookEvent.builder()
                .eventId(entity.getEventId())
                .eventType(new EventType(entity.getEventName(), entity.getEventVersion()))
                .creationDateTime(entity.getCreatedAt())
                .publishDateTime(entity.getPublishedAt())
                .sourceRequestId(Optional.ofNullable(entity.getSourceRequestId()))
                .sourceMemberId(Optional.ofNullable(entity.getSourceMemberId()))
                .sourceOAuthClientId(Optional.ofNullable(entity.getSourceOAuthClientId()))
                .sourceIpAddress(Optional.ofNullable(entity.getSourceIpAddress()))
                .sourceSessionId(Optional.ofNullable(entity.getSourceSessionId()))
                .details(toModel(entity.getDetails()))
                .build();
    }

    private Map<String, Object> toModel(Map<String, WebhookEventDetailValue> details) {
        Map<String, Object> result = new TreeMap<>(Comparator.nullsFirst(Comparator.naturalOrder()));
        details.forEach((key, value) -> result.put(key, value.value()));
        return result;
    }

    public WebhookEventSummary toSummaryModel(WebhookEventEntity entity) {
        return WebhookEventSummary.builder()
                .eventId(entity.getEventId())
                .eventType(new EventType(entity.getEventName(), entity.getEventVersion()))
                .publishDateTime(entity.getPublishedAt())
                .build();
    }

    public PageRequest toTargetPageRequest(Pageable request) {
        return PageRequest.of(request.getPageNumber(), request.getPageSize(), Sort.by(Sort.Direction.DESC, "publishedAt"));
    }

}
