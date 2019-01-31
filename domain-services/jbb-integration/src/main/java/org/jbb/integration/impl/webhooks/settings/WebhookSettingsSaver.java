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

import static org.jbb.integration.impl.webhooks.WebhookProperties.WEBHOOK_CLEAN_AFTER_DAYS_KEY;
import static org.jbb.integration.impl.webhooks.WebhookProperties.WEBHOOK_RETRY_AMOUNT_KEY;
import static org.jbb.integration.impl.webhooks.WebhookProperties.WEBHOOK_TIMEOUT_CONNECTION_SECONDS_KEY;
import static org.jbb.integration.impl.webhooks.WebhookProperties.WEBHOOK_TIMEOUT_READ_SECONDS_KEY;

@Component
@RequiredArgsConstructor
public class WebhookSettingsSaver {

    private final WebhookProperties webhookProperties;

    public void save(WebhookSettings newWebhookSettings) {
        webhookProperties.setProperty(WEBHOOK_CLEAN_AFTER_DAYS_KEY, Integer.toString(newWebhookSettings.getCleanUpEventsAfterDays()));
        webhookProperties.setProperty(WEBHOOK_RETRY_AMOUNT_KEY, Integer.toString(newWebhookSettings.getNumberOfRetries()));
        webhookProperties.setProperty(WEBHOOK_TIMEOUT_CONNECTION_SECONDS_KEY, Integer.toString(newWebhookSettings.getConnectionTimeoutSeconds()));
        webhookProperties.setProperty(WEBHOOK_TIMEOUT_READ_SECONDS_KEY, Integer.toString(newWebhookSettings.getReadTimeoutSeconds()));
    }


}
