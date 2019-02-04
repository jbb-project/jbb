/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.integration.impl.webhooks.subscription.model.validation.create;

import org.jbb.integration.impl.webhooks.subscription.dao.WebhookSubscriptionRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UrlNotBusyCreateValidator implements
        ConstraintValidator<UrlNotBusyCreate, String> {

    private final WebhookSubscriptionRepository subscriptionRepository;

    @Override
    public void initialize(UrlNotBusyCreate urlNotBusyCreate) {
        // not needed...
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isValid(String url,
                           ConstraintValidatorContext constraintValidatorContext) {
        return !subscriptionRepository.findFirstByUrl(url).isPresent();
    }

}
