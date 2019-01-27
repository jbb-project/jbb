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

import org.apache.commons.lang3.Validate;
import org.jbb.integration.api.webhooks.EventSearchCriteria;
import org.jbb.integration.api.webhooks.EventType;
import org.jbb.integration.api.webhooks.WebhookEvent;
import org.jbb.integration.api.webhooks.WebhookEventService;
import org.jbb.integration.api.webhooks.WebhookEventSummary;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultWebhookEventService implements WebhookEventService {

    @Override
    public Page<WebhookEventSummary> getEvents(EventSearchCriteria criteria) {
        Validate.notNull(criteria);
        return null;
    }

    @Override
    public Optional<WebhookEvent> getEvent(String eventId) {
        return Optional.empty();
    }

    @Override
    public List<EventType> getAllEventTypes() {
        return null;
    }
}
