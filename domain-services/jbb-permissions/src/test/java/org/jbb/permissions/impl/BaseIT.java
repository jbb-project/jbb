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

import static org.mockito.BDDMockito.given;

import com.google.common.collect.Lists;
import java.util.Comparator;
import java.util.List;
import org.jbb.install.InstallUpdateAction;
import org.jbb.install.InstallationData;
import org.jbb.lib.test.MockCommonsConfig;
import org.jbb.members.api.base.MemberService;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PermissionsConfig.class, MockCommonsConfig.class,
    MockPermissionsConfig.class})
public abstract class BaseIT {

    public static boolean installed;

    @Autowired
    MemberService memberService;

    @Autowired
    List<InstallUpdateAction> installActions;

    @Before
    public void setUp() {
        given(memberService.getAllMembersSortedByRegistrationDate())
                .willReturn(Lists.newArrayList());
        if (!installed) {
            installActions.sort(Comparator.comparing(InstallUpdateAction::fromVersion));
            installActions.forEach(action -> action.install(InstallationData.builder().build()));
            installed = true;
        }
    }

}
