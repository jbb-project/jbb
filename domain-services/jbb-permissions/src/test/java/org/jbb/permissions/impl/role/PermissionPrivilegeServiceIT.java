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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.jbb.lib.commons.security.UserDetailsSource;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.permissions.api.PermissionRoleService;
import org.jbb.permissions.api.matrix.PermissionTable;
import org.jbb.permissions.api.permission.PermissionType;
import org.jbb.permissions.api.role.PermissionRoleDefinition;
import org.jbb.permissions.api.role.PredefinedRole;
import org.jbb.permissions.impl.BaseIT;
import org.jbb.permissions.impl.role.dao.AclRoleRepository;
import org.jbb.security.api.privilege.PrivilegeService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PermissionPrivilegeServiceIT extends BaseIT {

    @Autowired
    PermissionRoleService permissionRoleService;

    @Autowired
    UserDetailsSource userDetailsSourceMock;

    @Autowired
    PrivilegeService privilegeServiceMock;

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
    public void shouldAddNewRole() {
        // given
        String roleName = "New member role";
        PermissionRoleDefinition roleDefinition = permissionRoleService.getRoleDefinition(PredefinedRole.STANDARD_MEMBER);
        PermissionTable permissionTable = permissionRoleService.getPermissionTable(PredefinedRole.STANDARD_MEMBER);

        roleDefinition.setName(roleName);
        roleDefinition.setPredefinedRole(Optional.empty());
        roleDefinition.setSourcePredefinedRole(PredefinedRole.STANDARD_MEMBER);

        // when
        PermissionRoleDefinition newRoleDef = permissionRoleService.addRole(roleDefinition, permissionTable);

        // then
        assertThat(newRoleDef.getName()).isEqualTo(roleName);
    }

    @Test
    public void shouldGetRoleDefinition_byId() {
        // given
        PermissionRoleDefinition newRole = addAndGetNewRole();

        // when
        PermissionRoleDefinition roleDefinition = permissionRoleService.getRoleDefinition(newRole.getId());

        // then
        assertThat(roleDefinition.getName()).isEqualTo("New member role");
    }

    @Test
    public void shouldGetRolePermissionTable_byId() {
        // given
        PermissionRoleDefinition newRole = addAndGetNewRole();

        // when
        PermissionTable permissionTable = permissionRoleService.getPermissionTable(newRole.getId());

        // then
        assertThat(permissionTable.getPermissions()).isNotEmpty();
    }

    @Test
    public void shouldUpdateRole() {
        // given
        String newRoleName = "new name";
        PermissionRoleDefinition newRole = addAndGetNewRole();
        newRole.setName(newRoleName);

        // when
        PermissionTable permissionTable = permissionRoleService.getPermissionTable(newRole.getId());
        permissionRoleService.updatePermissionTable(newRole.getId(), permissionTable);
        PermissionRoleDefinition updateRoleDefinition = permissionRoleService.updateRoleDefinition(newRole);

        // then
        assertThat(updateRoleDefinition.getName()).isEqualTo(newRoleName);
    }

    @Test
    public void shouldMoveRole_toGivenPosition() {
        // given
        PermissionRoleDefinition newRole = addAndGetNewRole();
        assertThat(newRole.getPosition()).isNotEqualTo(1);

        // when
        PermissionRoleDefinition updatedRoleDefinition = permissionRoleService.moveRoleToPosition(newRole.getId(), 1);

        // then
        assertThat(updatedRoleDefinition.getPosition()).isEqualTo(1);
    }

    @Test
    public void shouldRemoveRole() {
        // given
        PermissionRoleDefinition newRole = addAndGetNewRole();

        // when
        permissionRoleService.removeRole(newRole.getId());

        // then
        assertThat(roleRepository.findOne(newRole.getId())).isNull();
    }

    private PermissionRoleDefinition addAndGetNewRole() {
        String roleName = "New member role";
        PermissionRoleDefinition roleDefinition = permissionRoleService.getRoleDefinition(PredefinedRole.STANDARD_ANONYMOUS);
        PermissionTable permissionTable = permissionRoleService.getPermissionTable(PredefinedRole.STANDARD_ANONYMOUS);

        roleDefinition.setName(roleName);
        roleDefinition.setPredefinedRole(Optional.empty());
        roleDefinition.setPosition(2);
        roleDefinition.setSourcePredefinedRole(PredefinedRole.STANDARD_ANONYMOUS);

        return permissionRoleService.addRole(roleDefinition, permissionTable);
    }

}