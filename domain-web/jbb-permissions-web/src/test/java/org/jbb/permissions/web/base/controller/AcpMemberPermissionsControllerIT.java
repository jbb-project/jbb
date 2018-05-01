/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.web.base.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.google.common.collect.Lists;
import java.util.Optional;
import org.jbb.members.api.base.Member;
import org.jbb.members.api.base.MemberService;
import org.jbb.permissions.api.PermissionMatrixService;
import org.jbb.permissions.api.PermissionRoleService;
import org.jbb.permissions.api.PermissionService;
import org.jbb.permissions.api.identity.AdministratorGroupIdentity;
import org.jbb.permissions.api.matrix.PermissionMatrix;
import org.jbb.permissions.api.matrix.PermissionTable;
import org.jbb.permissions.api.permission.PermissionType;
import org.jbb.permissions.api.permission.PermissionValue;
import org.jbb.permissions.api.permission.domain.MemberPermissions;
import org.jbb.permissions.api.role.PermissionRoleDefinition;
import org.jbb.permissions.api.role.PredefinedRole;
import org.jbb.permissions.web.BaseIT;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

public class AcpMemberPermissionsControllerIT extends BaseIT {

    @Autowired
    WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    private MemberService memberServiceMock;

    @Autowired
    private PermissionService permissionServiceMock;

    @Autowired
    private PermissionRoleService permissionRoleServiceMock;

    @Autowired
    private PermissionMatrixService permissionMatrixServiceMock;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
            .apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }

    @Test
    public void shouldPutRoleType_andPermissionViewName_toModel_whenGET() throws Exception {
        // given
        given(permissionServiceMock.checkPermission(any())).willReturn(true);

        // when
        ResultActions result = mockMvc.perform(get("/acp/permissions/global-members"));

        // then
        result.andExpect(status().isOk())
            .andExpect(view().name("acp/permissions/security-identity-choose"))
            .andExpect(model().attributeExists("roleTypeSuffix"))
            .andExpect(model().attributeExists("permissionViewName"));
    }

    @Test
    public void shouldPutPermissionsMatrix_toModel_whenPOST() throws Exception {
        // given
        given(permissionServiceMock.checkPermission(any())).willReturn(true);

        Member member = mock(Member.class);
        given(member.getId()).willReturn(11L);
        given(memberServiceMock.getMemberWithDisplayedName(any())).willReturn(Optional.of(member));

        PermissionRoleDefinition roleDef = PermissionRoleDefinition.builder()
            .id(1L)
            .name("role name")
            .position(1)
            .permissionType(PermissionType.MEMBER_PERMISSIONS)
            .predefinedRole(Optional.of(PredefinedRole.STANDARD_MEMBER))
            .build();

        given(permissionRoleServiceMock.getRoleDefinitions(any()))
            .willReturn(Lists.newArrayList(roleDef));

        PermissionTable permissionTable = PermissionTable.builder()
            .putPermission(MemberPermissions.CAN_CHANGE_EMAIL, PermissionValue.NO)
            .build();

        given(permissionMatrixServiceMock.getPermissionMatrix(any(), any())).willReturn(
            PermissionMatrix.builder()
                .permissionType(PermissionType.MEMBER_PERMISSIONS)
                .securityIdentity(AdministratorGroupIdentity.getInstance())
                .permissionTable(Optional.of(permissionTable))
                .build()
        );

        // when
        ResultActions result = mockMvc.perform(post("/acp/permissions/global-members")
            .param("memberDisplayedName", "aaa")
            .param("identityType", "ANONYMOUS")
        );

        // then
        result.andExpect(status().isOk())
            .andExpect(view().name("acp/permissions/global-members"))
            .andExpect(model().attributeExists("roles"))
            .andExpect(model().attributeExists("permissionMatrixForm"))
            .andExpect(model().attributeExists("securityIdentity"))
            .andExpect(model().attributeExists("roleTypeSuffix"));
    }

}