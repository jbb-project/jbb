/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.web;

import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.lib.mvc.properties.MvcProperties;
import org.jbb.members.api.service.MemberService;
import org.jbb.security.api.service.MemberLockoutService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.text.SimpleDateFormat;

import static org.mockito.Mockito.when;

@Configuration
public class SecurityConfigMock {
    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        return Mockito.mock(UserDetailsService.class);
    }

    @Bean
    @Primary
    public AuthenticationProvider authenticationProvider() {
        return Mockito.mock(AuthenticationProvider.class);
    }

    @Bean
    @Primary
    public JbbEventBus jbbEventBus() {
        return Mockito.mock(JbbEventBus.class);
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
    public MemberService memberService() {
        return Mockito.mock(MemberService.class);
    }

    @Bean
    @Primary
    public MemberLockoutService userLockService() {
        return Mockito.mock(MemberLockoutService.class);
    }
}
