/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.password.logic;

import org.jbb.lib.core.vo.Password;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEqualsPolicy {
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PasswordEqualsPolicy(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public boolean matches(Password typedPassword, Password currentPassword) {

        String decodedTypedPassword = String.copyValueOf(typedPassword.getValue());
        String encodedCurrentPassword = String.copyValueOf(currentPassword.getValue());

        return passwordEncoder.matches(decodedTypedPassword, encodedCurrentPassword);
    }
}
