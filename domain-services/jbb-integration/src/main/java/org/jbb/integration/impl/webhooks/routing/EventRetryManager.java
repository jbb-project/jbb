/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.integration.impl.webhooks.routing;

import org.jbb.integration.impl.webhooks.event.model.WebhookEventEntity;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventRetryManager {

    private final RoutingAmqpListener routingAmqpListener;

    public void retry(WebhookEventEntity event) {
        log.info("Retrying webhook event with eventId: {}", event.getEventId());
        routingAmqpListener.route(event);
    }
}
