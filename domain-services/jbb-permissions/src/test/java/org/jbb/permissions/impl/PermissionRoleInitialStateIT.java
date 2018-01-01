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

import com.google.common.collect.Lists;

import org.jbb.permissions.api.identity.AdministratorGroupIdentity;
import org.jbb.permissions.api.identity.AnonymousIdentity;
import org.jbb.permissions.api.identity.RegisteredMembersIdentity;
import org.jbb.permissions.api.permission.domain.AdministratorPermissions;
import org.jbb.permissions.api.permission.domain.MemberPermissions;
import org.jbb.permissions.impl.role.dao.AclActiveRoleRepository;
import org.jbb.permissions.impl.role.dao.AclRoleEntryRepository;
import org.jbb.permissions.impl.role.dao.AclRoleRepository;
import org.jbb.permissions.impl.role.install.predefined.JuniorAdministratorRole;
import org.jbb.permissions.impl.role.install.predefined.StandardAdministratorRole;
import org.jbb.permissions.impl.role.install.predefined.StandardAnonymousRole;
import org.jbb.permissions.impl.role.install.predefined.StandardMemberRole;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class PermissionRoleInitialStateIT extends BaseIT {

    @Autowired
    AclRoleRepository aclRoleRepository;

    @Autowired
    AclRoleEntryRepository aclRoleEntryRepository;

    @Autowired
    AclActiveRoleRepository aclActiveRoleRepository;

    @Test
    public void shouldSaveAllDefaultRoles() {
        assertThat(aclRoleRepository.count()).isEqualTo(Lists.newArrayList(
                StandardMemberRole.class,
                StandardAnonymousRole.class,
                StandardAdministratorRole.class,
                JuniorAdministratorRole.class
        ).size());
    }

    @Test
    public void shouldSaveRoleEntriesForAllDefaultRoles() {
        assertThat(aclRoleEntryRepository.count()).isEqualTo(
                2 * AdministratorPermissions.values().length + 2 * MemberPermissions.values().length
        );
    }

    @Test
    public void shouldSetActiveRoles() {
        assertThat(aclActiveRoleRepository.count()).isEqualTo(Lists.newArrayList(
                RegisteredMembersIdentity.getInstance(),
                AnonymousIdentity.getInstance(),
                AdministratorGroupIdentity.getInstance()
        ).size());
    }
}
