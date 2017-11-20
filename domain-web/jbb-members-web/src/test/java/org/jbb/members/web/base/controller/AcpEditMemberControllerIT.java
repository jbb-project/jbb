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
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.google.common.collect.Sets;
import java.util.Optional;
import javax.validation.ConstraintViolation;
import org.jbb.lib.commons.vo.Email;
import org.jbb.lib.commons.vo.Username;
import org.jbb.members.api.base.AccountException;
import org.jbb.members.api.base.DisplayedName;
import org.jbb.members.api.base.Member;
import org.jbb.members.api.base.MemberService;
import org.jbb.members.api.base.ProfileException;
import org.jbb.members.web.BaseIT;
import org.jbb.members.web.base.form.EditMemberForm;
import org.jbb.security.api.lockout.MemberLockoutService;
import org.jbb.security.api.role.RoleService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;

public class AcpEditMemberControllerIT extends BaseIT {
    @Autowired
    WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    private MemberService memberServiceMock;

    @Autowired
    private RoleService roleServiceMock;

    @Autowired
    private MemberLockoutService memberLockoutServiceMock;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(SecurityMockMvcConfigurers.springSecurity()).build();
        Mockito.reset(memberServiceMock, roleServiceMock);
    }

    @Test
    public void shouldThrowIAE_whenChooseNotExistingMemberToEdit_whenGET() throws Exception {
        // given
        given(memberServiceMock.getMemberWithId(eq(123L))).willReturn(Optional.empty());

        try {
            // when
            mockMvc.perform(get("/acp/members/edit")
                    .param("id", "123"));
        } catch (NestedServletException e) {
            // then
            assertThat(e.getCause()).isInstanceOf(IllegalArgumentException.class);
            return;
        }

        failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
    }

    @Test
    public void shouldStoreMemberInModel_whenGET() throws Exception {
        // given
        Member memberMock = mock(Member.class);
        given(memberServiceMock.getMemberWithId(eq(100L))).willReturn(Optional.of(memberMock));

        Username username = Username.builder().value("john").build();
        DisplayedName displayedName = DisplayedName.builder().value("John").build();
        Email email = Email.builder().value("john@john.com").build();
        Long memberId = 100L;

        given(memberMock.getUsername()).willReturn(username);
        given(memberMock.getDisplayedName()).willReturn(displayedName);
        given(memberMock.getEmail()).willReturn(email);
        given(memberMock.getId()).willReturn(memberId);

        given(memberLockoutServiceMock.getMemberLock(any())).willReturn(Optional.empty());

        // when
        ResultActions result = mockMvc.perform(get("/acp/members/edit")
                .param("id", "100"));

        // then
        result.andExpect(view().name("acp/members/edit"));

        EditMemberForm form = (EditMemberForm) result.andReturn()
                .getModelAndView().getModel().get("editMemberForm");

        assertThat(form.getId()).isEqualTo(100L);
        assertThat(form.getUsername()).isEqualTo(username.toString());
        assertThat(form.getDisplayedName()).isEqualTo(displayedName.toString());
        assertThat(form.getEmail()).isEqualTo(email.toString());
    }

    @Test
    public void shouldThrowIAE_whenChooseNotExistingMemberToEdit_whenPOST() throws Exception {
        // given
        given(memberServiceMock.getMemberWithId(eq(123L))).willReturn(Optional.empty());

        try {
            // when
            mockMvc.perform(post("/acp/members/edit")
                    .param("id", "123"));
        } catch (NestedServletException e) {
            // then
            assertThat(e.getCause()).isInstanceOf(IllegalArgumentException.class);
            return;
        }

        failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
    }

    @Test
    public void shouldSetFlag_whenChange_whenPOST() throws Exception {
        // given
        memberMockPrepare();

        // when
        ResultActions result = mockMvc.perform(post("/acp/members/edit")
                .param("id", "100")
                .param("username", "john")
                .param("displayedName", "Johnny")
                .param("email", "new@john.com")
                .param("newPassword", "newPass11")
                .param("newPasswordAgain", "newPass11")
                .param("hasAdminRole", "False"));

        // then
        result.andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/acp/members/edit"))
                .andExpect(flash().attribute("editMemberFormSent", true));
    }

    @Test
    public void shouldSetFlag_whenChangeWithAddingAdminRole_whenPOST() throws Exception {
        // given
        memberMockPrepare();

        // when
        ResultActions result = mockMvc.perform(post("/acp/members/edit")
                .param("id", "100")
                .param("username", "john")
                .param("displayedName", "Johnny")
                .param("email", "new@john.com")
                .param("newPassword", "newPass11")
                .param("newPasswordAgain", "newPass11")
                .param("hasAdminRole", "True"));

        // then
        result.andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/acp/members/edit"))
                .andExpect(flash().attribute("editMemberFormSent", true));
    }

    @Test
    public void shouldNotSetFlag_whenNewPasswordAreNotTheSame_whenPOST() throws Exception {
        // given
        memberMockPrepare();

        // when
        ResultActions result = mockMvc.perform(post("/acp/members/edit")
                .param("id", "100")
                .param("username", "john")
                .param("displayedName", "Johnny")
                .param("email", "new@john.com")
                .param("newPassword", "newPass11")
                .param("newPasswordAgain", "newPass12")
                .param("hasAdminRole", "False"));

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("acp/members/edit"))
                .andExpect(model().attribute("editMemberFormSent", false));
    }

    @Test
    public void shouldNotSetFlag_whenExceptionDuringUpdateAccount_whenPOST() throws Exception {
        // given
        memberMockPrepare();

        AccountException exceptionMock = mock(AccountException.class);
        willThrow(exceptionMock).given(memberServiceMock).updateAccount(any(), any());

        // when
        ResultActions result = mockMvc.perform(post("/acp/members/edit")
                .param("id", "100")
                .param("username", "john")
                .param("displayedName", "Johnny")
                .param("email", "new@john.com")
                .param("newPassword", "newPass11")
                .param("newPasswordAgain", "newPass11")
                .param("hasAdminRole", "False"));

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("acp/members/edit"))
                .andExpect(model().attribute("editMemberFormSent", false));
    }

    @Test
    public void shouldNotSetFlag_whenExceptionDuringUpdateProfile_whenPOST() throws Exception {
        // given
        memberMockPrepare();

        ProfileException exceptionMock = mock(ProfileException.class);

        ConstraintViolation violationMock = mock(ConstraintViolation.class);
        given(violationMock.getMessage()).willReturn("violation");
        given(exceptionMock.getConstraintViolations()).willReturn(Sets.newHashSet(violationMock));

        willThrow(exceptionMock).given(memberServiceMock).updateProfile(any(), any());

        // when
        ResultActions result = mockMvc.perform(post("/acp/members/edit")
                .param("id", "100")
                .param("username", "john")
                .param("displayedName", "Johnny")
                .param("email", "new@john.com")
                .param("newPassword", "newPass11")
                .param("newPasswordAgain", "newPass11")
                .param("hasAdminRole", "True"));

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("acp/members/edit"))
                .andExpect(model().attribute("editMemberFormSent", false));
    }

    private void memberMockPrepare() {
        Member memberMock = mock(Member.class);
        given(memberServiceMock.getMemberWithId(eq(100L))).willReturn(Optional.of(memberMock));

        Username username = Username.builder().value("john").build();
        DisplayedName displayedName = DisplayedName.builder().value("John").build();
        Email email = Email.builder().value("john@john.com").build();
        Long memberId = 100L;

        given(memberMock.getUsername()).willReturn(username);
        given(memberMock.getDisplayedName()).willReturn(displayedName);
        given(memberMock.getEmail()).willReturn(email);
        given(memberMock.getId()).willReturn(memberId);
    }
}