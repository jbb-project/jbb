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

import org.jbb.permissions.api.permission.PermissionCategory;
import org.jbb.permissions.api.permission.PermissionDefinition;
import org.jbb.permissions.api.permission.domain.AdministratorPermissions;
import org.jbb.permissions.api.permission.domain.MemberPermissions;
import org.jbb.permissions.impl.acl.dao.AclPermissionCategoryRepository;
import org.jbb.permissions.impl.acl.dao.AclPermissionRepository;
import org.jbb.permissions.impl.acl.dao.AclPermissionTypeRepository;
import org.jbb.permissions.impl.acl.model.AclPermissionCategoryEntity;
import org.jbb.permissions.impl.acl.model.AclPermissionEntity;
import org.jbb.permissions.impl.acl.model.AclPermissionTypeEntity;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import lombok.RequiredArgsConstructor;

@Order(4)
@Component
@RequiredArgsConstructor
public class PermissionDefinitionsSyncHandler implements SyncHandler {

    private final AclPermissionTypeRepository typeRepository;
    private final AclPermissionCategoryRepository categoryRepository;
    private final AclPermissionRepository permissionRepository;

    @Override
    public void synchronize() {
        Arrays.stream(AdministratorPermissions.values()).forEach(this::savePermission);
        Arrays.stream(MemberPermissions.values()).forEach(this::savePermission);
    }

    private void savePermission(PermissionDefinition permissionDefinition) {
        PermissionCategory category = permissionDefinition.getCategory();
        AclPermissionTypeEntity typeEntity = typeRepository.findAllByName(category.getType().name());
        AclPermissionCategoryEntity categoryEntity = categoryRepository
                .findAllByNameAndType(category.getName(), typeEntity);

        AclPermissionEntity permission = permissionRepository.findAllByCode(permissionDefinition.getCode());

        if (permission != null) {
            permission.setName(permissionDefinition.getName());
            permission.setCategory(categoryEntity);
            permission.setPosition(permissionDefinition.getPosition());
        } else {
            permission = AclPermissionEntity.builder()
                    .name(permissionDefinition.getName())
                    .code(permissionDefinition.getCode())
                    .category(categoryEntity)
                    .position(permissionDefinition.getPosition())
                    .build();
        }
        permissionRepository.save(permission);
    }

}
