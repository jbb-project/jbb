/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.integration.impl.webhooks.settings;

import org.jbb.integration.api.webhooks.settings.WebhookSettings;
import org.jbb.integration.impl.webhooks.WebhookProperties;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WebhookSettingsProvider {

    private final WebhookProperties webhookProperties;

    public WebhookSettings currentWebhookSettings() {
        return WebhookSettings.builder()
                .cleanUpEventsAfterDays(webhookProperties.cleanUpAfterDays())
                .numberOfRetries(webhookProperties.numberOfRetries())
                .build();
    }

}
