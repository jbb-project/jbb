/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.integration.impl.webhooks.subscription.model.validation.update;

import org.jbb.integration.impl.webhooks.subscription.dao.WebhookSubscriptionRepository;
import org.jbb.integration.impl.webhooks.subscription.model.WebhookSubscriptionEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UrlNotBusyUpdateValidator implements
        ConstraintValidator<UrlNotBusyUpdate, WebhookSubscriptionEntity> {

    private final WebhookSubscriptionRepository subscriptionRepository;

    private String message;

    @Override
    public void initialize(UrlNotBusyUpdate urlNotBusyUpdate) {
        message = urlNotBusyUpdate.message();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isValid(WebhookSubscriptionEntity subscription, ConstraintValidatorContext context) {
        Long subscriptionId = subscription.getId();
        final Optional<WebhookSubscriptionEntity> foundSubscription = subscriptionRepository.findById(subscriptionId);
        if (foundSubscription.isPresent()) {
            boolean result = foundSubscription.get().getId().equals(subscription.getId());
            if (!result) {
                context.disableDefaultConstraintViolation();
                context
                        .buildConstraintViolationWithTemplate(message)
                        .addPropertyNode("url").addConstraintViolation();
                return false;
            }
        }
        return true;
    }

}
