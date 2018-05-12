/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.impl;

import org.jbb.lib.commons.security.UserDetailsSource;
import org.jbb.members.api.base.MemberService;
import org.jbb.security.api.privilege.PrivilegeService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MockPermissionsConfig {

    @Bean
    @Primary
    public PrivilegeService roleService() {
        return Mockito.mock(PrivilegeService.class);
    }

    @Bean
    @Primary
    public UserDetailsSource userDetailsSource() {
        return Mockito.mock(UserDetailsSource.class);
    }

    @Bean
    @Primary
    public MemberService memberService() {
        return Mockito.mock(MemberService.class);
    }

}
