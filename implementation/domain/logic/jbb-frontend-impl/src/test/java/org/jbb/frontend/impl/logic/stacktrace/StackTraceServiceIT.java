/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.logic.stacktrace;

import org.jbb.frontend.impl.FrontendConfig;
import org.jbb.frontend.impl.base.properties.FrontendProperties;
import org.jbb.frontend.impl.stacktrace.logic.StackTraceServiceImpl;
import org.jbb.lib.properties.PropertiesConfig;
import org.jbb.lib.test.CoreConfigMocks;
import org.jbb.lib.test.SpringSecurityConfigMocks;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {FrontendConfig.class, PropertiesConfig.class, CoreConfigMocks.class, SpringSecurityConfigMocks.class})
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class,
        WithSecurityContextTestExecutionListener.class})
public class StackTraceServiceIT {

    @Autowired
    private StackTraceServiceImpl stackTraceVisibilityUsersService;

    @Autowired
    private FrontendProperties frontendProperties;

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullExceptionHandled() throws Exception {
        // when
        stackTraceVisibilityUsersService.getStackTraceAsString(null);

        // then
        // throw NullPointerException
    }

    @Test
    public void shouldReturnStack_whenPropertyIsSetToEverybody_andUserIsNotAuthenticated() throws Exception {
        // given
        frontendProperties.setProperty(FrontendProperties.STACK_TRACE_VISIBILITY_LEVEL_KEY, "everybody");

        // when
        Optional<String> stack = stackTraceVisibilityUsersService.getStackTraceAsString(new Exception());

        // then
        assertThat(stack).isPresent();
        assertThat(stack.get()).isNotEmpty();
    }

    @Test
    @WithMockUser(username = "any", roles = {})
    public void shouldReturnStack_whenPropertyIsSetToEverybody_andUserIsAuthenticated() throws Exception {
        // given
        frontendProperties.setProperty(FrontendProperties.STACK_TRACE_VISIBILITY_LEVEL_KEY, "everybody");

        // when
        Optional<String> stack = stackTraceVisibilityUsersService.getStackTraceAsString(new Exception());

        // then
        assertThat(stack).isPresent();
        assertThat(stack.get()).isNotEmpty();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    public void shouldReturnStack_whenPropertyIsSetToEverybody_andUserIsAdministratorAuthenticated() throws Exception {
        // given
        frontendProperties.setProperty(FrontendProperties.STACK_TRACE_VISIBILITY_LEVEL_KEY, "everybody");

        // when
        Optional<String> stack = stackTraceVisibilityUsersService.getStackTraceAsString(new Exception());

        // then
        assertThat(stack).isPresent();
        assertThat(stack.get()).isNotEmpty();
    }

    @Test
    public void shouldNotReturnStack_whenPropertyIsSetToUsers_andUserIsNotAuthenticated() throws Exception {
        // given
        frontendProperties.setProperty(FrontendProperties.STACK_TRACE_VISIBILITY_LEVEL_KEY, "users");

        // when
        Optional<String> stack = stackTraceVisibilityUsersService.getStackTraceAsString(new Exception());

        // then
        assertThat(stack).isEmpty();
    }

    @Test
    @WithMockUser(username = "any", roles = {})
    public void shouldReturnStack_whenPropertyIsSetToUsers_andUserIsAuthenticated() throws Exception {
        // given
        frontendProperties.setProperty(FrontendProperties.STACK_TRACE_VISIBILITY_LEVEL_KEY, "users");

        // when
        Optional<String> stack = stackTraceVisibilityUsersService.getStackTraceAsString(new Exception());

        // then
        assertThat(stack).isPresent();
        assertThat(stack.get()).isNotEmpty();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    public void shouldReturnStack_whenPropertyIsSetToUsers_andUserIsAdministratorAuthenticated() throws Exception {
        // given
        frontendProperties.setProperty(FrontendProperties.STACK_TRACE_VISIBILITY_LEVEL_KEY, "users");

        // when
        Optional<String> stack = stackTraceVisibilityUsersService.getStackTraceAsString(new Exception());

        // then
        assertThat(stack).isPresent();
        assertThat(stack.get()).isNotEmpty();
    }

    @Test
    public void shouldNotReturnStack_whenPropertyIsSetToAdministrators_andUserIsNotAuthenticated() throws Exception {
        // given
        frontendProperties.setProperty(FrontendProperties.STACK_TRACE_VISIBILITY_LEVEL_KEY, "administrators");

        // when
        Optional<String> stack = stackTraceVisibilityUsersService.getStackTraceAsString(new Exception());

        // then
        assertThat(stack).isEmpty();
    }

    @Test
    @WithMockUser(username = "any", roles = {})
    public void shouldNotReturnStack_whenPropertyIsSetToAdministrators_andUserIsAuthenticated() throws Exception {
        // given
        frontendProperties.setProperty(FrontendProperties.STACK_TRACE_VISIBILITY_LEVEL_KEY, "administrators");

        // when
        Optional<String> stack = stackTraceVisibilityUsersService.getStackTraceAsString(new Exception());

        // then
        assertThat(stack).isEmpty();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    public void shouldReturnStack_whenPropertyIsSetToAdministrators_andUserIsAdministratorAuthenticated() throws Exception {
        // given
        frontendProperties.setProperty(FrontendProperties.STACK_TRACE_VISIBILITY_LEVEL_KEY, "administrators");

        // when
        Optional<String> stack = stackTraceVisibilityUsersService.getStackTraceAsString(new Exception());

        // then
        assertThat(stack).isPresent();
        assertThat(stack.get()).isNotEmpty();
    }

    @Test
    public void shouldNotReturnStack_whenPropertyIsSetToNobody_andUserIsNotAuthenticated() throws Exception {
        // given
        frontendProperties.setProperty(FrontendProperties.STACK_TRACE_VISIBILITY_LEVEL_KEY, "nobody");

        // when
        Optional<String> stack = stackTraceVisibilityUsersService.getStackTraceAsString(new Exception());

        // then
        assertThat(stack).isEmpty();
    }

    @Test
    @WithMockUser(username = "any", roles = {})
    public void shouldNotReturnStack_whenPropertyIsSetToNobody_andUserIsAuthenticated() throws Exception {
        // given
        frontendProperties.setProperty(FrontendProperties.STACK_TRACE_VISIBILITY_LEVEL_KEY, "nobody");

        // when
        Optional<String> stack = stackTraceVisibilityUsersService.getStackTraceAsString(new Exception());

        // then
        assertThat(stack).isEmpty();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    public void shouldNotReturnStack_whenPropertyIsSetToNobody_andUserIsAdministratorAuthenticated() throws Exception {
        // given
        frontendProperties.setProperty(FrontendProperties.STACK_TRACE_VISIBILITY_LEVEL_KEY, "nobody");

        // when
        Optional<String> stack = stackTraceVisibilityUsersService.getStackTraceAsString(new Exception());

        // then
        assertThat(stack).isEmpty();
    }
}
