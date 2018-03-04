/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.web.base.logic;

import org.jbb.permissions.api.PermissionRoleService;
import org.jbb.permissions.api.matrix.PermissionMatrix;
import org.jbb.permissions.api.matrix.PermissionTable;
import org.jbb.permissions.api.permission.PermissionType;
import org.jbb.permissions.api.role.PermissionRoleDefinition;
import org.jbb.permissions.web.base.form.PermissionMatrixForm;
import org.jbb.permissions.web.base.model.MatrixMode;
import org.springframework.stereotype.Component;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PermissionMatrixMapper {

    private final PermissionRoleService permissionRoleService;

    private final SecurityIdentityMapper securityIdentityMapper;
    private final PermissionTableMapper tableMapper;

    public PermissionMatrix toModel(PermissionMatrixForm form, PermissionType permissionType) {
        PermissionMatrix matrix = PermissionMatrix.builder()
                .permissionType(permissionType)
                .securityIdentity(securityIdentityMapper.toModel(form.getSecurityIdentity())
                        .orElseThrow(IllegalStateException::new))
                .build();

        MatrixMode matrixMode = form.getMatrixMode();
        if (matrixMode == MatrixMode.ASSIGNED_ROLE) {
            PermissionRoleDefinition roleDefinition = permissionRoleService.getRoleDefinition(form.getRoleId());
            matrix.setAssignedRole(Optional.of(roleDefinition));
        } else if (matrixMode == MatrixMode.PERMISSION_TABLE) {
            matrix.setPermissionTable(Optional.of(tableMapper.toModel(form.getValueMap())));
        } else {
            throw new IllegalStateException();
        }

        return matrix;
    }

    public PermissionTable getUsedPermissionTable(PermissionMatrix matrix) {
        Optional<PermissionRoleDefinition> assignedRole = matrix.getAssignedRole();
        Optional<PermissionTable> permissionTable = matrix.getPermissionTable();

        if (assignedRole.isPresent()) {
            return permissionRoleService.getPermissionTable(assignedRole.get().getId());
        } else if (permissionTable.isPresent()) {
            return permissionTable.get();
        }

        throw new IllegalStateException();
    }
}
