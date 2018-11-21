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

import static org.jbb.permissions.api.permission.PermissionValue.NO;
import static org.jbb.permissions.api.permission.PermissionValue.YES;
import static org.jbb.permissions.api.permission.domain.MemberPermissions.CAN_CHANGE_DISPLAYED_NAME;
import static org.jbb.permissions.api.permission.domain.MemberPermissions.CAN_CHANGE_EMAIL;
import static org.jbb.permissions.api.permission.domain.MemberPermissions.CAN_SEE_STACKTRACE;
import static org.jbb.permissions.api.permission.domain.MemberPermissions.CAN_VIEW_FAQ;

import java.util.Optional;
import org.jbb.permissions.api.matrix.PermissionTable;
import org.jbb.permissions.api.permission.PermissionType;
import org.jbb.permissions.api.role.PermissionRoleDefinition;
import org.jbb.permissions.api.role.PredefinedRole;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(2)
@Component
public class StandardAnonymousRole implements PredefinedRoleDetails {

    @Override
    public PredefinedRole getPredefinedRole() {
        return PredefinedRole.STANDARD_ANONYMOUS;
    }

    @Override
    public PermissionRoleDefinition getDefinition() {
        return PermissionRoleDefinition.builder()
                .name("Standard Anonymous")
                .description("Standard anonymous role")
                .permissionType(PermissionType.MEMBER_PERMISSIONS)
                .predefinedRole(Optional.of(PredefinedRole.STANDARD_ANONYMOUS))
                .build();
    }

    @Override
    public PermissionTable getPermissionTable() {
        return PermissionTable.builder()
                // Profile permissions
                .putPermission(CAN_CHANGE_EMAIL, NO)
                .putPermission(CAN_CHANGE_DISPLAYED_NAME, NO)
                // Misc permissions
                .putPermission(CAN_VIEW_FAQ, YES)
            .putPermission(CAN_SEE_STACKTRACE, NO)
                .build();
    }

}
