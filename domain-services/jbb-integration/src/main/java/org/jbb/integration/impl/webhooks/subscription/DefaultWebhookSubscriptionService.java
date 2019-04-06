/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.integration.impl.webhooks.subscription;

import org.apache.commons.lang3.Validate;
import org.jbb.integration.api.webhooks.event.EventType;
import org.jbb.integration.api.webhooks.subscription.CreateUpdateWebhookSubscription;
import org.jbb.integration.api.webhooks.subscription.SubscriptionSearchCriteria;
import org.jbb.integration.api.webhooks.subscription.WebhookSubscription;
import org.jbb.integration.api.webhooks.subscription.WebhookSubscriptionException;
import org.jbb.integration.api.webhooks.subscription.WebhookSubscriptionNotFoundException;
import org.jbb.integration.api.webhooks.subscription.WebhookSubscriptionService;
import org.jbb.integration.event.WebhookSubscriptionChangedEvent;
import org.jbb.integration.event.WebhookSubscriptionCreatedEvent;
import org.jbb.integration.event.WebhookSubscriptionRemovedEvent;
import org.jbb.integration.impl.webhooks.subscription.dao.WebhookSubscriptionRepository;
import org.jbb.integration.impl.webhooks.subscription.model.WebhookSubscriptionEntity;
import org.jbb.integration.impl.webhooks.subscription.model.validation.create.CreateGroup;
import org.jbb.integration.impl.webhooks.subscription.model.validation.update.UpdateGroup;
import org.jbb.integration.impl.webhooks.subscription.search.SubscriptionSpecificationCreator;
import org.jbb.lib.eventbus.JbbEventBus;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultWebhookSubscriptionService implements WebhookSubscriptionService {
    private final WebhookSubscriptionRepository subscriptionRepository;
    private final SubscriptionDomainTranslator domainTranslator;
    private final SubscriptionSpecificationCreator specificationCreator;
    private final Validator validator;
    private final JbbEventBus eventBus;

    @Override
    @Transactional
    public WebhookSubscription createSubscription(CreateUpdateWebhookSubscription webhookSubscription) {
        WebhookSubscriptionEntity subscriptionEntity = domainTranslator.toEntity(webhookSubscription);
        Set<ConstraintViolation<WebhookSubscriptionEntity>> violations = validator.validate(subscriptionEntity, Default.class, CreateGroup.class);
        if (!violations.isEmpty()) {
            throw new WebhookSubscriptionException(violations);
        }
        WebhookSubscriptionEntity savedEntity = subscriptionRepository.save(subscriptionEntity);
        eventBus.post(new WebhookSubscriptionCreatedEvent(savedEntity.getId()));
        return domainTranslator.toModel(savedEntity);
    }

    @Override
    @Transactional
    public WebhookSubscription updateSubscription(Long subscriptionId, CreateUpdateWebhookSubscription updateSubscription) {
        WebhookSubscriptionEntity subscriptionEntity = subscriptionRepository.findById(subscriptionId).orElseThrow(WebhookSubscriptionNotFoundException::new);
        WebhookSubscriptionEntity updatedSubscriptionEntity = domainTranslator.updateEntity(subscriptionEntity, updateSubscription);
        Set<ConstraintViolation<WebhookSubscriptionEntity>> violations = validator.validate(updatedSubscriptionEntity, Default.class, UpdateGroup.class);
        if (!violations.isEmpty()) {
            throw new WebhookSubscriptionException(violations);
        }
        final WebhookSubscriptionEntity savedEntity = subscriptionRepository.save(updatedSubscriptionEntity);
        eventBus.post(new WebhookSubscriptionChangedEvent(subscriptionId));
        return domainTranslator.toModel(savedEntity);
    }

    @Override
    @Transactional
    public void deleteSubscription(Long subscriptionId) {
        WebhookSubscriptionEntity subscription = subscriptionRepository.findById(subscriptionId).orElseThrow(WebhookSubscriptionNotFoundException::new);
        subscriptionRepository.deleteById(subscription.getId());
        eventBus.post(new WebhookSubscriptionRemovedEvent(subscriptionId));
    }

    @Override
    @Transactional(readOnly = true)
    public WebhookSubscription getSubscription(Long subscriptionId) {
        return subscriptionRepository.findById(subscriptionId).map(domainTranslator::toModel)
                .orElseThrow(WebhookSubscriptionNotFoundException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WebhookSubscription> getSubscription(SubscriptionSearchCriteria criteria) {
        Validate.notNull(criteria);
        Specification<WebhookSubscriptionEntity> spec = specificationCreator.createSpecification(criteria);
        return subscriptionRepository.findAll(spec, criteria.getPageRequest())
                .map(domainTranslator::toModel);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WebhookSubscription> getEnabledSubscriptionsForEventType(EventType eventType) {
        final SubscriptionSearchCriteria criteria = SubscriptionSearchCriteria.builder()
                .enabled(Optional.of(true))
                .eventType(Optional.of(eventType))
                .build();
        Specification<WebhookSubscriptionEntity> spec = specificationCreator.createSpecification(criteria);
        return subscriptionRepository.findAll(spec).stream()
                .map(domainTranslator::toModel)
                .collect(Collectors.toList());
    }
}
