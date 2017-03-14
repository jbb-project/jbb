/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.password.logic;

import org.jbb.lib.core.vo.Password;
import org.jbb.security.impl.password.model.PasswordEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PasswordEntityFactory {
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PasswordEntityFactory(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public PasswordEntity create(Long memberId, Password newPassword) {
        String newPasswordStr = String.valueOf(newPassword.getValue());

        return PasswordEntity.builder()
                .memberId(memberId)
                .password(passwordEncoder.encode(newPasswordStr))
                .applicableSince(LocalDateTime.now())
                .visiblePassword(newPasswordStr)
                .build();
    }
}
