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

import org.jbb.permissions.api.permission.PermissionType;
import org.jbb.permissions.impl.acl.dao.AclPermissionTypeRepository;
import org.jbb.permissions.impl.acl.model.AclPermissionTypeEntity;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import lombok.RequiredArgsConstructor;

@Order(2)
@Component
@RequiredArgsConstructor
public class PermissionTypesSyncHandler implements SyncHandler {

    private final AclPermissionTypeRepository repository;

    @Override
    public void synchronize() {
        Arrays.stream(PermissionType.values()).forEach(this::savePermissionType);
    }

    private void savePermissionType(PermissionType type) {
        AclPermissionTypeEntity permissionType = repository.findAllByName(type.name());
        if (permissionType == null) {
            permissionType = AclPermissionTypeEntity.builder()
                    .name(type.name())
                    .build();
            repository.save(permissionType);
        }
    }
}
