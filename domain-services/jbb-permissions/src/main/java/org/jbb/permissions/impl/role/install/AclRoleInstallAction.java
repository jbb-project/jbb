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
import org.jbb.permissions.api.matrix.PermissionTable;
import org.jbb.permissions.api.permission.PermissionType;
import org.jbb.permissions.api.role.PermissionRoleDefinition;
import org.jbb.permissions.api.role.PredefinedRole;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

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
import static org.jbb.permissions.api.permission.domain.MemberPermissions.CAN_CHANGE_DISPLAYED_NAME;
import static org.jbb.permissions.api.permission.domain.MemberPermissions.CAN_CHANGE_EMAIL;
import static org.jbb.permissions.api.permission.domain.MemberPermissions.CAN_VIEW_FAQ;

@Order(2)
@Component
@RequiredArgsConstructor
public class AclRoleInstallAction implements InstallUpdateAction {

    private final PermissionRoleService permissionRoleService;
    private final PermissionMatrixService permissionMatrixService;

    @Override
    public Version fromVersion() {
        return JbbVersions.VERSION_0_10_0;
    }

    @Override
    public void install(InstallationData installationData) {
        PermissionRoleDefinition standardMemberRole = permissionRoleService
                .addRole(StandardMember.definition(), StandardMember.permissionTable());

        PermissionRoleDefinition standardAnonymousRole = permissionRoleService
                .addRole(StandardAnonymous.definition(), StandardAnonymous
                        .permissionTable());

        PermissionRoleDefinition standardAdministratorRole = permissionRoleService
                .addRole(StandardAdministrator.definition(), StandardAdministrator.permissionTable());

        permissionRoleService.addRole(JuniorAdministrator.definition(),
                JuniorAdministrator.permissionTable());

        permissionMatrixService.setPermissionMatrix(
                PermissionMatrix.builder()
                        .permissionType(PermissionType.MEMBER_PERMISSIONS)
                        .securityIdentity(RegisteredMembersIdentity.getInstance())
                        .assignedRole(Optional.of(standardMemberRole))
                        .build()
        );

        permissionMatrixService.setPermissionMatrix(
                PermissionMatrix.builder()
                        .permissionType(PermissionType.MEMBER_PERMISSIONS)
                        .securityIdentity(AnonymousIdentity.getInstance())
                        .assignedRole(Optional.of(standardAnonymousRole))
                        .build()
        );

        permissionMatrixService.setPermissionMatrix(
                PermissionMatrix.builder()
                        .permissionType(PermissionType.ADMINISTRATOR_PERMISSIONS)
                        .securityIdentity(AdministratorGroupIdentity.getInstance())
                        .assignedRole(Optional.of(standardAdministratorRole))
                        .build()
        );
    }

    public static class StandardMember {

        static PermissionRoleDefinition definition() {
            return PermissionRoleDefinition.builder()
                    .name("Standard member")
                    .description("Standard member role")
                    .permissionType(PermissionType.MEMBER_PERMISSIONS)
                    .predefinedRole(Optional.of(PredefinedRole.STANDARD_MEMBER))
                    .build();
        }

        static PermissionTable permissionTable() {
            return PermissionTable.builder()
                    // Profile permissions
                    .putPermission(CAN_CHANGE_EMAIL, YES)
                    .putPermission(CAN_CHANGE_DISPLAYED_NAME, YES)
                    // Misc permissions
                    .putPermission(CAN_VIEW_FAQ, YES)
                    .build();
        }

    }

    public static class StandardAnonymous {

        static PermissionRoleDefinition definition() {
            return PermissionRoleDefinition.builder()
                    .name("Standard Anonymous")
                    .description("Standard anonymous role")
                    .permissionType(PermissionType.MEMBER_PERMISSIONS)
                    .predefinedRole(Optional.of(PredefinedRole.STANDARD_ANONYMOUS))
                    .build();
        }

        static PermissionTable permissionTable() {
            return PermissionTable.builder()
                    // Profile permissions
                    .putPermission(CAN_CHANGE_EMAIL, NO)
                    .putPermission(CAN_CHANGE_DISPLAYED_NAME, NO)
                    // Misc permissions
                    .putPermission(CAN_VIEW_FAQ, YES)
                    .build();
        }

    }

    public static class StandardAdministrator {

        static PermissionRoleDefinition definition() {
            return PermissionRoleDefinition.builder()
                    .name("Standard administrator")
                    .description("Standard administrator role")
                    .permissionType(PermissionType.ADMINISTRATOR_PERMISSIONS)
                    .predefinedRole(Optional.of(PredefinedRole.STANDARD_ADMINISTRATOR))
                    .build();
        }

        static PermissionTable permissionTable() {
            return PermissionTable.builder()
                    // Permission permissions
                    .putPermission(CAN_ALTER_ADMINISTRATOR_PERMISSIONS, YES)
                    .putPermission(CAN_ALTER_MEMBER_PERMISSIONS, YES)
                    .putPermission(CAN_MANAGE_PERMISSION_ROLES, YES)
                    // Member permissions
                    .putPermission(CAN_MANAGE_MEMBERS, YES)
                    .putPermission(CAN_DELETE_MEMBERS, YES)
                    // Forum permissions
                    .putPermission(CAN_ADD_FORUMS, YES)
                    .putPermission(CAN_MODIFY_FORUMS, YES)
                    .putPermission(CAN_DELETE_FORUMS, YES)
                    .build();
        }

    }


    public static class JuniorAdministrator {

        static PermissionRoleDefinition definition() {
            return PermissionRoleDefinition.builder()
                    .name("Junior administrator")
                    .description("Junior administrator role")
                    .permissionType(PermissionType.ADMINISTRATOR_PERMISSIONS)
                    .predefinedRole(Optional.of(PredefinedRole.JUNIOR_ADMINISTRATOR))
                    .build();
        }

        static PermissionTable permissionTable() {
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
}
