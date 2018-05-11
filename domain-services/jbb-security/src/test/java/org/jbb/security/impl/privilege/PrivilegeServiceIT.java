/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.privilege;

import static org.assertj.core.api.Assertions.assertThat;

import org.jbb.security.api.privilege.PrivilegeService;
import org.jbb.security.impl.BaseIT;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PrivilegeServiceIT extends BaseIT {
    @Autowired
    private PrivilegeService privilegeService;

    @Test
    public void shouldPassAdministratorRoleVerification_afterAddingRoleForMember() throws Exception {
        // given
        Long memberId = 673L;

        // when
        boolean firstVerification = privilegeService.hasAdministratorPrivilege(memberId);
        privilegeService.addAdministratorPrivilege(memberId);
        boolean secondVerification = privilegeService.hasAdministratorPrivilege(memberId);
        boolean firstRemovingStatus = privilegeService.removeAdministratorPrivilege(memberId);
        boolean thirdVerification = privilegeService.hasAdministratorPrivilege(memberId);
        boolean secondRemovingStatus = privilegeService.removeAdministratorPrivilege(memberId);

        // then
        assertThat(firstVerification).isFalse();
        assertThat(secondVerification).isTrue();
        assertThat(thirdVerification).isFalse();

        assertThat(firstRemovingStatus).isTrue();
        assertThat(secondRemovingStatus).isFalse();
    }
}