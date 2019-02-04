/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.integration.impl.webhooks.subscription.model.validation;

import org.jbb.integration.impl.webhooks.subscription.model.WebhookSubscriptionEntity;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ValidSubscribedEventTypesPolicyValidator implements
        ConstraintValidator<ValidSubscribedEventTypesPolicy, WebhookSubscriptionEntity> {

    @Override
    public void initialize(ValidSubscribedEventTypesPolicy validSubscribedEventTypesPolicy) {
        // not needed...
    }

    @Override
    public boolean isValid(WebhookSubscriptionEntity subscription, ConstraintValidatorContext context) {
        if (subscription.getAllSubscribed() && !subscription.getSubscribedEventTypes().isEmpty()) {
            return addViolation(context, "allSubscribed enabled but event types specified as well");
        } else if (!subscription.getAllSubscribed() && subscription.getSubscribedEventTypes().isEmpty()) {
            return addViolation(context, "allSubscribed disabled and no subscribed event type specified");
        }
        return true;
    }

    private boolean addViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context
                .buildConstraintViolationWithTemplate(message)
                .addPropertyNode("subscribedEventTypes").addConstraintViolation();
        return false;
    }

}
