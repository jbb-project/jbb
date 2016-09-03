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
import org.jbb.security.api.services.RoleService;
import org.jbb.security.dao.AdministratorRepository;
import org.jbb.security.entities.AdministratorEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoleServiceImpl implements RoleService {
    private final AdministratorRepository adminRepository;

    @Autowired
    public RoleServiceImpl(AdministratorRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasAdministratorRole(Login login) {
        Validate.notNull(login);
        return adminRepository.findByLogin(login).isPresent();
    }

    @Override
    @Transactional
    public void addAdministratorRole(Login login) {
        Validate.notNull(login);
        if (!hasAdministratorRole(login)) {
            AdministratorEntity administratorEntity = AdministratorEntity
                    .builder()
                    .login(login)
                    .build();
            adminRepository.save(administratorEntity);
        }
    }
}