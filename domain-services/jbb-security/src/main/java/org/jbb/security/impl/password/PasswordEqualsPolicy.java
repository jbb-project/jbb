/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.password;

import org.jbb.lib.commons.vo.Password;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PasswordEqualsPolicy {
    private final PasswordEncoder passwordEncoder;

    public boolean matches(Password typedPassword, Password currentPassword) {

        String decodedTypedPassword = String.copyValueOf(typedPassword.getValue());
        String encodedCurrentPassword = String.copyValueOf(currentPassword.getValue());

        return passwordEncoder.matches(decodedTypedPassword, encodedCurrentPassword);
    }
}
