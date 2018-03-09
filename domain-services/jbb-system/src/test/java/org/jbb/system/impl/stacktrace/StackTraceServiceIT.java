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

import org.jbb.system.impl.BaseIT;
import org.jbb.system.impl.SystemProperties;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class StackTraceServiceIT extends BaseIT {

    @Autowired
    private DefaultStackTraceService stackTraceVisibilityUsersService;

    @Autowired
    private SystemProperties systemProperties;

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
    @WithMockUser(username = "any", roles = {"ANONYMOUS"})
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
