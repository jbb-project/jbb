/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.stacktrace.format;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.assertj.core.util.Lists;
import org.jbb.system.api.stacktrace.StackTraceVisibilityLevel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@RunWith(MockitoJUnitRunner.class)
public class OnlyAuthenticatedUsersCanSeeStackTraceStrategyTest {
    @Mock
    private UserDetails userDetailsMock;

    @InjectMocks
    private OnlyAuthenticatedUsersCanSeeStackTraceStrategy strategy;

    @Test
    public void shouldHandle_whenVisibilityValueIsEqualToUsers_andUserDetailsArePresent() throws Exception {
        // given
        StackTraceVisibilityLevel visibility = StackTraceVisibilityLevel.USERS;

        // when
        boolean canHandle = strategy.canHandle(visibility, userDetailsMock);

        // then
        assertThat(canHandle).isTrue();
    }

    @Test
    public void shouldNotHandle_whenVisibilityValueIsEqualToUsers_andUserDetailsAbonymous() throws Exception {
        // given
        StackTraceVisibilityLevel visibility = StackTraceVisibilityLevel.USERS;
        UserDetails userDetails = mock(UserDetails.class);
        given(userDetails.getAuthorities())
                .willAnswer(invocationOnMock -> Sets.newHashSet(new SimpleGrantedAuthority("ROLE_ANONYMOUS")));

        // when
        boolean canHandle = strategy.canHandle(visibility, userDetails);

        // then
        assertThat(canHandle).isFalse();
    }

    @Test
    public void shouldHandle_whenVisibilityValueIsEqualToUsers_andUserDetailsIsNotAbonymous() throws Exception {
        // given
        StackTraceVisibilityLevel visibility = StackTraceVisibilityLevel.USERS;
        UserDetails userDetails = mock(UserDetails.class);
        given(userDetails.getAuthorities())
                .willAnswer(invocationOnMock -> Sets.newHashSet(new SimpleGrantedAuthority("ROLE_USER")));

        // when
        boolean canHandle = strategy.canHandle(visibility, userDetails);

        // then
        assertThat(canHandle).isTrue();
    }

    @Test
    public void shouldNotHandle_whenVisibilityValueIsNotEqualToUsers() throws Exception {
        // given
        List<StackTraceVisibilityLevel> visibilities = Lists.newArrayList(Arrays.asList(StackTraceVisibilityLevel.values()));
        visibilities.remove(StackTraceVisibilityLevel.USERS);

        // when
        for (StackTraceVisibilityLevel visibility : visibilities) {
            if (strategy.canHandle(visibility, userDetailsMock)) {
                // then
                fail("Should not be handled with visibility: " + visibility);
            }
        }
    }

    @Test
    public void shouldReturnStackTrace() throws Exception {
        // given
        Exception ex = new Exception("Example");

        // when
        Optional<String> stackTraceString = strategy.getStackTraceAsString(ex);

        // then
        assertThat(stackTraceString).isPresent();
    }
}
