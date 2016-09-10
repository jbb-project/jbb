/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.logic.stactrace.strategy;

import com.google.common.collect.Lists;

import org.jbb.frontend.impl.logic.stacktrace.strategy.AdminStackTraceVisibilityStrategy;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
@Ignore

public class AdminStackTraceVisibilityStrategyTest {

    private final static String ADMINISTRATOR_ROLE = "ADMINISTRATOR";
    @Mock
    private UserDetails principal;
    @InjectMocks
    private AdminStackTraceVisibilityStrategy adminStackTraceVisibilityStrategy;

    @Before
    public void init() {
        principal = Mockito.mock(UserDetails.class);
        defineMockBehaviour(principal);
    }

    @Test
    public void whenUserIsAdministratorAndFilePropertiesSetToAdministratorThenMethodShouldReturnTrue() {
//        //when
//
//        boolean canHandle = adminStackTraceVisibilityStrategy.canHandle(StackTraceVisibilityUsersValues.ADMINISTRATORS, principal);
//
//        //then
//        assertTrue(canHandle);
    }


    private void defineMockBehaviour(UserDetails principal) {
        List<GrantedAuthority> simpleGrantedAuthorities = Lists.newArrayList(new SimpleGrantedAuthority(ADMINISTRATOR_ROLE));

        doReturn(simpleGrantedAuthorities).when(principal.getAuthorities());
    }
}
