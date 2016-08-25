/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.services;

import org.jbb.lib.core.vo.Password;
import org.jbb.members.api.exceptions.RegistrationException;
import org.jbb.members.api.model.RegistrationRequest;
import org.jbb.security.api.services.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PasswordSaver {
    private PasswordService passwordService;

    @Autowired
    public PasswordSaver(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    @Transactional
    public void save(RegistrationRequest regRequest) {
        Password password = regRequest.getPassword();
        Password passwordAgain = regRequest.getPasswordAgain();

        if (!password.equals(passwordAgain)) {
            throw new RegistrationException(null);//TODO
        }

        passwordService.changeFor(regRequest.getLogin(), regRequest.getPassword());
    }

}
