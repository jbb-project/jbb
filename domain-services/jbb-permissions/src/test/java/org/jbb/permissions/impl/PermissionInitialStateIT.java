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

import org.jbb.lib.commons.CommonsConfig;
import org.jbb.lib.db.DbConfig;
import org.jbb.lib.properties.PropertiesConfig;
import org.jbb.lib.test.MockCommonsConfig;
import org.jbb.permissions.api.identity.SecurityIdentity;
import org.jbb.permissions.api.permission.PermissionType;
import org.jbb.permissions.api.permission.domain.AdministratorPermissions;
import org.jbb.permissions.api.permission.domain.AllPermissionCategories;
import org.jbb.permissions.api.permission.domain.MemberPermissions;
import org.jbb.permissions.impl.acl.dao.AclPermissionCategoryRepository;
import org.jbb.permissions.impl.acl.dao.AclPermissionRepository;
import org.jbb.permissions.impl.acl.dao.AclPermissionTypeRepository;
import org.jbb.permissions.impl.acl.dao.AclSecurityIdentityRepository;
import org.jbb.permissions.impl.acl.dao.AclSecurityIdentityTypeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CommonsConfig.class, MockCommonsConfig.class,
    PropertiesConfig.class, DbConfig.class, PermissionsConfig.class})
public class PermissionInitialStateIT {

    @Autowired
    AclSecurityIdentityTypeRepository aclSecurityIdentityTypeRepository;

    @Autowired
    AclSecurityIdentityRepository aclSecurityIdentityRepository;


    @Autowired
    AclPermissionTypeRepository aclPermissionTypeRepository;

    @Autowired
    AclPermissionCategoryRepository aclPermissionCategoryRepository;

    @Autowired
    AclPermissionRepository aclPermissionRepository;

    @Test
    public void shouldSaveAllSecurityIdentities() throws Exception {
        assertThat(aclSecurityIdentityTypeRepository.count())
            .isEqualTo(SecurityIdentity.Type.values().length);
    }

    @Test
    public void shouldSaveAllPermissionTypes() throws Exception {
        assertThat(aclPermissionTypeRepository.count()).isEqualTo(PermissionType.values().length);
    }

    @Test
    public void shouldSaveAllPermissionCategories() throws Exception {
        assertThat(aclPermissionCategoryRepository.count())
            .isEqualTo(AllPermissionCategories.values().length);
    }

    @Test
    public void shouldSaveAllPermissions() throws Exception {
        assertThat(aclPermissionRepository.count())
            .isEqualTo(
                AdministratorPermissions.values().length + MemberPermissions.values().length);
    }
}
