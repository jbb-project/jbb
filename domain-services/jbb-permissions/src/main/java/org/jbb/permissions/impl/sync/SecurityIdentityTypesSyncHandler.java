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

import org.jbb.permissions.api.identity.SecurityIdentity;
import org.jbb.permissions.impl.acl.dao.AclSecurityIdentityTypeRepository;
import org.jbb.permissions.impl.acl.model.AclSecurityIdentityTypeEntity;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import lombok.RequiredArgsConstructor;

@Order(1)
@Component
@RequiredArgsConstructor
public class SecurityIdentityTypesSyncHandler implements SyncHandler {

    private final AclSecurityIdentityTypeRepository repository;

    @Override
    public void synchronize() {
        Arrays.stream(SecurityIdentity.Type.values()).forEach(this::saveIdentityType);
    }

    private void saveIdentityType(SecurityIdentity.Type type) {
        AclSecurityIdentityTypeEntity identityType = repository.findAllByName(type.name());
        if (identityType == null) {
            identityType = AclSecurityIdentityTypeEntity.builder()
                    .name(type.name())
                    .build();
            repository.save(identityType);
        }
    }
}
