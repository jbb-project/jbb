/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.role;

import com.google.common.eventbus.Subscribe;

import org.apache.commons.lang3.Validate;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.lib.eventbus.JbbEventBusListener;
import org.jbb.members.event.MemberRemovedEvent;
import org.jbb.security.api.role.RoleService;
import org.jbb.security.event.AdministratorRoleAddedEvent;
import org.jbb.security.event.AdministratorRoleRemovedEvent;
import org.jbb.security.impl.role.dao.AdministratorRepository;
import org.jbb.security.impl.role.model.AdministratorEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import javax.cache.annotation.CacheRemove;
import javax.cache.annotation.CacheResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultRoleService implements RoleService, JbbEventBusListener {
    private final AdministratorRepository adminRepository;
    private final AdministratorEntityFactory adminFactory;
    private final JbbEventBus eventBus;

    @Override
    @Transactional(readOnly = true)
    @CacheResult(cacheName = RoleCaches.ADMINISTRATOR_ROLE)
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
    @CacheRemove(cacheName = RoleCaches.ADMINISTRATOR_ROLE)
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
    @CacheRemove(cacheName = RoleCaches.ADMINISTRATOR_ROLE)
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
