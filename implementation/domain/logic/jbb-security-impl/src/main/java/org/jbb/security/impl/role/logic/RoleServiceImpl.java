/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.role.logic;

import org.apache.commons.lang3.Validate;
import org.jbb.lib.core.vo.Username;
import org.jbb.security.api.service.RoleService;
import org.jbb.security.impl.role.dao.AdministratorRepository;
import org.jbb.security.impl.role.model.AdministratorEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {
    private final AdministratorRepository adminRepository;
    private final AdministratorEntityFactory adminFactory;

    @Autowired
    public RoleServiceImpl(AdministratorRepository adminRepository,
                           AdministratorEntityFactory adminFactory) {
        this.adminRepository = adminRepository;
        this.adminFactory = adminFactory;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasAdministratorRole(Username username) {
        Validate.notNull(username);
        return adminRepository.findByUsername(username).isPresent();
    }

    @Override
    @Transactional
    public void addAdministratorRole(Username username) {
        Validate.notNull(username);
        if (!hasAdministratorRole(username)) {
            AdministratorEntity administratorEntity = adminFactory.create(username);
            adminRepository.save(administratorEntity);
        }
    }

    @Override
    @Transactional
    public boolean removeAdministratorRole(Username username) {
        Validate.notNull(username);
        Optional<AdministratorEntity> administratorEntityOptional = adminRepository.findByUsername(username);
        if (administratorEntityOptional.isPresent()) {
            adminRepository.delete(administratorEntityOptional.get());
            return true;
        } else {
            return false;
        }
    }
}
