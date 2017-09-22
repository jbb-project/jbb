/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.impl;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.Lists;
import org.jbb.lib.commons.CommonsConfig;
import org.jbb.lib.db.DbConfig;
import org.jbb.lib.properties.PropertiesConfig;
import org.jbb.lib.test.MockCommonsConfig;
import org.jbb.permissions.api.permission.domain.AdministratorPermissions;
import org.jbb.permissions.api.permission.domain.MemberPermissions;
import org.jbb.permissions.impl.role.AclRoleInstallationAction.JuniorAdministrator;
import org.jbb.permissions.impl.role.AclRoleInstallationAction.StandardAdministrator;
import org.jbb.permissions.impl.role.AclRoleInstallationAction.StandardAnonymous;
import org.jbb.permissions.impl.role.AclRoleInstallationAction.StandardMember;
import org.jbb.permissions.impl.role.dao.AclRoleEntryRepository;
import org.jbb.permissions.impl.role.dao.AclRoleRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CommonsConfig.class, MockCommonsConfig.class,
    PropertiesConfig.class, DbConfig.class, PermissionsConfig.class})
public class PermissionRoleInitialStateIT {

    @Autowired
    AclRoleRepository aclRoleRepository;

    @Autowired
    AclRoleEntryRepository aclRoleEntryRepository;

    @Test
    public void shouldSaveAllDefaultRoles() throws Exception {
        assertThat(aclRoleRepository.count()).isEqualTo(Lists.newArrayList(
            StandardMember.class,
            StandardAnonymous.class,
            StandardAdministrator.class,
            JuniorAdministrator.class
        ).size());
    }

    @Test
    public void shouldSaveRoleEntriesForAllDefaultRoles() throws Exception {
        assertThat(aclRoleEntryRepository.count()).isEqualTo(
            2 * AdministratorPermissions.values().length + 2 * MemberPermissions.values().length
        );

    }
}