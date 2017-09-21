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

import java.util.Set;
import org.jbb.permissions.api.effective.EffectivePermissionTable;
import org.jbb.permissions.api.identity.SecurityIdentity;
import org.jbb.permissions.api.permission.Permission;
import org.jbb.permissions.api.permission.PermissionDefinition;
import org.jbb.permissions.api.permission.PermissionType;

public interface PermissionService {

    EffectivePermissionTable getEffectivePermissionTable(PermissionType permissionType,
        SecurityIdentity securityIdentity);

    boolean checkPermission(PermissionDefinition permissionDefinition);

    Set<Permission> getAllowedGlobalPermissions(Long memberId);

}
