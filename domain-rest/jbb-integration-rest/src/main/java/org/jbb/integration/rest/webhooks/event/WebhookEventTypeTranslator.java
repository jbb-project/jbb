/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.integration.rest.webhooks.event;

import org.jbb.integration.api.webhooks.event.EventType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class WebhookEventTypeTranslator {

    public List<WebhookEventTypeDto> toDto(List<EventType> eventTypes) {
        return eventTypes.stream().map(this::toDto)
                .collect(Collectors.toList());
    }

    private WebhookEventTypeDto toDto(EventType type) {
        return WebhookEventTypeDto.builder()
                .eventName(type.getName())
                .eventVersion(type.getVersion())
                .build();
    }
}
