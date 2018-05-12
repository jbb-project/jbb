/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.privilege;

import com.google.common.eventbus.Subscribe;
import java.util.Optional;
import javax.cache.annotation.CacheRemove;
import javax.cache.annotation.CacheResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.lib.eventbus.JbbEventBusListener;
import org.jbb.members.event.MemberRemovedEvent;
import org.jbb.security.api.privilege.PrivilegeService;
import org.jbb.security.event.AdministratorPrivilegeAddedEvent;
import org.jbb.security.event.AdministratorPrivilegeRemovedEvent;
import org.jbb.security.impl.privilege.dao.AdministratorRepository;
import org.jbb.security.impl.privilege.model.AdministratorEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultPrivilegeService implements PrivilegeService, JbbEventBusListener {
    private final AdministratorRepository adminRepository;
    private final AdministratorEntityFactory adminFactory;
    private final JbbEventBus eventBus;

    @Override
    @Transactional(readOnly = true)
    @CacheResult(cacheName = PrivilegeCaches.ADMINISTRATOR_ROLE)
    public boolean hasAdministratorPrivilege(Long memberId) {
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
    @CacheRemove(cacheName = PrivilegeCaches.ADMINISTRATOR_ROLE)
    public void addAdministratorPrivilege(Long memberId) {
        Validate.notNull(memberId);
        if (!hasAdministratorPrivilege(memberId)) {
            AdministratorEntity administratorEntity = adminFactory.create(memberId);
            adminRepository.save(administratorEntity);
            eventBus.post(new AdministratorPrivilegeAddedEvent(memberId));
        }
    }

    @Override
    @Transactional
    @CacheRemove(cacheName = PrivilegeCaches.ADMINISTRATOR_ROLE)
    public boolean removeAdministratorPrivilege(Long memberId) {
        Validate.notNull(memberId);
        Optional<AdministratorEntity> administratorEntityOptional = adminRepository.findByMemberId(memberId);
        if (administratorEntityOptional.isPresent()) {
            adminRepository.delete(administratorEntityOptional.get());
            eventBus.post(new AdministratorPrivilegeRemovedEvent(memberId));
            return true;
        } else {
            return false;
        }
    }
}
