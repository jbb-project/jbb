/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.impl.acl;

import org.jbb.permissions.api.PermissionMatrixService;
import org.jbb.permissions.api.matrix.PermissionMatrix;
import org.jbb.permissions.api.matrix.PermissionTable;
import org.jbb.permissions.impl.acl.model.AclSecurityIdentityEntity;
import org.jbb.permissions.impl.role.dao.AclActiveRoleRepository;
import org.jbb.permissions.impl.role.model.AclActiveRoleEntity;
import org.jbb.permissions.impl.role.model.AclRoleEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MatrixRepairManager {

    private final AclActiveRoleRepository activeRoleRepository;

    private final PermissionMatrixService permissionMatrixService;

    private final PermissionTypeTranslator permissionTypeTranslator;
    private final SecurityIdentityTranslator identityTranslator;

    public void fixMatrixes(AclRoleEntity roleToRemove, PermissionTable permissionTable) {
        activeRoleRepository.findByRole(roleToRemove)
                .forEach(activeRole -> reassignToPermissionTable(activeRole, permissionTable));
    }

    private void reassignToPermissionTable(AclActiveRoleEntity activeRole, PermissionTable permissionTable) {
        AclSecurityIdentityEntity identity = activeRole.getSecurityIdentity();

        PermissionMatrix permissionMatrix = permissionMatrixService.getPermissionMatrix(
                permissionTypeTranslator.toApiModel(activeRole.getRole().getPermissionType()),
                identityTranslator.toApiModel(identity)
        );

        permissionMatrix.setAssignedRole(Optional.empty());
        permissionMatrix.setPermissionTable(Optional.of(permissionTable));
        permissionMatrixService.setPermissionMatrix(permissionMatrix);
    }
}
