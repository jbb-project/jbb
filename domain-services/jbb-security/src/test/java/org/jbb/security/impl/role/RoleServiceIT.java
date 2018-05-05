/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.role;

import static org.assertj.core.api.Assertions.assertThat;

import org.jbb.security.api.role.RoleService;
import org.jbb.security.impl.BaseIT;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class RoleServiceIT extends BaseIT {
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