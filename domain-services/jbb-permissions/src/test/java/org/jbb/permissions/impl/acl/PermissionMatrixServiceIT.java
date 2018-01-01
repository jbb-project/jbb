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
import org.jbb.permissions.api.PermissionRoleService;
import org.jbb.permissions.api.identity.AdministratorGroupIdentity;
import org.jbb.permissions.api.matrix.PermissionMatrix;
import org.jbb.permissions.api.permission.PermissionType;
import org.jbb.permissions.api.role.PermissionRoleDefinition;
import org.jbb.permissions.impl.BaseIT;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jbb.permissions.api.role.PredefinedRole.JUNIOR_ADMINISTRATOR;
import static org.jbb.permissions.api.role.PredefinedRole.STANDARD_ADMINISTRATOR;

public class PermissionMatrixServiceIT extends BaseIT {

    @Autowired
    PermissionMatrixService permissionMatrixService;

    @Autowired
    PermissionRoleService permissionRoleService;

    @Test//TODO - in progress
    public void name() {
        PermissionRoleDefinition standardAdministrator = permissionRoleService.getRoleDefinition(STANDARD_ADMINISTRATOR);
        PermissionRoleDefinition juniorAdministrator = permissionRoleService.getRoleDefinition(JUNIOR_ADMINISTRATOR);

        PermissionMatrix permissionMatrix = permissionMatrixService
                .getPermissionMatrix(PermissionType.ADMINISTRATOR_PERMISSIONS,
                        AdministratorGroupIdentity.getInstance());
        permissionMatrix.setAssignedRole(Optional.of(juniorAdministrator));
        permissionMatrixService.setPermissionMatrix(permissionMatrix);
        PermissionMatrix updatedPermissionMatrix = permissionMatrixService
                .getPermissionMatrix(PermissionType.ADMINISTRATOR_PERMISSIONS,
                        AdministratorGroupIdentity.getInstance());
        assertThat(updatedPermissionMatrix.getAssignedRole().get().getName())
                .isEqualTo("Junior administrator");
        // rollback
        permissionMatrix.setAssignedRole(Optional.of(standardAdministrator));
        permissionMatrixService.setPermissionMatrix(permissionMatrix);
    }
}