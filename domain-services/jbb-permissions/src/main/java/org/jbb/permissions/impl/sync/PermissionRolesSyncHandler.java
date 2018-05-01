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

import org.jbb.permissions.api.PermissionRoleService;
import org.jbb.permissions.api.matrix.PermissionTable;
import org.jbb.permissions.api.permission.Permission;
import org.jbb.permissions.impl.acl.dao.AclPermissionRepository;
import org.jbb.permissions.impl.acl.model.AclPermissionEntity;
import org.jbb.permissions.impl.acl.model.AclPermissionTypeEntity;
import org.jbb.permissions.impl.role.dao.AclRoleRepository;
import org.jbb.permissions.impl.role.model.AclRoleEntity;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

import lombok.RequiredArgsConstructor;

@Order(7)
@Component
@RequiredArgsConstructor
public class PermissionRolesSyncHandler implements SyncHandler {

    private final PermissionRoleService permissionRoleService;
    private final MissingPermissionResolver missingPermissionResolver;

    private final AclRoleRepository roleRepository;
    private final AclPermissionRepository permissionRepository;


    @Override
    public void synchronize() {
        roleRepository.findAll().forEach(this::checkForMissingPermissions);
    }

    private void checkForMissingPermissions(AclRoleEntity role) {
        AclPermissionTypeEntity typeEntity = role.getPermissionType();

        PermissionTable permissionTable = permissionRoleService.getPermissionTable(role.getId());
        Set<Permission> permissions = permissionTable.getPermissions();
        int oldTableSize = permissions.size();

        List<AclPermissionEntity> targetPermissions = permissionRepository.findAllByPermissionType(typeEntity);
        targetPermissions.forEach(permission -> checkIfMissing(permission, permissionTable, role));
        int newTableSize = permissions.size();

        if (oldTableSize != newTableSize) {
            permissionRoleService.updatePermissionTable(role.getId(), permissionTable);
        }

    }

    private void checkIfMissing(AclPermissionEntity targetPermission, PermissionTable permissionTable,
                                AclRoleEntity role) {
        Set<Permission> permissions = permissionTable.getPermissions();
        boolean permissionIsMissing = permissions.stream()
                .noneMatch(p -> p.getDefinition().getCode().equals(targetPermission.getCode()));

        if (permissionIsMissing) {
            Permission newPermission = missingPermissionResolver.resolve(role, targetPermission);
            permissions.add(newPermission);
        }

    }

}
