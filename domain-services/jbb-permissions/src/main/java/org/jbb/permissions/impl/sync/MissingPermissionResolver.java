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

import org.jbb.permissions.api.permission.Permission;
import org.jbb.permissions.api.permission.PermissionValue;
import org.jbb.permissions.api.role.PredefinedRole;
import org.jbb.permissions.impl.acl.PermissionTranslator;
import org.jbb.permissions.impl.acl.model.AclPermissionEntity;
import org.jbb.permissions.impl.role.model.AclRoleEntity;
import org.jbb.permissions.impl.role.predefined.PredefinedRoleDetails;
import org.springframework.stereotype.Component;

import java.util.List;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MissingPermissionResolver {
    private static final String MISSING_PREDEFINED_ROLE_BEAN = "Missing bean of type PredefinedRoleDetails for " +
            "one of the predefined roles";
    private static final String MISSING_PERMISSION_IN_PREFEDINED_ROLE_DEF = "Missing value for the permission " +
            "in PrefedinedRoleDetails";

    private final List<PredefinedRoleDetails> predefinedRolesDetails;
    private final PermissionTranslator permissionTranslator;

    public Permission resolve(AclRoleEntity role, AclPermissionEntity targetPermission) {
        return permissionTranslator.toApiModel(targetPermission, buildValue(role, targetPermission));
    }

    private PermissionValue buildValue(AclRoleEntity role, AclPermissionEntity permission) {
        PredefinedRole predefinedRole = role.getPredefinedRole();
        PredefinedRole sourceRole = role.getSourcePredefinedRole();
        if (predefinedRole == null) {
            if (sourceRole == null) {
                return PermissionValue.NO;
            }
            predefinedRole = sourceRole;
        }

        return readPermissionValue(predefinedRole, permission);
    }

    private PermissionValue readPermissionValue(PredefinedRole predefinedRole, AclPermissionEntity permission) {
        PredefinedRoleDetails roleDetails = predefinedRolesDetails.stream()
                .filter(details -> details.getPredefinedRole().equals(predefinedRole))
                .findFirst().orElseThrow(() -> new IllegalStateException(MISSING_PREDEFINED_ROLE_BEAN));

        Permission foundPermission = roleDetails.getPermissionTable().getPermissions().stream()
                .filter(p -> p.getDefinition().getCode().equals(permission.getCode()))
                .findFirst().orElseThrow(() -> new IllegalStateException(MISSING_PERMISSION_IN_PREFEDINED_ROLE_DEF));

        return foundPermission.getValue();
    }
}
