/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.impl.vote;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Set;
import org.assertj.core.util.Lists;
import org.jbb.lib.commons.security.SecurityContentUser;
import org.jbb.lib.commons.security.UserDetailsSource;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.members.event.MemberRegistrationEvent;
import org.jbb.permissions.api.PermissionService;
import org.jbb.permissions.api.permission.PermissionDefinition;
import org.jbb.permissions.api.permission.domain.AdministratorPermissions;
import org.jbb.permissions.api.permission.domain.MemberPermissions;
import org.jbb.permissions.impl.BaseIT;
import org.jbb.security.api.role.RoleService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

public class PermissionServiceIT extends BaseIT {

    @Autowired
    PermissionService permissionService;

    @Autowired
    UserDetailsSource userDetailsSourceMock;

    @Autowired
    RoleService roleServiceMock;

    @Autowired
    JbbEventBus eventBus;

    @Before
    public void setUp() throws Exception {
        Mockito.reset(userDetailsSourceMock, roleServiceMock);
    }

    @Test
    public void notAdministratorMember_shouldHaveAllMemberPermissions() throws Exception {
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
    public void administratorMember_shouldHaveAllMemberAndAdministratorPermissions()
        throws Exception {
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
    public void anonymousMember_shouldHaveOnlyFaqReadAccess() throws Exception {
        // given
        Long memberId = 0L; //anonymous

        // when
        Set<PermissionDefinition> allowedPermissions = permissionService
            .getAllAllowedGlobalPermissions(memberId);

        // then
        assertThat(allowedPermissions).containsExactlyInAnyOrder(MemberPermissions.CAN_VIEW_FAQ);
    }

    private void prepareMember(Long memberId, boolean isAdministrator) {
        eventBus.post(new MemberRegistrationEvent(memberId));
        SecurityContentUser securityContentUser = mock(SecurityContentUser.class);
        given(userDetailsSourceMock.getFromApplicationContext()).willReturn(securityContentUser);
        given(securityContentUser.getUserId()).willReturn(memberId);
        given(roleServiceMock.hasAdministratorRole(eq(memberId))).willReturn(isAdministrator);
    }
}