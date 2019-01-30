/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.integration.impl.webhooks.event;

import org.apache.commons.lang3.Validate;
import org.jbb.integration.api.webhooks.event.EventSearchCriteria;
import org.jbb.integration.api.webhooks.event.EventType;
import org.jbb.integration.api.webhooks.event.WebhookEvent;
import org.jbb.integration.api.webhooks.event.WebhookEventNotFoundException;
import org.jbb.integration.api.webhooks.event.WebhookEventService;
import org.jbb.integration.api.webhooks.event.WebhookEventSummary;
import org.jbb.integration.impl.webhooks.event.dao.WebhookEventRepository;
import org.jbb.integration.impl.webhooks.event.model.WebhookEventEntity;
import org.jbb.integration.impl.webhooks.event.search.EventSpecificationCreator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultWebhookEventService implements WebhookEventService {

    private final WebhookEventRepository eventRepository;
    private final EventDomainTranslator domainTranslator;
    private final EventSpecificationCreator specificationCreator;

    @Override
    @Transactional(readOnly = true)
    public Page<WebhookEventSummary> getEvents(EventSearchCriteria criteria) {
        Validate.notNull(criteria);
        Specification<WebhookEventEntity> spec = specificationCreator.createSpecification(criteria);
        PageRequest pageRequest = domainTranslator.toTargetPageRequest(criteria.getPageRequest());
        return eventRepository.findAll(spec, pageRequest)
                .map(domainTranslator::toSummaryModel);
    }

    @Override
    @Transactional(readOnly = true)
    public WebhookEvent getEvent(String eventId) {
        return eventRepository.findByEventId(eventId)
                .map(domainTranslator::toModel)
                .orElseThrow(WebhookEventNotFoundException::new);
    }

    @Override
    @Transactional
    public void deleteEvent(String eventId) {
        WebhookEventEntity eventEntity = eventRepository.findByEventId(eventId)
                .orElseThrow(WebhookEventNotFoundException::new);
        eventRepository.deleteById(eventEntity.getId());
    }

    @Override
    public List<EventType> getAllEventTypes() {
        return null;
    }
}
