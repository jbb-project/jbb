/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb;

import org.jbb.lib.restful.domain.ErrorInfo;
import org.jbb.lib.restful.error.ErrorResponse;
import org.jbb.lib.test.MockCommonsAutoInstallConfig;
import org.jbb.members.api.base.MemberService;
import org.jbb.members.api.registration.RegistrationService;
import org.jbb.webapp.DomainCompositeConfig;
import org.jbb.webapp.LibsCompositeConfig;
import org.jbb.webapp.RestCompositeConfig;
import org.jbb.webapp.WebCompositeConfig;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MockCommonsAutoInstallConfig.class,
        LibsCompositeConfig.class, DomainCompositeConfig.class,
        WebCompositeConfig.class, RestCompositeConfig.class})
@WebAppConfiguration
public abstract class BaseIT {

    protected MockMvc mvc;

    @Autowired
    protected ApplicationContext context;

    @Autowired
    WebApplicationContext wac;

    @Autowired
    RegistrationService registrationService;

    @Autowired
    MemberService memberService;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(springSecurity())
                .apply(sharedHttpSession()).build();
        RestAssuredMockMvc.mockMvc(mvc);
    }

    protected void assertErrorInfo(MockMvcResponse response, ErrorInfo errorInfo) {
        response.then().statusCode(errorInfo.getStatus().value());
        ErrorResponse resultBody = response.then().extract().body().as(ErrorResponse.class);
        assertThat(resultBody.getCode()).isEqualTo(errorInfo.getCode());
    }
}
