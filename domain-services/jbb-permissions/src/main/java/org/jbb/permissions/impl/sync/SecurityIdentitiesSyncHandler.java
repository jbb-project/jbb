/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.impl.sync;

import org.jbb.permissions.api.identity.AdministratorGroupIdentity;
import org.jbb.permissions.api.identity.AnonymousIdentity;
import org.jbb.permissions.api.identity.RegisteredMembersIdentity;
import org.jbb.permissions.api.identity.SecurityIdentity;
import org.jbb.permissions.impl.acl.dao.AclSecurityIdentityRepository;
import org.jbb.permissions.impl.acl.dao.AclSecurityIdentityTypeRepository;
import org.jbb.permissions.impl.acl.model.AclSecurityIdentityEntity;
import org.jbb.permissions.impl.acl.model.AclSecurityIdentityTypeEntity;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

@Order(5)
@Component
@RequiredArgsConstructor
public class SecurityIdentitiesSyncHandler implements SyncHandler {

    private final AclSecurityIdentityTypeRepository typeRepository;
    private final AclSecurityIdentityRepository identityRepository;


    @Override
    public void synchronize() {
        createIdentity(AnonymousIdentity.getInstance());
        createIdentity(AdministratorGroupIdentity.getInstance());
        createIdentity(RegisteredMembersIdentity.getInstance());
    }

    private void createIdentity(SecurityIdentity securityIdentity) {
        AclSecurityIdentityTypeEntity typeEntity = typeRepository.findAllByName(securityIdentity.getType().name());
        Optional<AclSecurityIdentityEntity> identity = identityRepository.findByTypeAndPrimarySid(typeEntity, securityIdentity.getId());
        if (!identity.isPresent()) {
            AclSecurityIdentityEntity identityEntity = AclSecurityIdentityEntity.builder()
                    .type(typeEntity)
                    .primarySid(securityIdentity.getId())
                    .build();
            identityRepository.save(identityEntity);
        }
    }

}
