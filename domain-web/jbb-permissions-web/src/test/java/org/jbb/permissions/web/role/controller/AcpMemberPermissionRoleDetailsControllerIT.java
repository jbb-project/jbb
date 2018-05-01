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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.assertj.core.util.Lists;
import org.jbb.permissions.api.PermissionRoleService;
import org.jbb.permissions.api.exceptions.RemovePredefinedRoleException;
import org.jbb.permissions.api.matrix.PermissionTable;
import org.jbb.permissions.api.permission.Permission;
import org.jbb.permissions.api.permission.PermissionDefinition;
import org.jbb.permissions.api.permission.PermissionValue;
import org.jbb.permissions.api.permission.domain.AllPermissionCategories;
import org.jbb.permissions.api.permission.domain.MemberPermissions;
import org.jbb.permissions.api.role.PermissionRoleDefinition;
import org.jbb.permissions.web.BaseIT;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

public class AcpMemberPermissionRoleDetailsControllerIT extends BaseIT {

    @Autowired
    WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    private PermissionRoleService permissionRoleServiceMock;

    @Before
    public void setUp() {
        Mockito.reset(permissionRoleServiceMock);
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void shouldPutRoleDetails_intoModel_whenGet() throws Exception {
        // given
        prepareMockForRoleDefAndPermissionTable();

        // when
        ResultActions result = mockMvc.perform(get("/acp/permissions/role-members/details")
                .param("id", "12"));

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("acp/permissions/role-details"))
                .andExpect(model().attributeExists("roleDetailsForm"))
                .andExpect(model().attributeExists("roleDetails"))
                .andExpect(model().attributeExists("roleTypeSuffix"));
    }

    @Test
    public void shouldPutPredefinedRole_intoModel_whenGetNewRole() throws Exception {
        // given
        given(permissionRoleServiceMock.getPredefinedRoles(any()))
                .willReturn(Lists.newArrayList(mock(PermissionRoleDefinition.class)));

        // when
        ResultActions result = mockMvc.perform(get("/acp/permissions/role-members/new"));

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("acp/permissions/predefined-roles-choose"))
                .andExpect(model().attributeExists("predefinedRoleForm"))
                .andExpect(model().attributeExists("roleTypeSuffix"))
                .andExpect(model().attributeExists("predefinedRoles"));
    }

    @Test
    public void shouldFillModelWithPermissions_whenCreateOrEditRoleDetails() throws Exception {
        // given
        prepareMockForRoleDefAndPermissionTable();

        // when
        ResultActions result = mockMvc.perform(post("/acp/permissions/role-members/new/details")
                .param("roleId", "12"));

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("acp/permissions/role-details"))
                .andExpect(model().attributeExists("roleDetailsForm"))
                .andExpect(model().attributeExists("roleDetails"))
                .andExpect(model().attributeExists("roleTypeSuffix"));
    }

    @Test
    public void shouldInvokeAddRoleServiceMethod_whenCreateRole() throws Exception {
        // given
        prepareMockForRoleDefAndPermissionTable();

        // when
        ResultActions result = mockMvc.perform(post("/acp/permissions/role-members")
            .param("definition.name", "new test role")
            .param("valueMap.CAN_VIEW_FAQ", "NEVER")
        );

        // then
        result.andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:role-members"));
        verify(permissionRoleServiceMock, times(1)).addRole(any(), any());
    }

    @Test
    public void shouldInvokeUpdateServiceMethods_whenEditRole() throws Exception {
        // given
        prepareMockForRoleDefAndPermissionTable();

        // when
        ResultActions result = mockMvc.perform(post("/acp/permissions/role-members")
            .param("definition.id", "11")
            .param("definition.name", "new test role")
            .param("valueMap.CAN_VIEW_FAQ", "NEVER")
        );

        // then
        result.andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:role-members"));
        verify(permissionRoleServiceMock, times(1)).updatePermissionTable(eq(11L), any());
        verify(permissionRoleServiceMock, times(1)).updateRoleDefinition(any());
    }

    @Test
    public void shouldRemoveRole_whenDelete_andRedirect() throws Exception {
        // when
        ResultActions result = mockMvc.perform(post("/acp/permissions/role-members/delete")
                .param("id", "12"));

        // then
        result.andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeCount(0));
    }

    @Test
    public void shouldNotRemoveRole_whenDelete_andTriedToRemovePredefinedRole() throws Exception {
        // given
        Mockito.doThrow(new RemovePredefinedRoleException()).when(permissionRoleServiceMock).removeRole(any());

        // when
        ResultActions result = mockMvc.perform(post("/acp/permissions/role-members/delete")
                .param("id", "12"));

        // then
        result.andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("removePredefinedRoleError", true));
    }

    @Test
    public void shouldMoveUp_andRedirect() throws Exception {
        // when
        ResultActions result = mockMvc.perform(post("/acp/permissions/role-members/moveup")
                .param("id", "12").param("position", "4"));

        // then
        result.andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeCount(0));
    }

    @Test
    public void shouldMoveDown_andRedirect() throws Exception {
        // when
        ResultActions result = mockMvc.perform(post("/acp/permissions/role-members/movedown")
                .param("id", "12").param("position", "4"));

        // then
        result.andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeCount(0));
    }

    private void prepareMockForRoleDefAndPermissionTable() {
        given(permissionRoleServiceMock.getRoleDefinition(any(Long.class)))
                .willReturn(mock(PermissionRoleDefinition.class));
        PermissionDefinition permissionDefinition = mock(PermissionDefinition.class);
        given(permissionDefinition.getCode()).willReturn(MemberPermissions.CAN_CHANGE_DISPLAYED_NAME.getCode());
        given(permissionDefinition.getCategory()).willReturn(AllPermissionCategories.MEMBERS);
        Permission permission = new Permission();
        permission.setDefinition(permissionDefinition);
        permission.setValue(PermissionValue.NEVER);
        PermissionTable permissionTable = PermissionTable.builder()
                .putPermission(permission)
                .build();
        given(permissionRoleServiceMock.getPermissionTable(any(Long.class))).willReturn(permissionTable);
    }

}