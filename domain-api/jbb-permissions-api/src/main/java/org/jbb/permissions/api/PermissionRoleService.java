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
import org.jbb.permissions.api.role.PermissionRole;

public interface PermissionRoleService {

    List<PermissionRole> getRoles(PermissionType permissionType);

    PermissionRole addRole(PermissionRole role, PermissionTable permissionTable);

    void removeRole(PermissionRole permissionRole);

    PermissionTable getPermissionTable(PermissionRole role);

    PermissionTable updatePermissionTable(PermissionRole role);

}
