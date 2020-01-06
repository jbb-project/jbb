/*
 * Copyright (C) 2020 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.integration.rest.webhooks.event;

import org.apache.commons.lang3.StringUtils;
import org.jbb.integration.api.webhooks.event.EventSearchCriteria;
import org.jbb.integration.api.webhooks.event.EventType;
import org.jbb.integration.api.webhooks.event.WebhookEvent;
import org.jbb.integration.api.webhooks.event.WebhookEventService;
import org.jbb.integration.rest.webhooks.event.exception.InvalidNameVersionCriteriaParam;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Component
@RequiredArgsConstructor
public class WebhookEventTranslator {

    private final WebhookEventService webhookEventService;

    public EventSearchCriteria toCriteria(WebhookEventCriteriaDto dto) {
        return EventSearchCriteria.builder()
                .eventType(foo(dto))
                .eventName(Optional.ofNullable(dto.getEventName()))
                .pageRequest(PageRequest.of(dto.getPage(), dto.getPageSize()))
                .build();
    }

    private Optional<EventType> foo(WebhookEventCriteriaDto dto) {
        if (isNotBlank(dto.getEventName()) && isNotBlank(dto.getEventVersion())) {
            return Optional.of(EventType.builder()
                    .name(dto.getEventName())
                    .version(dto.getEventVersion())
                    .build());
        }
        return Optional.empty();
    }

    private EventType findType(String eventNameVersion) {
        String[] result = eventNameVersion.split(StringUtils.SPACE);
        if (result.length != 2) {
            throw new InvalidNameVersionCriteriaParam();
        }
        String eventName = result[0];
        String eventVersion = result[1];
        return webhookEventService.getAllEventTypes().stream()
                .filter(type -> type.getName().equals(eventName) && type.getVersion().equals(eventVersion))
                .findFirst().orElseThrow(InvalidNameVersionCriteriaParam::new);
    }

    public WebhookEventDto toDto(WebhookEvent event) {
        return WebhookEventDto.builder()
                .eventId(event.getEventId())
                .eventName(event.getEventType().getName())
                .eventVersion(event.getEventType().getVersion())
                .publishDateTime(event.getPublishDateTime())
                .processingStatus(event.getProcessingStatus())
                .requestId(event.getSourceRequestId().orElse(null))
                .sourceMemberId(event.getSourceMemberId().orElse(null))
                .oAuthClientId(event.getSourceOAuthClientId().orElse(null))
                .ipAddress(event.getSourceIpAddress().orElse(null))
                .sessionId(event.getSourceSessionId().orElse(null))
                .details(event.getDetails())
                .build();
    }
}
