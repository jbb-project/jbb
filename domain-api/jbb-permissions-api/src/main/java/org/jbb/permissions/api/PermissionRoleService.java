/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.api;

import org.jbb.permissions.api.matrix.PermissionTable;
import org.jbb.permissions.api.permission.PermissionType;
import org.jbb.permissions.api.role.PermissionRoleDefinition;
import org.jbb.permissions.api.role.PredefinedRole;

import java.util.List;

public interface PermissionRoleService {

    List<PermissionRoleDefinition> getRoleDefinitions(PermissionType permissionType);

    PermissionRoleDefinition getRoleDefinition(PredefinedRole predefinedRole);

    PermissionRoleDefinition addRole(PermissionRoleDefinition role,
                                     PermissionTable permissionTable);

    void removeRole(Long roleId);

    PermissionTable getPermissionTable(Long roleId);

    PermissionTable getPermissionTable(PredefinedRole predefinedRole);

    PermissionRoleDefinition updateRoleDefinition(PermissionRoleDefinition role);

    PermissionTable updatePermissionTable(Long roleId,
                                          PermissionTable permissionTable);

}
