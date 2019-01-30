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
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import static org.jbb.integration.impl.IntegrationConfig.ROUTING_QUEUE_NAME;

@Slf4j
@Component
public class RoutingAmqpListener {

    @RabbitListener(queues = ROUTING_QUEUE_NAME)
    public void foo(WebhookEventEntity eventEntity) {
        log.info("Event received {}", eventEntity);

    }
}
