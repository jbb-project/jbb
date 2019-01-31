/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.integration.rest.webhooks.settings;

import org.jbb.integration.api.webhooks.settings.WebhookSettings;
import org.springframework.stereotype.Component;

@Component
public class WebhookSettingsTranslator {

    public WebhookSettingsDto toDto(WebhookSettings webhookSettings) {
        return WebhookSettingsDto.builder()
                .cleanUpEventsAfterDays(webhookSettings.getCleanUpEventsAfterDays())
                .numberOfRetries(webhookSettings.getNumberOfRetries())
                .connectionTimeoutSeconds(webhookSettings.getConnectionTimeoutSeconds())
                .readTimeoutSeconds(webhookSettings.getReadTimeoutSeconds())
                .build();
    }

    public WebhookSettings toModel(WebhookSettingsDto dto) {
        return WebhookSettings.builder()
                .cleanUpEventsAfterDays(dto.getCleanUpEventsAfterDays())
                .numberOfRetries(dto.getNumberOfRetries())
                .connectionTimeoutSeconds(dto.getConnectionTimeoutSeconds())
                .readTimeoutSeconds(dto.getReadTimeoutSeconds())
                .build();
    }

}
