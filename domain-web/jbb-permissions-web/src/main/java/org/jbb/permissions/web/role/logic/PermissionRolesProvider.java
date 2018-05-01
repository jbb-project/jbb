/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.web.role.logic;

import org.jbb.permissions.api.PermissionRoleService;
import org.jbb.permissions.api.PermissionService;
import org.jbb.permissions.api.permission.PermissionType;
import org.jbb.permissions.api.permission.domain.AdministratorPermissions;
import org.jbb.permissions.api.role.PermissionRoleDefinition;
import org.jbb.permissions.web.role.model.RoleDefinition;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PermissionRolesProvider {

    private final PermissionService permissionService;
    private final PermissionRoleService permissionRoleService;

    private final PermissionRoleDefinitionMapper roleDefinitionMapper;

    public List<RoleDefinition> getRoleDefinition(PermissionType permissionType) {
        return mapToDto(permissionRoleService.getRoleDefinitions(permissionType));
    }

    public boolean hasPermissionToManageRoles() {
        return permissionService.checkPermission(AdministratorPermissions.CAN_MANAGE_PERMISSION_ROLES);
    }

    private List<RoleDefinition> mapToDto(List<PermissionRoleDefinition> roleDefinitions) {
        return roleDefinitions.stream()
                .map(roleDefinitionMapper::toDto)
                .sorted(Comparator.comparing(RoleDefinition::getPosition))
                .collect(Collectors.toList());
    }
}
