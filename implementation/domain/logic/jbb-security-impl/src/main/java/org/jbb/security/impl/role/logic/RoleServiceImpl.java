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
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.security.api.service.RoleService;
import org.jbb.security.event.AdministratorRoleAddedEvent;
import org.jbb.security.event.AdministratorRoleRemovedEvent;
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
    private final JbbEventBus eventBus;

    @Autowired
    public RoleServiceImpl(AdministratorRepository adminRepository,
                           AdministratorEntityFactory adminFactory,
                           JbbEventBus eventBus) {
        this.adminRepository = adminRepository;
        this.adminFactory = adminFactory;
        this.eventBus = eventBus;
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
            eventBus.post(new AdministratorRoleAddedEvent(username));
        }
    }

    @Override
    @Transactional
    public boolean removeAdministratorRole(Username username) {
        Validate.notNull(username);
        Optional<AdministratorEntity> administratorEntityOptional = adminRepository.findByUsername(username);
        if (administratorEntityOptional.isPresent()) {
            adminRepository.delete(administratorEntityOptional.get());
            eventBus.post(new AdministratorRoleRemovedEvent(username));
            return true;
        } else {
            return false;
        }
    }
}
