/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.stacktrace;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import java.util.Optional;
import org.jbb.lib.commons.security.SecurityContentUser;
import org.jbb.lib.commons.security.UserDetailsSource;
import org.jbb.system.impl.BaseIT;
import org.jbb.system.impl.SystemProperties;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithMockUser;

public class StackTraceServiceIT extends BaseIT {

    @Autowired
    private DefaultStackTraceService stackTraceVisibilityUsersService;

    @Autowired
    private SystemProperties systemProperties;

    @Autowired
    private UserDetailsSource userDetailsSourceMock;

    @Before
    public void setUp() throws Exception {
        Mockito.reset(userDetailsSourceMock);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullExceptionHandled() {
        // when
        stackTraceVisibilityUsersService.getStackTraceAsString(null);

        // then
        // throw NullPointerException
    }

    @Test
    @WithMockUser(username = "any", roles = {"ANONYMOUS"})
    public void shouldReturnStack_whenPropertyIsSetToEverybody_andUserIsNotAuthenticated() {
        // given
        systemProperties.setProperty(SystemProperties.STACK_TRACE_VISIBILITY_LEVEL_KEY, "everybody");

        // when
        Optional<String> stack = stackTraceVisibilityUsersService.getStackTraceAsString(new Exception());

        // then
        assertThat(stack).isPresent();
        assertThat(stack.get()).isNotEmpty();
    }

    @Test
    @WithMockUser(username = "any", roles = {})
    public void shouldReturnStack_whenPropertyIsSetToEverybody_andUserIsAuthenticated() {
        // given
        systemProperties.setProperty(SystemProperties.STACK_TRACE_VISIBILITY_LEVEL_KEY, "everybody");

        // when
        Optional<String> stack = stackTraceVisibilityUsersService.getStackTraceAsString(new Exception());

        // then
        assertThat(stack).isPresent();
        assertThat(stack.get()).isNotEmpty();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    public void shouldReturnStack_whenPropertyIsSetToEverybody_andUserIsAdministratorAuthenticated() {
        // given
        systemProperties.setProperty(SystemProperties.STACK_TRACE_VISIBILITY_LEVEL_KEY, "everybody");

        // when
        Optional<String> stack = stackTraceVisibilityUsersService.getStackTraceAsString(new Exception());

        // then
        assertThat(stack).isPresent();
        assertThat(stack.get()).isNotEmpty();
    }

    @Test
    public void shouldNotReturnStack_whenPropertyIsSetToUsers_andUserIsNotAuthenticated() {
        // given
        systemProperties.setProperty(SystemProperties.STACK_TRACE_VISIBILITY_LEVEL_KEY, "users");

        // when
        Optional<String> stack = stackTraceVisibilityUsersService.getStackTraceAsString(new Exception());

        // then
        assertThat(stack).isEmpty();
    }

    @Test
    @WithMockUser(username = "any", roles = {})
    public void shouldReturnStack_whenPropertyIsSetToUsers_andUserIsAuthenticated() {
        // given
        systemProperties.setProperty(SystemProperties.STACK_TRACE_VISIBILITY_LEVEL_KEY, "users");

        SecurityContentUser user = new SecurityContentUser(
            new User("any", "pass", Lists.newArrayList()), "Any", 2L);
        when(userDetailsSourceMock.getFromApplicationContext()).thenReturn(user);

        // when
        Optional<String> stack = stackTraceVisibilityUsersService.getStackTraceAsString(new Exception());

        // then
        assertThat(stack).isPresent();
        assertThat(stack.get()).isNotEmpty();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    public void shouldReturnStack_whenPropertyIsSetToUsers_andUserIsAdministratorAuthenticated() {
        // given
        systemProperties.setProperty(SystemProperties.STACK_TRACE_VISIBILITY_LEVEL_KEY, "users");

        SecurityContentUser user = new SecurityContentUser(
            new User("admin", "pass",
                Lists.newArrayList(new SimpleGrantedAuthority("ROLE_ADMINISTRATOR"))), "Any", 2L);
        when(userDetailsSourceMock.getFromApplicationContext()).thenReturn(user);

        // when
        Optional<String> stack = stackTraceVisibilityUsersService.getStackTraceAsString(new Exception());

        // then
        assertThat(stack).isPresent();
        assertThat(stack.get()).isNotEmpty();
    }

    @Test
    public void shouldNotReturnStack_whenPropertyIsSetToAdministrators_andUserIsNotAuthenticated() {
        // given
        systemProperties.setProperty(SystemProperties.STACK_TRACE_VISIBILITY_LEVEL_KEY, "administrators");

        // when
        Optional<String> stack = stackTraceVisibilityUsersService.getStackTraceAsString(new Exception());

        // then
        assertThat(stack).isEmpty();
    }

    @Test
    @WithMockUser(username = "any", roles = {})
    public void shouldNotReturnStack_whenPropertyIsSetToAdministrators_andUserIsAuthenticated() {
        // given
        systemProperties.setProperty(SystemProperties.STACK_TRACE_VISIBILITY_LEVEL_KEY, "administrators");

        // when
        Optional<String> stack = stackTraceVisibilityUsersService.getStackTraceAsString(new Exception());

        // then
        assertThat(stack).isEmpty();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    public void shouldReturnStack_whenPropertyIsSetToAdministrators_andUserIsAdministratorAuthenticated() {
        // given
        systemProperties.setProperty(SystemProperties.STACK_TRACE_VISIBILITY_LEVEL_KEY, "administrators");

        SecurityContentUser user = new SecurityContentUser(
            new User("admin", "pass",
                Lists.newArrayList(new SimpleGrantedAuthority("ROLE_ADMINISTRATOR"))), "Any", 2L);
        when(userDetailsSourceMock.getFromApplicationContext()).thenReturn(user);

        // when
        Optional<String> stack = stackTraceVisibilityUsersService.getStackTraceAsString(new Exception());

        // then
        assertThat(stack).isPresent();
        assertThat(stack.get()).isNotEmpty();
    }

    @Test
    public void shouldNotReturnStack_whenPropertyIsSetToNobody_andUserIsNotAuthenticated() {
        // given
        systemProperties.setProperty(SystemProperties.STACK_TRACE_VISIBILITY_LEVEL_KEY, "nobody");

        // when
        Optional<String> stack = stackTraceVisibilityUsersService.getStackTraceAsString(new Exception());

        // then
        assertThat(stack).isEmpty();
    }

    @Test
    @WithMockUser(username = "any", roles = {})
    public void shouldNotReturnStack_whenPropertyIsSetToNobody_andUserIsAuthenticated() {
        // given
        systemProperties.setProperty(SystemProperties.STACK_TRACE_VISIBILITY_LEVEL_KEY, "nobody");

        // when
        Optional<String> stack = stackTraceVisibilityUsersService.getStackTraceAsString(new Exception());

        // then
        assertThat(stack).isEmpty();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    public void shouldNotReturnStack_whenPropertyIsSetToNobody_andUserIsAdministratorAuthenticated() {
        // given
        systemProperties.setProperty(SystemProperties.STACK_TRACE_VISIBILITY_LEVEL_KEY, "nobody");

        // when
        Optional<String> stack = stackTraceVisibilityUsersService.getStackTraceAsString(new Exception());

        // then
        assertThat(stack).isEmpty();
    }
}
