/*
 * Copyright (C) 2019 the original author or authors.
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

    Page<WebhookEventSummary> getEvents(EventSearchCriteria criteria);

    WebhookEvent getEvent(String eventId);

    void deleteEvent(String eventId);

    WebhookEvent retryEventProcessing(String eventId);

    List<EventType> getAllEventTypes();
}
