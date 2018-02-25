/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.impl.sync;

import org.jbb.permissions.api.PermissionRoleService;
import org.jbb.permissions.api.role.PermissionRoleDefinition;
import org.jbb.permissions.impl.role.predefined.PredefinedRoleDetails;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

import lombok.RequiredArgsConstructor;

@Order(6)
@Component
@RequiredArgsConstructor
public class PredefinedPermissionRolesSyncHandler implements SyncHandler {

    private final PermissionRoleService permissionRoleService;
    private final List<PredefinedRoleDetails> predefinedRolesDetails;

    @Override
    public void synchronize() {
        predefinedRolesDetails.forEach(this::addIfAbsent);
    }

    private void addIfAbsent(PredefinedRoleDetails role) {
        PermissionRoleDefinition roleDefinition = permissionRoleService.getRoleDefinition(role.getPredefinedRole());
        if (roleDefinition == null) {
            permissionRoleService.addRole(role.getDefinition(), role.getPermissionTable());
        }
    }
}
