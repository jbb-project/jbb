/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.impl.vote;

import org.assertj.core.util.Lists;
import org.jbb.lib.commons.security.SecurityContentUser;
import org.jbb.lib.commons.security.UserDetailsSource;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.members.event.MemberRegisteredEvent;
import org.jbb.permissions.api.PermissionService;
import org.jbb.permissions.api.effective.EffectivePermissionTable;
import org.jbb.permissions.api.effective.PermissionVerdict;
import org.jbb.permissions.api.identity.AdministratorGroupIdentity;
import org.jbb.permissions.api.identity.AnonymousIdentity;
import org.jbb.permissions.api.identity.RegisteredMembersIdentity;
import org.jbb.permissions.api.permission.PermissionDefinition;
import org.jbb.permissions.api.permission.PermissionType;
import org.jbb.permissions.api.permission.domain.AdministratorPermissions;
import org.jbb.permissions.api.permission.domain.MemberPermissions;
import org.jbb.permissions.impl.BaseIT;
import org.jbb.security.api.role.RoleService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class PermissionServiceIT extends BaseIT {

    @Autowired
    PermissionService permissionService;

    @Autowired
    UserDetailsSource userDetailsSourceMock;

    @Autowired
    RoleService roleServiceMock;

    @Autowired
    JbbEventBus eventBus;

    @Test
    public void anonymous_shouldNotHaveAnyAdministratorPermissions() {
        // when
        EffectivePermissionTable permissionTable = permissionService.getEffectivePermissionTable(PermissionType.ADMINISTRATOR_PERMISSIONS,
                AnonymousIdentity.getInstance());

        // then
        assertThat(permissionTable.getPermissions()).allSatisfy(
                permission -> permission.getVerdict().equals(PermissionVerdict.FORBIDDEN)
        );
    }

    @Test
    public void registeredMembers_shouldNotHaveAnyAdministratorPermissions() {
        // when
        EffectivePermissionTable permissionTable = permissionService.getEffectivePermissionTable(PermissionType.ADMINISTRATOR_PERMISSIONS,
                RegisteredMembersIdentity.getInstance());

        // then
        assertThat(permissionTable.getPermissions()).allSatisfy(
                permission -> permission.getVerdict().equals(PermissionVerdict.FORBIDDEN)
        );
    }

    @Test
    public void administrators_shouldHaveAllAdministratorPermissions() {
        // when
        EffectivePermissionTable permissionTable = permissionService.getEffectivePermissionTable(PermissionType.ADMINISTRATOR_PERMISSIONS,
                AdministratorGroupIdentity.getInstance());

        // then
        assertThat(permissionTable.getPermissions()).allSatisfy(
                permission -> permission.getVerdict().equals(PermissionVerdict.ALLOW)
        );
    }

    @Test
    public void notAdministratorMember_shouldHaveAllMemberPermissions() {
        // given
        Long memberId = 12L;
        prepareMember(memberId, false);

        // when
        Set<PermissionDefinition> allowedPermissions = permissionService
                .getAllAllowedGlobalPermissions(memberId);

        // then
        assertThat(allowedPermissions).containsExactlyInAnyOrder(MemberPermissions.values());
    }

    @Test
    public void administratorMember_shouldHaveAllMemberAndAdministratorPermissions() {
        // given
        Long memberId = 13L;
        prepareMember(memberId, true);

        // when
        Set<PermissionDefinition> allowedPermissions = permissionService
                .getAllAllowedGlobalPermissions(memberId);

        // then
        ArrayList<PermissionDefinition> permissions = Lists
                .newArrayList(MemberPermissions.values());
        permissions.addAll(Lists.newArrayList(AdministratorPermissions.values()));
        assertThat(allowedPermissions)
                .containsExactlyInAnyOrder(permissions.toArray(new PermissionDefinition[]{}));
    }

    @Test
    public void shouldNotThrowException_whenAdministratorAskForAdministratorPermissions() {
        // given
        Long memberId = 13L;
        prepareMember(memberId, true);

        // when
        permissionService.assertPermission(AdministratorPermissions.CAN_MANAGE_PERMISSION_ROLES);
    }

    @Test
    public void anonymousMember_shouldHaveOnlyFaqReadAccess() {
        // given
        Long memberId = 0L; //anonymous

        // when
        Set<PermissionDefinition> allowedPermissions = permissionService
                .getAllAllowedGlobalPermissions(memberId);

        // then
        assertThat(allowedPermissions).containsExactlyInAnyOrder(MemberPermissions.CAN_VIEW_FAQ);
    }

    @Test
    public void getPermissionDefinitionByCode_whenNotFound() {
        // when
        Optional<PermissionDefinition> definition = permissionService.getPermissionDefinitionByCode("NOT_EXISTING_CODE");

        // then
        assertThat(definition).isNotPresent();
    }

    @Test
    public void getPermissionDefinitionByCode_whenFound() {
        // given
        AdministratorPermissions permission = AdministratorPermissions.CAN_ADD_FORUMS;

        // when
        Optional<PermissionDefinition> definition = permissionService.getPermissionDefinitionByCode(permission.getCode());

        // then
        assertThat(definition).isPresent();
        assertThat(definition.get().getCode()).isEqualTo(permission.getCode());
    }

    private void prepareMember(Long memberId, boolean isAdministrator) {
        eventBus.post(new MemberRegisteredEvent(memberId));
        SecurityContentUser securityContentUser = mock(SecurityContentUser.class);
        given(userDetailsSourceMock.getFromApplicationContext()).willReturn(securityContentUser);
        given(securityContentUser.getUserId()).willReturn(memberId);
        given(roleServiceMock.hasAdministratorRole(eq(memberId))).willReturn(isAdministrator);
    }
}