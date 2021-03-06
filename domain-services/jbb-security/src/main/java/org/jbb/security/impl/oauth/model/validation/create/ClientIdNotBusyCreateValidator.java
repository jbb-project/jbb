/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.oauth.model.validation.create;

import org.jbb.security.impl.oauth.dao.OAuthClientRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClientIdNotBusyCreateValidator implements
        ConstraintValidator<ClientIdNotBusyCreate, String> {

    private final OAuthClientRepository clientRepository;

    @Override
    public void initialize(ClientIdNotBusyCreate clientIdNotBusyCreate) {
        // not needed...
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isValid(String clientId,
                           ConstraintValidatorContext constraintValidatorContext) {
        return !clientRepository.findByClientId(clientId).isPresent();
    }

}
