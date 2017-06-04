/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.role.logic;

import com.google.common.eventbus.Subscribe;

import org.apache.commons.lang3.Validate;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.members.event.MemberRemovedEvent;
import org.jbb.security.api.role.RoleService;
import org.jbb.security.event.AdministratorRoleAddedEvent;
import org.jbb.security.event.AdministratorRoleRemovedEvent;
import org.jbb.security.impl.role.dao.AdministratorRepository;
import org.jbb.security.impl.role.model.AdministratorEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
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
        this.eventBus.register(this);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasAdministratorRole(Long memberId) {
        Validate.notNull(memberId);
        return adminRepository.findByMemberId(memberId).isPresent();
    }

    @Subscribe
    @Transactional
    public void removeAdministratorEntity(MemberRemovedEvent event) {
        log.debug("Remove administrator entity for member id {} (if applicable)", event.getMemberId());
        adminRepository.findByMemberId(event.getMemberId())
                .ifPresent(adminRepository::delete);
    }

    @Override
    @Transactional
    public void addAdministratorRole(Long memberId) {
        Validate.notNull(memberId);
        if (!hasAdministratorRole(memberId)) {
            AdministratorEntity administratorEntity = adminFactory.create(memberId);
            adminRepository.save(administratorEntity);
            eventBus.post(new AdministratorRoleAddedEvent(memberId));
        }
    }

    @Override
    @Transactional
    public boolean removeAdministratorRole(Long memberId) {
        Validate.notNull(memberId);
        Optional<AdministratorEntity> administratorEntityOptional = adminRepository.findByMemberId(memberId);
        if (administratorEntityOptional.isPresent()) {
            adminRepository.delete(administratorEntityOptional.get());
            eventBus.post(new AdministratorRoleRemovedEvent(memberId));
            return true;
        } else {
            return false;
        }
    }
}
