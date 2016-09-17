/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.web.base.controller;

import com.google.common.collect.Lists;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.jbb.lib.core.vo.Email;
import org.jbb.members.api.data.DisplayedName;
import org.jbb.members.api.data.MemberRegistrationAware;
import org.jbb.members.api.data.RegistrationMetaData;
import org.jbb.members.api.service.MemberService;
import org.jbb.members.web.base.data.MemberBrowserRow;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MemberControllerTest {
    @Mock
    private MemberService memberServiceMock;

    @InjectMocks
    private MemberController memberController;

    @Test
    public void testName() throws Exception {
        // given
        Model modelMock = mock(Model.class);

        RegistrationMetaData registrationMetaDataMock = mock(RegistrationMetaData.class);
        given(registrationMetaDataMock.getJoinDateTime()).willReturn(LocalDateTime.of(2016, 9, 12, 7, 0, 0));

        MemberRegistrationAware memberRegistrationAwareMock = mock(MemberRegistrationAware.class);
        given(memberRegistrationAwareMock.getEmail()).willReturn(Email.builder().value("foo@bar.com").build());
        given(memberRegistrationAwareMock.getDisplayedName()).willReturn(DisplayedName.builder().value("John").build());
        given(memberRegistrationAwareMock.getRegistrationMetaData()).willReturn(registrationMetaDataMock);

        given(memberServiceMock.getAllMembersSortedByRegistrationDate()).willReturn(Lists.newArrayList(memberRegistrationAwareMock));

        // when
        String viewName = memberController.getMemberBrowser(modelMock);

        // then
        assertThat(viewName).isEqualTo("member_browser");

        verify(modelMock, times(1)).addAttribute(eq("memberRows"), Matchers.argThat(new BaseMatcher<List<MemberBrowserRow>>() {
            @Override
            public boolean matches(Object o) {
                List<MemberBrowserRow> list = (List<MemberBrowserRow>) o;
                MemberBrowserRow memberBrowserRow = list.get(0);
                assertThat(memberBrowserRow.getEmail()).isEqualTo(Email.builder().value("foo@bar.com").build());
                assertThat(memberBrowserRow.getDisplayedName()).isEqualTo(DisplayedName.builder().value("John").build());
                assertThat(memberBrowserRow.getJoinDateTime()).isEqualTo(LocalDateTime.of(2016, 9, 12, 7, 0, 0));
                return true;
            }

            @Override
            public void describeTo(Description description) {
            }
        }));
    }
}