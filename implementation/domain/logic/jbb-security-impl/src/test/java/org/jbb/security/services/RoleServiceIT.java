/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.services;

import org.jbb.lib.core.vo.Login;
import org.jbb.lib.db.DbConfig;
import org.jbb.lib.eventbus.EventBusConfig;
import org.jbb.lib.properties.PropertiesConfig;
import org.jbb.lib.test.CleanHsqlDbAfterTestsConfig;
import org.jbb.lib.test.CoreConfigMocks;
import org.jbb.security.MemberConfigMocks;
import org.jbb.security.SecurityConfig;
import org.jbb.security.api.services.RoleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CoreConfigMocks.class, CleanHsqlDbAfterTestsConfig.class,
        SecurityConfig.class, PropertiesConfig.class,
        EventBusConfig.class, DbConfig.class, MemberConfigMocks.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class RoleServiceIT {
    @Autowired
    private RoleService roleService;

    @Test
    public void shouldPassAdministratorRoleVerification_afterAddingRoleForMember() throws Exception {
        // given
        Login login = Login.builder().value("thomas").build();

        // when
        boolean firstVerification = roleService.hasAdministratorRole(login);
        roleService.addAdministratorRole(login);
        boolean secondVerification = roleService.hasAdministratorRole(login);

        // then
        assertThat(firstVerification).isFalse();
        assertThat(secondVerification).isTrue();
    }
}