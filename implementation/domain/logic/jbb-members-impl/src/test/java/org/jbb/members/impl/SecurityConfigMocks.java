/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.impl;

import org.jbb.members.impl.base.logic.FirstMemberCreator;
import org.jbb.security.api.service.PasswordService;
import org.jbb.security.api.service.RoleService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class SecurityConfigMocks {
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
    public FirstMemberCreator firstMemberCreator() {
        return Mockito.mock(FirstMemberCreator.class);
    }
}
