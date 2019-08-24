/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.rest.signin;

import org.jbb.security.api.signin.SignInSettings;
import org.springframework.stereotype.Component;

@Component
public class SignInSettingsTranslator {

    public SignInSettingsDto toDto(SignInSettings signInSettings) {
        return SignInSettingsDto.builder()
                .rememberMeTokenValidityDays(signInSettings.getRememberMeTokenValidityDays())
                .basicAuthEnabled(signInSettings.getBasicAuthEnabled())
                .build();
    }

    public SignInSettings toModel(SignInSettingsDto dto) {
        return SignInSettings.builder()
                .rememberMeTokenValidityDays(dto.getRememberMeTokenValidityDays())
                .basicAuthEnabled(dto.getBasicAuthEnabled())
                .build();
    }
}
