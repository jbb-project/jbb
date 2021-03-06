/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.web;


import org.jbb.lib.mvc.security.SecurityContextHelper;
import org.jbb.members.api.base.MemberService;
import org.jbb.members.api.registration.RegistrationService;
import org.jbb.permissions.api.PermissionService;
import org.jbb.security.api.lockout.MemberLockoutService;
import org.jbb.security.api.password.PasswordService;
import org.jbb.security.api.privilege.PrivilegeService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MockMembersWebConfig {
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
    public PrivilegeService roleService() {
        return Mockito.mock(PrivilegeService.class);
    }

    @Bean
    @Primary
    public MemberLockoutService userLockService() {
        return Mockito.mock(MemberLockoutService.class);
    }

    @Bean
    @Primary
    public SecurityContextHelper securityContextHelper() {
        return Mockito.mock(SecurityContextHelper.class);
    }

    @Bean
    @Primary
    public PermissionService permissionService() {
        return Mockito.mock(PermissionService.class);
    }
}
