/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.impl.registration.logic;

import org.jbb.members.api.data.RegistrationRequest;
import org.jbb.members.impl.registration.data.PasswordPair;
import org.jbb.security.api.exception.PasswordException;
import org.jbb.security.api.service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

@Component
public class PasswordSaver {
    private final PasswordService passwordService;
    private final Validator validator;

    @Autowired
    public PasswordSaver(PasswordService passwordService, Validator validator) {
        this.passwordService = passwordService;
        this.validator = validator;
    }

    @Transactional
    public void save(RegistrationRequest regRequest) {
        Set<ConstraintViolation<PasswordPair>> validationResult = validator.validate(
                new PasswordPair(regRequest.getPassword(), regRequest.getPasswordAgain()));
        if (!validationResult.isEmpty()) {
            throw new PasswordException(validationResult);
        }

        passwordService.changeFor(regRequest.getUsername(), regRequest.getPassword());
    }

}
