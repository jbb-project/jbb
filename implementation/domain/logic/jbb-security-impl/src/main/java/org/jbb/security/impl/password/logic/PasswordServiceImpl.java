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

import org.apache.commons.lang3.Validate;
import org.jbb.lib.core.vo.Login;
import org.jbb.lib.core.vo.Password;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.security.api.data.PasswordRequirements;
import org.jbb.security.api.exception.PasswordException;
import org.jbb.security.api.service.PasswordService;
import org.jbb.security.event.PasswordChangedEvent;
import org.jbb.security.impl.password.dao.PasswordRepository;
import org.jbb.security.impl.password.model.PasswordEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

@Service
public class PasswordServiceImpl implements PasswordService {
    private final PasswordEncoder passwordEncoder;
    private final PasswordRepository passwordRepository;
    private final PasswordRequirementsPolicy requirementsPolicy;
    private final Validator validator;
    private final JbbEventBus eventBus;

    @Autowired
    public PasswordServiceImpl(PasswordEncoder passwordEncoder,
                               PasswordRepository passwordRepository,
                               PasswordRequirementsPolicy requirementsPolicy,
                               Validator validator, JbbEventBus eventBus) {
        this.passwordEncoder = passwordEncoder;
        this.passwordRepository = passwordRepository;
        this.requirementsPolicy = requirementsPolicy;
        this.validator = validator;
        this.eventBus = eventBus;
    }

    @Override
    @Transactional
    public void changeFor(Login login, Password newPassword) {
        Validate.notNull(login, "Login cannot be null");
        Validate.notNull(newPassword, "Password cannot be null");

        String newPasswordStr = String.valueOf(newPassword.getValue());

        PasswordEntity password = PasswordEntity.builder()
                .login(login)
                .password(passwordEncoder.encode(newPasswordStr))
                .applicableSince(LocalDateTime.now())
                .visiblePassword(newPasswordStr)
                .build();

        Set<ConstraintViolation<PasswordEntity>> validateResult = validator.validate(password);
        if (!validateResult.isEmpty()) {
            throw new PasswordException(validateResult);
        }

        passwordRepository.save(password);

        publishEvent(password);
    }

    private void publishEvent(PasswordEntity password) {
        eventBus.post(new PasswordChangedEvent(password.getLogin()));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean verifyFor(Login login, Password typedPassword) {
        Validate.notNull(login, "Login cannot be null");
        Validate.notNull(typedPassword, "Password cannot be null");

        Optional<PasswordEntity> currentPassword = passwordRepository.findTheNewestByLogin(login);
        if (!currentPassword.isPresent()) {
            return false;
        }

        String decodedTypedPassword = String.copyValueOf(typedPassword.getValue());
        String encodedCurrentPassword = currentPassword.get().getPassword();

        return passwordEncoder.matches(decodedTypedPassword, encodedCurrentPassword);
    }

    @Override
    public PasswordRequirements currentRequirements() {
        return requirementsPolicy.currentRequirements();
    }

    @Override
    public void updateRequirements(PasswordRequirements requirements) {
        Validate.notNull(requirements);
        requirementsPolicy.update(requirements);
    }

}
