/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.web;

import org.jbb.lib.mvc.properties.MvcProperties;
import org.jbb.lib.mvc.security.SecurityContextHelper;
import org.jbb.members.api.service.MemberService;
import org.jbb.members.api.service.RegistrationService;
import org.jbb.security.api.service.MemberLockoutService;
import org.jbb.security.api.service.PasswordService;
import org.jbb.security.api.service.RoleService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.text.SimpleDateFormat;

import static org.mockito.Mockito.when;

@Configuration
public class MembersConfigMock {
    @Bean
    @Primary
    public RegistrationService registrationService() {
        return Mockito.mock(RegistrationService.class);
    }

    @Bean
    @Primary
    public MemberService memberService() {
        return Mockito.mock(MemberService.class);
    }

    @Bean
    @Primary
    public PasswordService passwordService() {
        return Mockito.mock(PasswordService.class);
    }

    @Bean
    @Primary
    public RoleService roleService() {
        return Mockito.mock(RoleService.class);
    }

    @Bean
    @Primary
    public MemberLockoutService userLockService() {
        return Mockito.mock(MemberLockoutService.class);
    }

    @Bean
    @Primary
    public MvcProperties mvcProperties() {
        MvcProperties prop = Mockito.mock(MvcProperties.class);
        when(prop.localDateTimeFormatPattern()).thenReturn(new SimpleDateFormat().toLocalizedPattern());
        return prop;
    }

    @Bean
    @Primary
    public SecurityContextHelper securityContextHelper() {
        return Mockito.mock(SecurityContextHelper.class);
    }
}
