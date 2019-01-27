/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.integration.rest.webhooks;

import org.jbb.integration.api.webhooks.EventSearchCriteria;
import org.jbb.integration.api.webhooks.WebhookEvent;
import org.jbb.integration.api.webhooks.WebhookEventSummary;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class WebhookEventTranslator {

    public EventSearchCriteria toCriteria(WebhookEventCriteriaDto dto) {
        return EventSearchCriteria.builder()
                .eventId(Optional.ofNullable(dto.getEventId()))
                .eventType(Optional.empty())
                .pageRequest(PageRequest.of(dto.getPage(), dto.getPageSize()))
                .build();
    }

    public WebhookEventSummaryDto toSummaryDto(WebhookEventSummary summary) {
        return WebhookEventSummaryDto.builder()
                .eventId(summary.getEventId())
                .eventName(summary.getEventType().getName())
                .eventVersion(summary.getEventType().getVersion())
                .publishDateTime(summary.getPublishDateTime())
                .build();
    }

    public WebhookEventDto toDto(WebhookEvent event) {
        return WebhookEventDto.builder()
                .eventId(event.getEventId())
                .eventName(event.getEventType().getName())
                .eventVersion(event.getEventType().getVersion())
                .publishDateTime(event.getPublishDateTime())
                .requestId(event.getSourceRequestId().orElse(null))
                .sourceMemberId(event.getSourceMemberId().orElse(null))
                .oAuthClientId(event.getSourceOAuthClientId().orElse(null))
                .ipAddress(event.getSourceIpAddress().orElse(null))
                .sessionId(event.getSourceSessionId().orElse(null))
                .details(event.getDetails())
                .build();
    }
}
