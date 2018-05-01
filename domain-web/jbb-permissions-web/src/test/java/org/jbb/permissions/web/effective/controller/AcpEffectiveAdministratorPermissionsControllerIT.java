/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.web.effective.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Optional;
import org.jbb.members.api.base.Member;
import org.jbb.members.api.base.MemberService;
import org.jbb.permissions.api.PermissionService;
import org.jbb.permissions.api.effective.EffectivePermission;
import org.jbb.permissions.api.effective.EffectivePermissionTable;
import org.jbb.permissions.api.effective.PermissionVerdict;
import org.jbb.permissions.api.permission.domain.AdministratorPermissions;
import org.jbb.permissions.web.BaseIT;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


public class AcpEffectiveAdministratorPermissionsControllerIT extends BaseIT {

    @Autowired
    WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    private PermissionService permissionServiceMock;

    @Autowired
    private MemberService memberServiceMock;

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
        ResultActions result = mockMvc.perform(get("/acp/permissions/effective-administrators"));

        // then
        result.andExpect(status().isOk())
            .andExpect(view().name("acp/permissions/security-identity-choose"))
            .andExpect(model().attributeExists("roleTypeSuffix"))
            .andExpect(model().attributeExists("permissionViewName"));
    }

    @Test
    public void shouldPutEffectivePermissions_toModel_whenPOST() throws Exception {
        // given
        given(permissionServiceMock.checkPermission(any())).willReturn(true);
        Member member = mock(Member.class);
        given(member.getId()).willReturn(11L);
        given(memberServiceMock.getMemberWithDisplayedName(any())).willReturn(Optional.of(member));

        given(permissionServiceMock.getEffectivePermissionTable(any(), any())).willReturn(
            EffectivePermissionTable.builder()
                .putPermission(EffectivePermission.builder()
                    .definition(AdministratorPermissions.CAN_ADD_FORUMS)
                    .verdict(PermissionVerdict.ALLOW)
                    .build())
                .build());

        // when
        ResultActions result = mockMvc.perform(post("/acp/permissions/effective-administrators")
            .param("memberDisplayedName", "aaa")
            .param("identityType", "ADMIN_GROUP")
        );

        // then
        result.andExpect(status().isOk())
            .andExpect(view().name("acp/permissions/effective-administrators"))
            .andExpect(model().attributeExists("effectivePermissions"));
    }
}