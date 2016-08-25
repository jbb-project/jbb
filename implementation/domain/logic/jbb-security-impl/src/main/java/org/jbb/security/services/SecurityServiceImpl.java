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
import org.jbb.security.api.model.SecurityAccountDetails;
import org.jbb.security.api.services.SecurityService;
import org.jbb.security.dao.SecurityAccountDetailsRepository;
import org.jbb.security.entities.SecurityAccountDetailsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service //TODO
public class SecurityServiceImpl implements SecurityService {
    private final SecurityAccountDetailsRepository repository;

    @Autowired
    public SecurityServiceImpl(SecurityAccountDetailsRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public SecurityAccountDetails securityAccountDetailsFor(Login login) {
        Validate.notNull(login);
        return repository.findByLogin(login).get();
    }

    @Override
    @Transactional
    public void updateSecurityAccountDetailsFor(Login login, SecurityAccountDetails newSecurityDetails) {
        Validate.notNull(login);
        Validate.notNull(newSecurityDetails);
        SecurityAccountDetailsEntity securityDetails = repository.findByLogin(login).get();
        securityDetails.setAccountEnabled(newSecurityDetails.isAccountEnabled());
        securityDetails.setAccountExpired(newSecurityDetails.isAccountExpired());
        securityDetails.setAccountLocked(newSecurityDetails.isAccountLocked());
    }
}
