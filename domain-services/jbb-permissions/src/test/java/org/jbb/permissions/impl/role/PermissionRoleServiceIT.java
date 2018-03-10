/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.impl.role;

import org.jbb.lib.commons.security.UserDetailsSource;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.permissions.api.PermissionRoleService;
import org.jbb.permissions.api.matrix.PermissionTable;
import org.jbb.permissions.api.permission.PermissionType;
import org.jbb.permissions.api.role.PermissionRoleDefinition;
import org.jbb.permissions.api.role.PredefinedRole;
import org.jbb.permissions.impl.BaseIT;
import org.jbb.permissions.impl.role.dao.AclRoleRepository;
import org.jbb.security.api.role.RoleService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class PermissionRoleServiceIT extends BaseIT {

    @Autowired
    PermissionRoleService permissionRoleService;

    @Autowired
    UserDetailsSource userDetailsSourceMock;

    @Autowired
    RoleService roleServiceMock;

    @Autowired
    JbbEventBus eventBus;

    @Autowired
    AclRoleRepository roleRepository;

    @Test
    public void shouldContains_defaultMemberRoles() {
        // when
        List<PermissionRoleDefinition> roles = permissionRoleService.getRoleDefinitions(PermissionType.MEMBER_PERMISSIONS);

        // then
        assertThat(roles).allSatisfy(
                role -> role.getPermissionType().equals(PermissionType.MEMBER_PERMISSIONS)
        );
    }

    @Test
    public void shouldContains_defaultAdministratorRoles() {
        // when
        List<PermissionRoleDefinition> roles = permissionRoleService.getRoleDefinitions(PermissionType.ADMINISTRATOR_PERMISSIONS);

        // then
        assertThat(roles).allSatisfy(
                role -> role.getPermissionType().equals(PermissionType.ADMINISTRATOR_PERMISSIONS)
        );
    }

    @Test
    public void shouldContains_predefinedMemberRoles() {
        // when
        List<PermissionRoleDefinition> roles = permissionRoleService.getPredefinedRoles(PermissionType.MEMBER_PERMISSIONS);

        // then
        assertThat(roles).allSatisfy(
                role -> role.getPermissionType().equals(PermissionType.MEMBER_PERMISSIONS)
        );
        assertThat(roles).allSatisfy(
                role -> role.getPredefinedRole().isPresent()
        );
    }

    @Test
    public void shouldContains_predefinedAdministratorRoles() {
        // when
        List<PermissionRoleDefinition> roles = permissionRoleService.getPredefinedRoles(PermissionType.ADMINISTRATOR_PERMISSIONS);

        // then
        assertThat(roles).allSatisfy(
                role -> role.getPermissionType().equals(PermissionType.ADMINISTRATOR_PERMISSIONS)
        );
        assertThat(roles).allSatisfy(
                role -> role.getPredefinedRole().isPresent()
        );
    }

    @Test
    public void getPredefinedRoleDefinition() {
        // when
        PermissionRoleDefinition juniorAdminDef = permissionRoleService.getRoleDefinition(PredefinedRole.JUNIOR_ADMINISTRATOR);

        // then
        assertThat(juniorAdminDef.getPredefinedRole().get()).isEqualTo(PredefinedRole.JUNIOR_ADMINISTRATOR);
    }

    @Test
    public void getPredefinedRolePermissionTable() {
        // when
        PermissionTable permissionTable = permissionRoleService.getPermissionTable(PredefinedRole.JUNIOR_ADMINISTRATOR);

        // then
        assertThat(permissionTable.getPermissions()).isNotEmpty();
    }

    @Test
    public void CRUD_test_for_roles() {
        // given
        String roleName = "New member role";
        String updatedRoleName = "Updated member role";

        PermissionRoleDefinition roleDefinition = permissionRoleService.getRoleDefinition(PredefinedRole.STANDARD_MEMBER);
        PermissionTable permissionTable = permissionRoleService.getPermissionTable(PredefinedRole.STANDARD_MEMBER);

        roleDefinition.setName(roleName);
        roleDefinition.setPredefinedRole(Optional.empty());
        roleDefinition.setSourcePredefinedRole(PredefinedRole.STANDARD_MEMBER);

        // when
        PermissionRoleDefinition newRoleDef = permissionRoleService.addRole(roleDefinition, permissionTable);

        // then
        assertThat(newRoleDef.getName()).isEqualTo(roleName);

        // when
        PermissionRoleDefinition resultDefinition = permissionRoleService.getRoleDefinition(newRoleDef.getId());

        // then
        assertThat(resultDefinition.getName()).isEqualTo(roleName);

        // when
        PermissionTable permTable = permissionRoleService.getPermissionTable(newRoleDef.getId());

        // then
        assertThat(permTable.getPermissions()).isNotEmpty();

        // when
        newRoleDef.setName(updatedRoleName);
        permissionRoleService.updatePermissionTable(newRoleDef.getId(), permTable);
        newRoleDef = permissionRoleService.updateRoleDefinition(newRoleDef);

        // then
        assertThat(newRoleDef.getName()).isEqualTo(updatedRoleName);

        // when
        newRoleDef = permissionRoleService.moveRoleToPosition(newRoleDef.getId(), 1);

        // then
        assertThat(newRoleDef.getPosition()).isEqualTo(1);

        // when
        permissionRoleService.removeRole(newRoleDef.getId());

        // then
        assertThat(roleRepository.findOne(newRoleDef.getId())).isNull();
    }

}