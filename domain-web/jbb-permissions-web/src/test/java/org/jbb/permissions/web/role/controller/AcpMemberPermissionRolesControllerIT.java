/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.web.role.controller;

import org.assertj.core.util.Lists;
import org.jbb.permissions.api.PermissionRoleService;
import org.jbb.permissions.api.PermissionService;
import org.jbb.permissions.api.role.PermissionRoleDefinition;
import org.jbb.permissions.web.BaseIT;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class AcpMemberPermissionRolesControllerIT extends BaseIT {

    @Autowired
    WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    private PermissionService permissionServiceMock;

    @Autowired
    private PermissionRoleService permissionRoleServiceMock;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void shouldPutRoles_andPermissionToManageFlag_toModel() throws Exception {
        // given
        given(permissionRoleServiceMock.getRoleDefinitions(any()))
                .willReturn(Lists.newArrayList(mock(PermissionRoleDefinition.class)));
        given(permissionServiceMock.checkPermission(any())).willReturn(true);

        // when
        ResultActions result = mockMvc.perform(get("/acp/permissions/role-members"));

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("acp/permissions/role-members"))
                .andExpect(model().attributeExists("roles"))
                .andExpect(model().attributeExists("hasPermissionToManage"));
    }

}