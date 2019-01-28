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

import com.google.common.eventbus.Subscribe;

import org.jbb.integration.impl.webhooks.dao.WebhookEventRepository;
import org.jbb.integration.impl.webhooks.model.WebhookEventEntity;
import org.jbb.lib.eventbus.JbbEvent;
import org.jbb.lib.eventbus.JbbEventBusListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventListener implements JbbEventBusListener {
    private final WebhookEventRepository eventRepository;
    private final JbbEventTranslator jbbEventTranslator;

    @Subscribe
    @Transactional
    public void saveEvent(JbbEvent event) {
        log.debug("Saving webhook event to database with eventId: {}", event.getEventId());
        WebhookEventEntity eventEntity = jbbEventTranslator.toEntity(event);
        eventRepository.save(eventEntity);
    }


}
