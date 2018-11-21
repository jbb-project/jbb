/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.rest.password;

import org.jbb.security.api.password.PasswordPolicy;
import org.springframework.stereotype.Component;

@Component
public class PasswordPolicyTranslator {

    public PasswordPolicyDto toDto(PasswordPolicy passwordPolicy) {
        return PasswordPolicyDto.builder()
            .minimumLength(passwordPolicy.getMinimumLength())
            .maximumLength(passwordPolicy.getMaximumLength())
                .build();
    }

    public PasswordPolicy toModel(PasswordPolicyDto dto) {
        return PasswordPolicy.builder()
                .minimumLength(dto.getMinimumLength())
                .maximumLength(dto.getMaximumLength())
                .build();
    }
}
