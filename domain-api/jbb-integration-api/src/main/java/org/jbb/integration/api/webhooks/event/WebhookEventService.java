/*
 * Copyright (C) 2020 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.integration.api.webhooks.event;

import org.springframework.data.domain.Page;

import java.util.List;

public interface WebhookEventService {

    Page<WebhookEvent> getEvents(EventSearchCriteria criteria);

    WebhookEvent getEvent(String webhookEventId);

    void deleteEvent(String webhookEventId);

    WebhookEvent retryEventProcessing(String webhookEventId);

    List<EventType> getAllEventTypes();
}
