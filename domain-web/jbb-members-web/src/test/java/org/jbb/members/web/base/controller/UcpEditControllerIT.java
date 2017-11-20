/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.web.base.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.google.common.collect.Sets;
import java.util.Optional;
import javax.validation.ConstraintViolation;
import org.jbb.members.api.base.DisplayedName;
import org.jbb.members.api.base.Member;
import org.jbb.members.api.base.MemberService;
import org.jbb.members.api.base.ProfileException;
import org.jbb.members.web.BaseIT;
import org.jbb.members.web.base.form.EditProfileForm;
import org.jbb.permissions.api.PermissionService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

public class UcpEditControllerIT extends BaseIT {
    @Autowired
    WebApplicationContext wac;

    @Autowired
    private MemberService memberServiceMock;

    @Autowired
    private PermissionService permissionServiceMock;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(SecurityMockMvcConfigurers.springSecurity()).build();
        Mockito.reset(memberServiceMock);
    }

    @Test
    @WithMockUser(username = "any", roles = {})
    public void shouldPutCurrentDisplayedNameToForm_whenGet() throws Exception {
        // given
        Member memberMock = mock(Member.class);
        given(memberServiceMock.getMemberWithUsername(any())).willReturn(Optional.of(memberMock));
        DisplayedName displayedName = DisplayedName.builder().value("foo").build();
        given(memberMock.getDisplayedName()).willReturn(displayedName);

        // when
        ResultActions result = mockMvc.perform(get("/ucp/profile/edit"));

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("ucp/profile/edit"));

        EditProfileForm editProfileForm = (EditProfileForm) result.andReturn()
                .getModelAndView().getModel().get("editProfileForm");

        assertThat(editProfileForm.getDisplayedName()).isEqualTo("foo");
    }

    @Test
    @WithMockUser(username = "any", roles = {})
    public void shouldThrowUserNotFoundException_whenGetInvokedForNotExistUser() throws Exception {
        // given
        given(memberServiceMock.getMemberWithUsername(any())).willReturn(Optional.empty());

        // when
        ResultActions result = mockMvc.perform(get("/ucp/profile/edit"));

        // then
        result.andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "any", roles = {})
    public void shouldUpdateDisplayedName_whenPost() throws Exception {
        // given
        EditProfileForm form = new EditProfileForm();
        form.setDisplayedName("bar");

        given(permissionServiceMock.checkPermission(any())).willReturn(true);

        // when
        ResultActions result = mockMvc.perform(post("/ucp/profile/edit")
                        .requestAttr("editProfileForm", form)
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("ucp/profile/edit"))
                .andExpect(model().attribute("editProfileFormSaved", true));

        verify(memberServiceMock, times(1)).updateProfile(any(), any());
    }

    @Test
    @WithMockUser(username = "any", roles = {})
    public void shouldNotSetFlag_whenSomeErrorHappenedDuringMemberServiceInvocation() throws Exception {
        // given
        EditProfileForm form = new EditProfileForm();
        form.setDisplayedName("bar");

        ProfileException profileException = mock(ProfileException.class);
        ConstraintViolation constraintViolation = mock(ConstraintViolation.class);
        given(constraintViolation.getMessage()).willReturn("foo");
        given(profileException.getConstraintViolations()).willReturn(Sets.newHashSet(constraintViolation));

        doThrow(profileException).when(memberServiceMock).updateProfile(any(), any());
        given(permissionServiceMock.checkPermission(any())).willReturn(true);

        // when
        ResultActions result = mockMvc.perform(post("/ucp/profile/edit")
                        .requestAttr("editProfileForm", form)
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("ucp/profile/edit"))
                .andExpect(model().attribute("editProfileFormSaved", false));
    }
}