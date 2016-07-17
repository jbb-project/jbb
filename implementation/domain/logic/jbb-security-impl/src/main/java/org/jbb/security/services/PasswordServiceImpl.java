/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.services;

import org.jbb.lib.core.vo.Login;
import org.jbb.security.SecurityConfig;
import org.jbb.security.api.model.Password;
import org.jbb.security.api.services.PasswordService;
import org.jbb.security.dao.PasswordRepository;
import org.jbb.security.dao.SecurityAccountDetailsRepository;
import org.jbb.security.entities.PasswordEntity;
import org.jbb.security.entities.SecurityAccountDetailsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PasswordServiceImpl implements PasswordService {
    private final PasswordEncoder passwordEncoder;
    private final PasswordRepository passwordRepository;
    private final SecurityAccountDetailsRepository securityRepository;

    @Autowired
    public PasswordServiceImpl(PasswordEncoder passwordEncoder,
                               PasswordRepository passwordRepository,
                               SecurityAccountDetailsRepository securityRepository) {
        this.passwordEncoder = passwordEncoder;
        this.passwordRepository = passwordRepository;
        this.securityRepository = securityRepository;
    }

    @Override
    @Transactional(transactionManager = SecurityConfig.JTA_MANAGER)
    public void changeFor(Login login, Password newPassword) {
        PasswordEntity password = PasswordEntity.builder()
                .login(login)
                .password(passwordEncoder.encode(String.valueOf(newPassword.getValue())))
                .applicableSince(LocalDateTime.now())
                .build();

        PasswordEntity passwordEntity = passwordRepository.save(password);

        SecurityAccountDetailsEntity securityDetails = securityRepository.findByLogin(login);
        securityDetails.setCurrentPassword(passwordEntity);
    }

    @Override
    @Transactional(transactionManager = SecurityConfig.JTA_MANAGER, readOnly = true)
    public boolean verifyFor(Login login, Password typedPassword) {
        SecurityAccountDetailsEntity securityDetails = securityRepository.findByLogin(login);

        PasswordEntity currentPassword = securityDetails.getCurrentPassword();
        String encodedTypedPassword = passwordEncoder.encode(String.valueOf(typedPassword.getValue()));
        String encodedCurrentPassword = currentPassword.getPassword();

        return encodedCurrentPassword.equals(encodedTypedPassword);
    }

    @Override
    public boolean verifyExpirationFor(Login login) {
        // TODO
        return false;
    }
}
