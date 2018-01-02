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

import com.github.zafarkhaja.semver.Version;

import org.jbb.install.InstallUpdateAction;
import org.jbb.install.InstallationData;
import org.jbb.install.JbbVersions;
import org.jbb.permissions.api.PermissionMatrixService;
import org.jbb.permissions.api.PermissionRoleService;
import org.jbb.permissions.api.identity.AdministratorGroupIdentity;
import org.jbb.permissions.api.identity.AnonymousIdentity;
import org.jbb.permissions.api.identity.RegisteredMembersIdentity;
import org.jbb.permissions.api.matrix.PermissionMatrix;
import org.jbb.permissions.api.permission.PermissionType;
import org.jbb.permissions.api.role.PermissionRoleDefinition;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

import static org.jbb.permissions.api.role.PredefinedRole.STANDARD_ADMINISTRATOR;
import static org.jbb.permissions.api.role.PredefinedRole.STANDARD_ANONYMOUS;
import static org.jbb.permissions.api.role.PredefinedRole.STANDARD_MEMBER;

@Order(4)
@Component
@RequiredArgsConstructor
public class AclRoleActivateAction implements InstallUpdateAction {

    private final PermissionRoleService permissionRoleService;
    private final PermissionMatrixService permissionMatrixService;

    @Override
    public Version fromVersion() {
        return JbbVersions.VERSION_0_10_0;
    }

    @Override
    public void install(InstallationData installationData) {

        PermissionRoleDefinition standardMemberRole = permissionRoleService.getRoleDefinition(STANDARD_MEMBER);
        permissionMatrixService.setPermissionMatrix(
                PermissionMatrix.builder()
                        .permissionType(PermissionType.MEMBER_PERMISSIONS)
                        .securityIdentity(RegisteredMembersIdentity.getInstance())
                        .assignedRole(Optional.of(standardMemberRole))
                        .build()
        );

        PermissionRoleDefinition standardAnonymousRole = permissionRoleService.getRoleDefinition(STANDARD_ANONYMOUS);
        permissionMatrixService.setPermissionMatrix(
                PermissionMatrix.builder()
                        .permissionType(PermissionType.MEMBER_PERMISSIONS)
                        .securityIdentity(AnonymousIdentity.getInstance())
                        .assignedRole(Optional.of(standardAnonymousRole))
                        .build()
        );

        PermissionRoleDefinition standardAdministratorRole = permissionRoleService.getRoleDefinition(STANDARD_ADMINISTRATOR);
        permissionMatrixService.setPermissionMatrix(
                PermissionMatrix.builder()
                        .permissionType(PermissionType.ADMINISTRATOR_PERMISSIONS)
                        .securityIdentity(AdministratorGroupIdentity.getInstance())
                        .assignedRole(Optional.of(standardAdministratorRole))
                        .build()
        );
    }
}


