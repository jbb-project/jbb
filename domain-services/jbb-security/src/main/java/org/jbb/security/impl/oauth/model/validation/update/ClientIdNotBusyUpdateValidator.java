/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.oauth.model.validation.update;

import org.jbb.security.impl.oauth.dao.OAuthClientRepository;
import org.jbb.security.impl.oauth.model.OAuthClientEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClientIdNotBusyUpdateValidator implements
        ConstraintValidator<ClientIdNotBusyUpdate, OAuthClientEntity> {

    private final OAuthClientRepository clientRepository;

    private String message;

    @Override
    public void initialize(ClientIdNotBusyUpdate clientIdNotBusyUpdate) {
        message = clientIdNotBusyUpdate.message();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isValid(OAuthClientEntity client, ConstraintValidatorContext context) {
        String clientId = client.getClientId();
        Optional<OAuthClientEntity> foundClient = clientRepository.findByClientId(clientId);
        if (foundClient.isPresent()) {
            boolean result = foundClient.get().getId().equals(client.getId());
            if (!result) {
                context.disableDefaultConstraintViolation();
                context
                        .buildConstraintViolationWithTemplate(message)
                        .addPropertyNode("clientId").addConstraintViolation();
                return false;
            }
        }
        return true;
    }

}
