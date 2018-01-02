/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.impl.role.predefined;

import org.jbb.permissions.api.matrix.PermissionTable;
import org.jbb.permissions.api.permission.PermissionType;
import org.jbb.permissions.api.role.PermissionRoleDefinition;
import org.jbb.permissions.api.role.PredefinedRole;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.jbb.permissions.api.permission.PermissionValue.NO;
import static org.jbb.permissions.api.permission.PermissionValue.YES;
import static org.jbb.permissions.api.permission.domain.AdministratorPermissions.CAN_ADD_FORUMS;
import static org.jbb.permissions.api.permission.domain.AdministratorPermissions.CAN_ALTER_ADMINISTRATOR_PERMISSIONS;
import static org.jbb.permissions.api.permission.domain.AdministratorPermissions.CAN_ALTER_MEMBER_PERMISSIONS;
import static org.jbb.permissions.api.permission.domain.AdministratorPermissions.CAN_DELETE_FORUMS;
import static org.jbb.permissions.api.permission.domain.AdministratorPermissions.CAN_DELETE_MEMBERS;
import static org.jbb.permissions.api.permission.domain.AdministratorPermissions.CAN_MANAGE_MEMBERS;
import static org.jbb.permissions.api.permission.domain.AdministratorPermissions.CAN_MANAGE_PERMISSION_ROLES;
import static org.jbb.permissions.api.permission.domain.AdministratorPermissions.CAN_MODIFY_FORUMS;

@Component
public class JuniorAdministratorRole implements PredefinedRoleDetails {

    @Override
    public PredefinedRole getPredefinedRole() {
        return PredefinedRole.JUNIOR_ADMINISTRATOR;
    }

    @Override
    public PermissionRoleDefinition getDefinition() {
        return PermissionRoleDefinition.builder()
                .name("Junior administrator")
                .description("Junior administrator role")
                .permissionType(PermissionType.ADMINISTRATOR_PERMISSIONS)
                .predefinedRole(Optional.of(PredefinedRole.JUNIOR_ADMINISTRATOR))
                .build();
    }

    @Override
    public PermissionTable getPermissionTable() {
        return PermissionTable.builder()
                // Permission permissions
                .putPermission(CAN_ALTER_ADMINISTRATOR_PERMISSIONS, NO)
                .putPermission(CAN_ALTER_MEMBER_PERMISSIONS, NO)
                .putPermission(CAN_MANAGE_PERMISSION_ROLES, NO)
                // Member permissions
                .putPermission(CAN_MANAGE_MEMBERS, YES)
                .putPermission(CAN_DELETE_MEMBERS, NO)
                // Forum permissions
                .putPermission(CAN_ADD_FORUMS, YES)
                .putPermission(CAN_MODIFY_FORUMS, YES)
                .putPermission(CAN_DELETE_FORUMS, NO)
                .build();
    }

}
