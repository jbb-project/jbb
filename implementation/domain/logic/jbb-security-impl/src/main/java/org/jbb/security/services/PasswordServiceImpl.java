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

import org.apache.commons.lang3.Validate;
import org.jbb.lib.core.vo.Login;
import org.jbb.lib.core.vo.Password;
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
import java.util.Optional;

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
    @Transactional
    public void changeFor(Login login, Password newPassword) {
        Validate.notNull(login, "Login cannot be null");
        Validate.notNull(newPassword, "Password cannot be null");

        PasswordEntity password = PasswordEntity.builder()
                .login(login)
                .password(passwordEncoder.encode(String.valueOf(newPassword.getValue())))
                .applicableSince(LocalDateTime.now())
                .build();

        PasswordEntity passwordEntity = passwordRepository.save(password);

        Optional<SecurityAccountDetailsEntity> securityDetails = securityRepository.findByLogin(login);
        if(securityDetails.isPresent()){
            securityDetails.get().setCurrentPassword(passwordEntity);
        } else {
            SecurityAccountDetailsEntity securityData = SecurityAccountDetailsEntity.builder()
                    .accountEnabled(true)
                    .accountExpired(false)
                    .accountLocked(false)
                    .login(login)
                    .currentPassword(password)
                    .build();
            securityRepository.save(securityData);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean verifyFor(Login login, Password typedPassword) {
        Validate.notNull(login, "Login cannot be null");
        Validate.notNull(typedPassword, "Password cannot be null");

        Optional<SecurityAccountDetailsEntity> securityDetails = securityRepository.findByLogin(login);

        if(!securityDetails.isPresent()){
            return false;
        }

        PasswordEntity currentPassword = securityDetails.get().getCurrentPassword();
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
