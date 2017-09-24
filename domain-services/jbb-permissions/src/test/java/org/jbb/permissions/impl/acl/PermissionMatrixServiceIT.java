/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.impl.acl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.jbb.permissions.api.PermissionMatrixService;
import org.jbb.permissions.api.PermissionRoleService;
import org.jbb.permissions.api.identity.AdministratorGroupIdentity;
import org.jbb.permissions.api.matrix.PermissionMatrix;
import org.jbb.permissions.api.permission.PermissionType;
import org.jbb.permissions.api.role.PermissionRoleDefinition;
import org.jbb.permissions.impl.BaseIT;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PermissionMatrixServiceIT extends BaseIT {

    @Autowired
    PermissionMatrixService permissionMatrixService;

    @Autowired
    PermissionRoleService permissionRoleService;

    @Test//TODO - in progress
    public void name() throws Exception {
        List<PermissionRoleDefinition> administratorRoles = permissionRoleService
            .getRoleDefinitions(PermissionType.ADMINISTRATOR_PERMISSIONS);
        PermissionMatrix permissionMatrix = permissionMatrixService
            .getPermissionMatrix(PermissionType.ADMINISTRATOR_PERMISSIONS,
                AdministratorGroupIdentity.getInstance());
        permissionMatrix.setAssignedRole(Optional.of(administratorRoles.get(1)));
        permissionMatrixService.setPermissionMatrix(permissionMatrix);
        PermissionMatrix updatedPermissionMatrix = permissionMatrixService
            .getPermissionMatrix(PermissionType.ADMINISTRATOR_PERMISSIONS,
                AdministratorGroupIdentity.getInstance());
        assertThat(updatedPermissionMatrix.getAssignedRole().get().getName())
            .isEqualTo("Junior administrator");
    }
}