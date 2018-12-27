/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.rest;

import org.jbb.members.api.base.MemberService;
import org.jbb.security.api.lockout.LockoutSettingsService;
import org.jbb.security.api.lockout.MemberLockoutService;
import org.jbb.security.api.oauth.OAuthClientsService;
import org.jbb.security.api.password.PasswordService;
import org.jbb.security.api.privilege.PrivilegeService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MockSecurityRestConfig {

    @Bean
    @Primary
    public LockoutSettingsService lockoutSettingsService() {
        return Mockito.mock(LockoutSettingsService.class);
    }

    @Bean
    @Primary
    public MemberLockoutService memberLockoutService() {
        return Mockito.mock(MemberLockoutService.class);
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
    public PrivilegeService privilegeService() {
        return Mockito.mock(PrivilegeService.class);
    }

    @Bean
    @Primary
    public OAuthClientsService oAuthClientsService() {
        return Mockito.mock(OAuthClientsService.class);
    }

}
