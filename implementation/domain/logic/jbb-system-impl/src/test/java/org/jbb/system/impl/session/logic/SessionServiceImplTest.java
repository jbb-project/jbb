/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.session.logic;


import com.google.common.collect.Maps;

import org.jbb.lib.core.security.SecurityContentUser;
import org.jbb.lib.mvc.repository.JbbSessionRepository;
import org.jbb.system.api.model.session.UserSession;
import org.jbb.system.impl.base.properties.SystemProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.session.ExpiringSession;
import org.springframework.session.MapSession;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class SessionServiceImplTest {

    @Mock
    private JbbSessionRepository jbbSessionRepository;

    @Mock
    private SystemProperties systemProperties;

    private SessionServiceImpl sessionService;

    @Before
    public void init(){
        when(systemProperties.sessionMaxInActiveTimeAsSeconds()).thenReturn(3600);
        this.sessionService = new SessionServiceImpl(jbbSessionRepository,systemProperties);
    }

    @Test
    public void whenNoOneIsLogInThenServiceShouldReturnEmptyCollection(){

        //given
        when(jbbSessionRepository.getSessionMap()).thenReturn(Maps.newHashMap());
        //when
        List<UserSession> allUserSessions = sessionService.getAllUserSessions();
        //then
        assertThat(allUserSessions.size()).isEqualTo(0);

    }

    @Test
    public void whenSomeoneIsLogInThenServiceShouldReturnCollectionThatContainsUser(){

        //given
        Map<String, ExpiringSession> fakeLogInUser = getSessionMapWithOneUser();
        when(jbbSessionRepository.getSessionMap()).thenReturn(fakeLogInUser);

        //when
        List<UserSession> allUserSessions = sessionService.getAllUserSessions();

        //then
        assertThat(allUserSessions.size()).isEqualTo(1);
    }

    @Test
    public void whenTerminateSessionMethodIsInvokeThenUserSessionShouldBeClosed(){

        //given
        UserSession userSession = getSessionToDelete();
        Map<String, ExpiringSession> fakeLogInUser = getSessionMapWithOneUser();
        Map<String, ExpiringSession> clearSessionMap = Maps.newHashMap();

        when(jbbSessionRepository.getSessionMap()).thenReturn(fakeLogInUser);

        //when
        sessionService.terminateSession(userSession.sessionId());
        when(jbbSessionRepository.getSessionMap()).thenReturn(clearSessionMap);
        List<UserSession> allUserSessions = sessionService.getAllUserSessions();

        //then
        assertThat(allUserSessions.size()).isEqualTo(0);

    }

    @Test
    public void whenTerminateSessionMethodIsInvokeWithNoExistingUserSessionThenNothingShouldHappen(){

        //given

        //when
        sessionService.terminateSession("fake_session_id");

        //then

    }
    @Test
    public void whenSomeoneChangeDefaultInactiveSessionIntervalThenParameterShouldBeChangeWithoutException(){

        //given
        Duration newInterval = Duration.ofHours(1);
        when(jbbSessionRepository.getDefaultMaxInactiveInterval()).thenReturn(3600);
        //when
        sessionService.setMaxInactiveSessionInterval(newInterval);
        Duration inactiveSessionInterval = sessionService.getMaxInactiveSessionInterval();

        //then
        assertThat(inactiveSessionInterval.get(ChronoUnit.SECONDS)).isEqualTo(3600);
    }

    @Test
    public void whenSomeoneSetNewValueOfPropertiesThenValueShouldBeSaveInFileAndInRepository(){

        //given
        int newValueOfMaxInactiveIntervalSecondsProperties = 7200;

        //when
        sessionService.setMaxInactiveSessionInterval(Duration.ofSeconds(newValueOfMaxInactiveIntervalSecondsProperties));

        //then
        verify(systemProperties,atLeast(1)).setProperty(anyString(),anyString());
        verify(jbbSessionRepository,atLeast(1)).setDefaultMaxInactiveInterval(anyInt());
    }


    private Map<String,ExpiringSession> getSessionMapWithOneUser() {
        Map<String,ExpiringSession> result = Maps.newHashMap();
        MapSession mapSession = new MapSession();
        String fakeUserName = "fakeuser";

        SecurityContextImpl securityContext = Mockito.mock(SecurityContextImpl.class);

        SecurityContentUser user = Mockito.mock(SecurityContentUser.class);
        doReturn(fakeUserName).when(user).getDisplayedName();
        doReturn(fakeUserName).when(user).getUsername();

        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        securityContext.setAuthentication(authentication);

        mapSession.setAttribute("SPRING_SECURITY_CONTEXT",securityContext);
        result.put("fakesessionid",mapSession);
        return result;
    }


    private UserSession getSessionToDelete() {
        return new UserSession() {
            @Override
            public String sessionId() {
                return "fakesessionid";
            }

            @Override
            public LocalDateTime creationTime() {
                return null;
            }

            @Override
            public LocalDateTime lastAccessedTime() {
                return null;
            }

            @Override
            public Duration usedTime() {
                return null;
            }

            @Override
            public Duration inactiveTime() {
                return null;
            }

            @Override
            public Duration timeToLive() {
                return null;
            }

            @Override
            public String userName() {
                return "fakeuser";
            }

            @Override
            public String displayUserName() {
                return "fakeuser";
            }
        };
    }
}
