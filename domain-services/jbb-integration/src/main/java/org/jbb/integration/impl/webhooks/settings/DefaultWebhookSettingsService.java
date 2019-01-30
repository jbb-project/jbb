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

import org.apache.commons.lang3.Validate;
import org.jbb.integration.api.webhooks.settings.WebhookSettings;
import org.jbb.integration.api.webhooks.settings.WebhookSettingsException;
import org.jbb.integration.api.webhooks.settings.WebhookSettingsService;
import org.jbb.integration.event.WebhookSettingsChangedEvent;
import org.jbb.lib.eventbus.JbbEventBus;
import org.springframework.stereotype.Service;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultWebhookSettingsService implements WebhookSettingsService {

    private final WebhookSettingsProvider settingsProvider;
    private final WebhookSettingsSaver settingsSaver;
    private final Validator validator;
    private final JbbEventBus eventBus;

    @Override
    public WebhookSettings getWebhookSettings() {
        return settingsProvider.currentWebhookSettings();
    }

    @Override
    public void setWebhookSettings(WebhookSettings newWebhookSettings) {
        Validate.notNull(newWebhookSettings);

        Set<ConstraintViolation<WebhookSettings>> validationResult = validator.validate(newWebhookSettings);
        if (!validationResult.isEmpty()) {
            throw new WebhookSettingsException(validationResult);
        }

        settingsSaver.save(newWebhookSettings);
        eventBus.post(new WebhookSettingsChangedEvent());
    }
}
