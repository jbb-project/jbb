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
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.google.common.collect.Sets;
import java.util.Optional;
import javax.validation.ConstraintViolation;
import javax.validation.Path;
import org.jbb.lib.commons.vo.Email;
import org.jbb.lib.commons.vo.Username;
import org.jbb.members.api.base.AccountException;
import org.jbb.members.api.base.Member;
import org.jbb.members.api.base.MemberService;
import org.jbb.members.web.BaseIT;
import org.jbb.members.web.base.form.EditAccountForm;
import org.jbb.permissions.api.PermissionService;
import org.jbb.security.api.password.PasswordService;
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

public class UcpEditAccountControllerIT extends BaseIT {
    @Autowired
    WebApplicationContext wac;

    @Autowired
    private MemberService memberServiceMock;

    @Autowired
    private PasswordService passwordServiceMock;

    @Autowired
    private PermissionService permissionServiceMock;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(SecurityMockMvcConfigurers.springSecurity()).build();
        Mockito.reset(memberServiceMock);
        Mockito.reset(passwordServiceMock);
    }

    @Test
    @WithMockUser(username = "any", roles = {})
    public void shouldPutCurrentUsernameAndEmailToForm_whenGet() throws Exception {
        // given
        Member memberMock = mock(Member.class);
        given(memberServiceMock.getMemberWithUsername(any())).willReturn(Optional.of(memberMock));

        Username username = Username.builder().value("any").build();
        given(memberMock.getUsername()).willReturn(username);

        Email email = Email.builder().value("foo@bar.com").build();
        given(memberMock.getEmail()).willReturn(email);

        // when
        ResultActions result = mockMvc.perform(get("/ucp/profile/editAccount"));

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("ucp/profile/editAccount"));

        EditAccountForm editAccountForm = (EditAccountForm) result.andReturn()
                .getModelAndView().getModel().get("editAccountForm");

        assertThat(editAccountForm.getUsername()).isEqualTo("any");
        assertThat(editAccountForm.getEmail()).isEqualTo("foo@bar.com");
    }

    @Test
    @WithMockUser(username = "any", roles = {})
    public void shouldThrowUserNotFoundException_whenGetInvokedForNotExistUser() throws Exception {
        // given
        given(memberServiceMock.getMemberWithUsername(any())).willReturn(Optional.empty());

        // when
        ResultActions result = mockMvc.perform(get("/ucp/profile/editAccount"));

        // then
        result.andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "any", roles = {})
    public void shouldNotSetSaveFlag_whenCurrentPasswordIsNotCorrect() throws Exception {
        // given
        Member memberMock = mock(Member.class);
        given(memberServiceMock.getMemberWithUsername(any())).willReturn(Optional.of(memberMock));

        Username username = Username.builder().value("any").build();
        given(memberMock.getUsername()).willReturn(username);

        EditAccountForm form = new EditAccountForm();
        form.setCurrentPassword("incorrectPassword");
        given(passwordServiceMock.verifyFor(any(), any())).willReturn(false);


        // when
        ResultActions result = mockMvc.perform(post("/ucp/profile/editAccount")
                .param("currentPassword", form.getCurrentPassword())
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("ucp/profile/editAccount"))
                .andExpect(model().attribute("editAccountFormSaved", false));
    }

    @Test
    @WithMockUser(username = "any", roles = {})
    public void shouldNotSetSaveFlag_whenNewPasswordsAreNotTheSame() throws Exception {
        // given
        Member memberMock = mock(Member.class);
        given(memberServiceMock.getMemberWithUsername(any())).willReturn(Optional.of(memberMock));

        Username username = Username.builder().value("any").build();
        given(memberMock.getUsername()).willReturn(username);
        given(memberMock.getEmail()).willReturn(Email.builder().value("old@email.com").build());

        EditAccountForm form = new EditAccountForm();
        form.setCurrentPassword("incorrectPassword");
        form.setEmail("new@email.com");
        form.setNewPassword("");
        form.setNewPasswordAgain("newPassword");
        given(passwordServiceMock.verifyFor(any(), any())).willReturn(true);
        given(permissionServiceMock.checkPermission(any())).willReturn(true);


        // when
        ResultActions result = mockMvc.perform(post("/ucp/profile/editAccount")
                .param("currentPassword", form.getCurrentPassword())
                .param("email", form.getEmail())
                .param("newPassword", form.getNewPassword())
                .param("newPasswordAgain", form.getNewPasswordAgain())
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("ucp/profile/editAccount"))
                .andExpect(model().attribute("editAccountFormSaved", false));
    }

    @Test
    @WithMockUser(username = "any", roles = {})
    public void shouldNotSetSaveFlag_whenErrorHappenDuringInvokingService() throws Exception {
        // given
        Member memberMock = mock(Member.class);
        given(memberServiceMock.getMemberWithUsername(any())).willReturn(Optional.of(memberMock));

        Username username = Username.builder().value("any").build();
        given(memberMock.getUsername()).willReturn(username);
        given(memberMock.getEmail()).willReturn(Email.builder().value("old@email.com").build());

        EditAccountForm form = new EditAccountForm();
        form.setCurrentPassword("incorrectPassword");
        form.setEmail("new@email.com");
        form.setNewPassword("newPassword");
        form.setNewPasswordAgain("newPassword");
        given(passwordServiceMock.verifyFor(any(), any())).willReturn(true);

        AccountException accountException = mock(AccountException.class);
        ConstraintViolation constraintViolation = mock(ConstraintViolation.class);
        given(constraintViolation.getMessage()).willReturn("foo");
        Path propertyPathMock = mock(Path.class);
        given(propertyPathMock.toString()).willReturn("email");
        given(constraintViolation.getPropertyPath()).willReturn(propertyPathMock);
        given(accountException.getConstraintViolations()).willReturn(Sets.newHashSet(constraintViolation));
        given(permissionServiceMock.checkPermission(any())).willReturn(true);

        doThrow(accountException).when(memberServiceMock).updateAccount(any(), any());


        // when
        ResultActions result = mockMvc.perform(post("/ucp/profile/editAccount")
                .param("currentPassword", form.getCurrentPassword())
                .param("email", form.getEmail())
                .param("newPassword", form.getNewPassword())
                .param("newPasswordAgain", form.getNewPasswordAgain())
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("ucp/profile/editAccount"))
                .andExpect(model().attribute("editAccountFormSaved", false));
    }

    @Test
    @WithMockUser(username = "any", roles = {})
    public void shouldNotSetSaveFlag_whenEverythingIsOk() throws Exception {
        // given
        Member memberMock = mock(Member.class);
        given(memberServiceMock.getMemberWithUsername(any())).willReturn(Optional.of(memberMock));

        Username username = Username.builder().value("any").build();
        given(memberMock.getUsername()).willReturn(username);
        given(memberMock.getEmail()).willReturn(Email.builder().value("old@email.com").build());

        EditAccountForm form = new EditAccountForm();
        form.setCurrentPassword("incorrectPassword");
        form.setEmail("new@email.com");
        form.setNewPassword("newPassword");
        form.setNewPasswordAgain("newPassword");
        given(passwordServiceMock.verifyFor(any(), any())).willReturn(true);
        given(permissionServiceMock.checkPermission(any())).willReturn(true);

        // when
        ResultActions result = mockMvc.perform(post("/ucp/profile/editAccount")
                .param("currentPassword", form.getCurrentPassword())
                .param("email", form.getEmail())
                .param("newPassword", form.getNewPassword())
                .param("newPasswordAgain", form.getNewPasswordAgain())
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("ucp/profile/editAccount"))
                .andExpect(model().attribute("editAccountFormSaved", true));

        verify(memberServiceMock).updateAccount(any(), any());
    }
}