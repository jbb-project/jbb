/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.stacktrace;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.jbb.lib.commons.CommonsConfig;
import org.jbb.lib.db.DbConfig;
import org.jbb.lib.eventbus.EventBusConfig;
import org.jbb.lib.mvc.MvcConfig;
import org.jbb.lib.properties.PropertiesConfig;
import org.jbb.lib.test.MockCommonsConfig;
import org.jbb.lib.test.MockSpringSecurityConfig;
import org.jbb.system.impl.DbConfigMocks;
import org.jbb.system.impl.LoggingConfigMock;
import org.jbb.system.impl.SystemConfig;
import org.jbb.system.impl.SystemProperties;
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

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {CommonsConfig.class, SystemConfig.class, MvcConfig.class, LoggingConfigMock.class, PropertiesConfig.class, DbConfig.class, DbConfigMocks.class, EventBusConfig.class, MockCommonsConfig.class, MockSpringSecurityConfig.class})
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class,
        WithSecurityContextTestExecutionListener.class})
public class StackTraceServiceIT {

    @Autowired
    private DefaultStackTraceService stackTraceVisibilityUsersService;

    @Autowired
    private SystemProperties systemProperties;

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullExceptionHandled() throws Exception {
        // when
        stackTraceVisibilityUsersService.getStackTraceAsString(null);

        // then
        // throw NullPointerException
    }

    @Test
    @WithMockUser(username = "any", roles = {"ANONYMOUS"})
    public void shouldReturnStack_whenPropertyIsSetToEverybody_andUserIsNotAuthenticated() throws Exception {
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
    public void shouldReturnStack_whenPropertyIsSetToEverybody_andUserIsAuthenticated() throws Exception {
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
    public void shouldReturnStack_whenPropertyIsSetToEverybody_andUserIsAdministratorAuthenticated() throws Exception {
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
    public void shouldNotReturnStack_whenPropertyIsSetToUsers_andUserIsNotAuthenticated() throws Exception {
        // given
        systemProperties.setProperty(SystemProperties.STACK_TRACE_VISIBILITY_LEVEL_KEY, "users");

        // when
        Optional<String> stack = stackTraceVisibilityUsersService.getStackTraceAsString(new Exception());

        // then
        assertThat(stack).isEmpty();
    }

    @Test
    @WithMockUser(username = "any", roles = {})
    public void shouldReturnStack_whenPropertyIsSetToUsers_andUserIsAuthenticated() throws Exception {
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
    public void shouldReturnStack_whenPropertyIsSetToUsers_andUserIsAdministratorAuthenticated() throws Exception {
        // given
        systemProperties.setProperty(SystemProperties.STACK_TRACE_VISIBILITY_LEVEL_KEY, "users");

        // when
        Optional<String> stack = stackTraceVisibilityUsersService.getStackTraceAsString(new Exception());

        // then
        assertThat(stack).isPresent();
        assertThat(stack.get()).isNotEmpty();
    }

    @Test
    public void shouldNotReturnStack_whenPropertyIsSetToAdministrators_andUserIsNotAuthenticated() throws Exception {
        // given
        systemProperties.setProperty(SystemProperties.STACK_TRACE_VISIBILITY_LEVEL_KEY, "administrators");

        // when
        Optional<String> stack = stackTraceVisibilityUsersService.getStackTraceAsString(new Exception());

        // then
        assertThat(stack).isEmpty();
    }

    @Test
    @WithMockUser(username = "any", roles = {})
    public void shouldNotReturnStack_whenPropertyIsSetToAdministrators_andUserIsAuthenticated() throws Exception {
        // given
        systemProperties.setProperty(SystemProperties.STACK_TRACE_VISIBILITY_LEVEL_KEY, "administrators");

        // when
        Optional<String> stack = stackTraceVisibilityUsersService.getStackTraceAsString(new Exception());

        // then
        assertThat(stack).isEmpty();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    public void shouldReturnStack_whenPropertyIsSetToAdministrators_andUserIsAdministratorAuthenticated() throws Exception {
        // given
        systemProperties.setProperty(SystemProperties.STACK_TRACE_VISIBILITY_LEVEL_KEY, "administrators");

        // when
        Optional<String> stack = stackTraceVisibilityUsersService.getStackTraceAsString(new Exception());

        // then
        assertThat(stack).isPresent();
        assertThat(stack.get()).isNotEmpty();
    }

    @Test
    public void shouldNotReturnStack_whenPropertyIsSetToNobody_andUserIsNotAuthenticated() throws Exception {
        // given
        systemProperties.setProperty(SystemProperties.STACK_TRACE_VISIBILITY_LEVEL_KEY, "nobody");

        // when
        Optional<String> stack = stackTraceVisibilityUsersService.getStackTraceAsString(new Exception());

        // then
        assertThat(stack).isEmpty();
    }

    @Test
    @WithMockUser(username = "any", roles = {})
    public void shouldNotReturnStack_whenPropertyIsSetToNobody_andUserIsAuthenticated() throws Exception {
        // given
        systemProperties.setProperty(SystemProperties.STACK_TRACE_VISIBILITY_LEVEL_KEY, "nobody");

        // when
        Optional<String> stack = stackTraceVisibilityUsersService.getStackTraceAsString(new Exception());

        // then
        assertThat(stack).isEmpty();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    public void shouldNotReturnStack_whenPropertyIsSetToNobody_andUserIsAdministratorAuthenticated() throws Exception {
        // given
        systemProperties.setProperty(SystemProperties.STACK_TRACE_VISIBILITY_LEVEL_KEY, "nobody");

        // when
        Optional<String> stack = stackTraceVisibilityUsersService.getStackTraceAsString(new Exception());

        // then
        assertThat(stack).isEmpty();
    }
}
