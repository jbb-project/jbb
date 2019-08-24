/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.signin;

import org.apache.commons.lang3.Validate;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.security.api.signin.SignInSettings;
import org.jbb.security.api.signin.SignInSettingsService;
import org.jbb.security.event.SignInSettingsChangedEvent;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultSignInSettingsService implements SignInSettingsService {

    private final SignInProperties properties;
    private final SignInSettingsValidator settingsValidator;
    private final JbbEventBus eventBus;

    @Override
    public SignInSettings getSignInSettings() {
        return SignInSettings.builder()
                .basicAuthEnabled(properties.basicAuthEnabled())
                .rememberMeTokenValidityDays(properties.rememberMeTokenValidityDays())
                .build();
    }

    @Override
    public void setSignInSettings(SignInSettings signInSettings) {
        Validate.notNull(signInSettings);
        settingsValidator.validate(signInSettings);
        properties.setProperty(SignInProperties.BASIC_AUTH_ENABLED, Boolean.toString(signInSettings.getBasicAuthEnabled()));
        properties.setProperty(SignInProperties.REMEMBER_ME_TOKEN_VALIDITY_DAYS, Long.toString(signInSettings.getRememberMeTokenValidityDays()));
        eventBus.post(new SignInSettingsChangedEvent());
    }
}
