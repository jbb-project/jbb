/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.api;

import java.util.List;
import org.jbb.permissions.api.matrix.PermissionTable;
import org.jbb.permissions.api.permission.PermissionType;
import org.jbb.permissions.api.role.PermissionRoleDefinition;

public interface PermissionRoleService {

    List<PermissionRoleDefinition> getRoles(PermissionType permissionType);

    PermissionRoleDefinition addRole(PermissionRoleDefinition role,
        PermissionTable permissionTable);

    void removeRole(Long roleId);

    PermissionTable getPermissionTable(PermissionRoleDefinition role);

    PermissionRoleDefinition updatePermissionRoleDefinition(PermissionRoleDefinition role);

    PermissionTable updatePermissionTable(PermissionRoleDefinition role,
        PermissionTable permissionTable);

}
