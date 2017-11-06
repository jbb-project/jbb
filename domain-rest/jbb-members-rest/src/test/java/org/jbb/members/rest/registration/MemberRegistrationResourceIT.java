/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.rest.registration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import java.util.Optional;
import org.jbb.lib.commons.CommonsConfig;
import org.jbb.lib.commons.vo.Email;
import org.jbb.lib.commons.vo.Username;
import org.jbb.lib.mvc.MvcConfig;
import org.jbb.lib.properties.PropertiesConfig;
import org.jbb.lib.restful.RestConfig;
import org.jbb.lib.test.MockCommonsConfig;
import org.jbb.lib.test.MockSpringSecurityConfig;
import org.jbb.members.api.base.DisplayedName;
import org.jbb.members.api.base.Member;
import org.jbb.members.api.base.MemberService;
import org.jbb.members.rest.MemberMockConfig;
import org.jbb.members.rest.MembersRestConfig;
import org.jbb.members.rest.base.MemberPublicDto;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {CommonsConfig.class, MvcConfig.class, RestConfig.class,
    MembersRestConfig.class, PropertiesConfig.class,
    MemberMockConfig.class, MockCommonsConfig.class, MockSpringSecurityConfig.class})
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class,
    WithSecurityContextTestExecutionListener.class})
public class MemberRegistrationResourceIT {

    @Autowired
    WebApplicationContext wac;
    @Autowired
    private MemberService memberServiceMock;

    @Before
    public void setUp() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(wac)
            .apply(SecurityMockMvcConfigurers.springSecurity()).build();
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    @Ignore
    public void name() throws Exception {
        // given
        String username = "jacob";
        String email = "jacob@example.com";
        String displayedName = "Jacob";
        Long id = 200L;

        RegistrationRequestDto registrationRequest = RegistrationRequestDto.builder()
            .username(username)
            .displayedName(displayedName)
            .email(email)
            .password("password")
            .build();

        Member createdMember = mock(Member.class);
        given(createdMember.getId()).willReturn(id);
        given(createdMember.getUsername()).willReturn(Username.builder().value(username).build());
        given(createdMember.getEmail()).willReturn(Email.builder().value(email).build());
        given(createdMember.getDisplayedName())
            .willReturn(DisplayedName.builder().value(displayedName).build());
        given(memberServiceMock.getMemberWithUsername(any()))
            .willReturn(Optional.of(createdMember));

        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(registrationRequest);

        // when
        MockMvcResponse response = request.when().post("/api/v1/members");

        // then
        response.then().statusCode(201);
        MemberPublicDto resultBody = response.then().extract().body().as(MemberPublicDto.class);

        assertThat(resultBody.getId()).isEqualTo(id);
        assertThat(resultBody.getDisplayedName()).isEqualTo(displayedName);
    }
}