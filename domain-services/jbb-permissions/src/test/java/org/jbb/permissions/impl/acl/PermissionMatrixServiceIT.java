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
import org.jbb.permissions.api.identity.RegisteredMembersIdentity;
import org.jbb.permissions.api.matrix.PermissionMatrix;
import org.jbb.permissions.api.matrix.PermissionTable;
import org.jbb.permissions.api.permission.PermissionType;
import org.jbb.permissions.api.role.PermissionRoleDefinition;
import org.jbb.permissions.api.role.PredefinedRole;
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

    @Test
    public void assignmentToRoleShouldWork() {
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

    @Test
    public void settingNewMatrixWithAssingedRole_andThenRemoveRole_shouldFallbackToPermissionTable() {
        // given
        PermissionRoleDefinition newRoleDef = addAndGetNewRole();
        PermissionMatrix permissionMatrix = permissionMatrixService.getPermissionMatrix(PermissionType.MEMBER_PERMISSIONS, RegisteredMembersIdentity.getInstance());

        // when
        permissionMatrix.setPermissionTable(Optional.empty());
        permissionMatrix.setAssignedRole(Optional.of(newRoleDef));
        permissionMatrixService.setPermissionMatrix(permissionMatrix);

        // then
        permissionMatrix = permissionMatrixService.getPermissionMatrix(PermissionType.MEMBER_PERMISSIONS, RegisteredMembersIdentity.getInstance());
        assertThat(permissionMatrix.getAssignedRole()).isPresent();
        assertThat(permissionMatrix.getPermissionTable()).isNotPresent();

        // when
        permissionRoleService.removeRole(newRoleDef.getId());

        // then
        permissionMatrix = permissionMatrixService.getPermissionMatrix(PermissionType.MEMBER_PERMISSIONS, RegisteredMembersIdentity.getInstance());
        assertThat(permissionMatrix.getAssignedRole()).isNotPresent();
        assertThat(permissionMatrix.getPermissionTable()).isPresent();
    }

    private PermissionRoleDefinition addAndGetNewRole() {
        String roleName = "New member role";
        PermissionRoleDefinition roleDefinition = permissionRoleService.getRoleDefinition(PredefinedRole.STANDARD_MEMBER);
        PermissionTable permissionTable = permissionRoleService.getPermissionTable(PredefinedRole.STANDARD_MEMBER);

        roleDefinition.setName(roleName);
        roleDefinition.setPredefinedRole(Optional.empty());
        roleDefinition.setPosition(2);
        roleDefinition.setSourcePredefinedRole(PredefinedRole.STANDARD_MEMBER);

        return permissionRoleService.addRole(roleDefinition, permissionTable);
    }
}