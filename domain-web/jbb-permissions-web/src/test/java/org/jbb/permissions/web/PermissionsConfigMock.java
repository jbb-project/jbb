/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.web;

import org.jbb.permissions.api.PermissionMatrixService;
import org.jbb.permissions.api.PermissionRoleService;
import org.jbb.permissions.api.PermissionService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class PermissionsConfigMock {

    @Bean
    @Primary
    public PermissionService permissionService() {
        return Mockito.mock(PermissionService.class);
    }

    @Bean
    @Primary
    public PermissionRoleService permissionRoleService() {
        return Mockito.mock(PermissionRoleService.class);
    }

    @Bean
    @Primary
    public PermissionMatrixService permissionMatrixService() {
        return Mockito.mock(PermissionMatrixService.class);
    }
}
