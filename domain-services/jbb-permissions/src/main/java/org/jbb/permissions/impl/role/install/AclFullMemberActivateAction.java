/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.impl.role.install;

import static org.jbb.permissions.api.role.PredefinedRole.FULL_MEMBER;

import com.github.zafarkhaja.semver.Version;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jbb.install.InstallUpdateAction;
import org.jbb.install.InstallationData;
import org.jbb.install.JbbVersions;
import org.jbb.permissions.api.PermissionMatrixService;
import org.jbb.permissions.api.PermissionRoleService;
import org.jbb.permissions.api.identity.AdministratorGroupIdentity;
import org.jbb.permissions.api.matrix.PermissionMatrix;
import org.jbb.permissions.api.permission.PermissionType;
import org.jbb.permissions.api.role.PermissionRoleDefinition;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(3)
@Component
@RequiredArgsConstructor
public class AclFullMemberActivateAction implements InstallUpdateAction {

    private final PermissionRoleService permissionRoleService;
    private final PermissionMatrixService permissionMatrixService;

    @Override
    public Version fromVersion() {
        return JbbVersions.VERSION_0_11_0;
    }

    @Override
    public void install(InstallationData installationData) {

        PermissionRoleDefinition fullMemberRole = permissionRoleService
            .getRoleDefinition(FULL_MEMBER);
        permissionMatrixService.setPermissionMatrix(
            PermissionMatrix.builder()
                .permissionType(PermissionType.MEMBER_PERMISSIONS)
                .securityIdentity(AdministratorGroupIdentity.getInstance())
                .assignedRole(Optional.of(fullMemberRole))
                .build()
        );
    }
}


