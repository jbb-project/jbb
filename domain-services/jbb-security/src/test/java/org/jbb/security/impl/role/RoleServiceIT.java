/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.role;

import static org.assertj.core.api.Assertions.assertThat;

import org.jbb.lib.commons.CommonsConfig;
import org.jbb.lib.db.DbConfig;
import org.jbb.lib.eventbus.EventBusConfig;
import org.jbb.lib.properties.PropertiesConfig;
import org.jbb.lib.test.MockCommonsConfig;
import org.jbb.security.api.role.RoleService;
import org.jbb.security.impl.MemberConfigMocks;
import org.jbb.security.impl.SecurityConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CommonsConfig.class, MockCommonsConfig.class,
        SecurityConfig.class, PropertiesConfig.class,
        EventBusConfig.class, DbConfig.class, MemberConfigMocks.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class RoleServiceIT {
    @Autowired
    private RoleService roleService;

    @Test
    public void shouldPassAdministratorRoleVerification_afterAddingRoleForMember() throws Exception {
        // given
        Long memberId = 673L;

        // when
        boolean firstVerification = roleService.hasAdministratorRole(memberId);
        roleService.addAdministratorRole(memberId);
        boolean secondVerification = roleService.hasAdministratorRole(memberId);
        boolean firstRemovingStatus = roleService.removeAdministratorRole(memberId);
        boolean thirdVerification = roleService.hasAdministratorRole(memberId);
        boolean secondRemovingStatus = roleService.removeAdministratorRole(memberId);

        // then
        assertThat(firstVerification).isFalse();
        assertThat(secondVerification).isTrue();
        assertThat(thirdVerification).isFalse();

        assertThat(firstRemovingStatus).isTrue();
        assertThat(secondRemovingStatus).isFalse();
    }
}