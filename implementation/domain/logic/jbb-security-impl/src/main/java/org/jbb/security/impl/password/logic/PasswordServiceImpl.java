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
import org.jbb.lib.core.vo.Password;
import org.jbb.lib.core.vo.Username;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.security.api.data.PasswordRequirements;
import org.jbb.security.api.exception.PasswordException;
import org.jbb.security.api.service.PasswordService;
import org.jbb.security.event.PasswordChangedEvent;
import org.jbb.security.impl.password.dao.PasswordRepository;
import org.jbb.security.impl.password.model.PasswordEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

@Service
public class PasswordServiceImpl implements PasswordService {
    private final PasswordRepository passwordRepository;
    private final PasswordEntityFactory passwordEntityFactory;
    private final PasswordEqualsPolicy passwordEqualsPolicy;
    private final PasswordRequirementsPolicy requirementsPolicy;
    private final Validator validator;
    private final JbbEventBus eventBus;

    @Autowired
    public PasswordServiceImpl(PasswordRepository passwordRepository,
                               PasswordEntityFactory passwordEntityFactory,
                               PasswordEqualsPolicy passwordEqualsPolicy,
                               PasswordRequirementsPolicy requirementsPolicy,
                               Validator validator, JbbEventBus eventBus) {
        this.passwordRepository = passwordRepository;
        this.passwordEntityFactory = passwordEntityFactory;
        this.passwordEqualsPolicy = passwordEqualsPolicy;
        this.requirementsPolicy = requirementsPolicy;
        this.validator = validator;
        this.eventBus = eventBus;
    }

    @Override
    @Transactional
    public void changeFor(Username username, Password newPassword) {
        Validate.notNull(username, "Username cannot be null");
        Validate.notNull(newPassword, "Password cannot be null");

        PasswordEntity passwordEntity = passwordEntityFactory.create(username, newPassword);

        Set<ConstraintViolation<PasswordEntity>> validateResult = validator.validate(passwordEntity);
        if (!validateResult.isEmpty()) {
            throw new PasswordException(validateResult);
        }

        passwordRepository.save(passwordEntity);

        publishEvent(passwordEntity);
    }

    private void publishEvent(PasswordEntity password) {
        eventBus.post(new PasswordChangedEvent(password.getUsername()));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean verifyFor(Username username, Password typedPassword) {
        Validate.notNull(username, "Username cannot be null");
        Validate.notNull(typedPassword, "Password cannot be null");

        Optional<PasswordEntity> currentPasswordEntity = passwordRepository.findTheNewestByUsername(username);
        if (currentPasswordEntity.isPresent()) {
            return passwordEqualsPolicy
                    .matches(typedPassword, currentPasswordEntity.get().getPasswordValueObject());
        }
        return false;
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
