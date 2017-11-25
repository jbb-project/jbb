/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.session;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.Maps;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import org.jbb.lib.commons.security.SecurityContentUser;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.lib.mvc.session.JbbSessionRepository;
import org.jbb.system.api.session.MemberSession;
import org.jbb.system.event.SessionTerminatedEvent;
import org.jbb.system.impl.SystemProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.session.ExpiringSession;
import org.springframework.session.MapSession;


@RunWith(MockitoJUnitRunner.class)
public class DefaultSessionServiceTest {

    @Mock
    private JbbSessionRepository jbbSessionRepositoryMock;

    @Mock
    private SystemProperties systemPropertiesMock;

    @Mock
    private JbbEventBus eventBusMock;

    @InjectMocks
    private DefaultSessionService sessionService;

    @Before
    public void init(){
        when(systemPropertiesMock.sessionMaxInActiveTimeAsSeconds()).thenReturn(3600);
        sessionService.init();
    }

    @Test
    public void whenNoOneIsLogInThenServiceShouldReturnEmptyCollection() {
        //given
        when(jbbSessionRepositoryMock.getSessionMap()).thenReturn(Maps.newHashMap());

        //when
        List<MemberSession> allMemberSessions = sessionService.getAllUserSessions();

        //then
        assertThat(allMemberSessions.size()).isEqualTo(0);

    }

    @Test
    public void whenSomeoneIsLogInThenServiceShouldReturnCollectionThatContainsUser() {
        //given
        Map<String, ExpiringSession> fakeLogInUser = getSessionMapWithOneUser();
        when(jbbSessionRepositoryMock.getSessionMap()).thenReturn(fakeLogInUser);

        //when
        List<MemberSession> allMemberSessions = sessionService.getAllUserSessions();

        //then
        assertThat(allMemberSessions.size()).isEqualTo(1);
    }

    @Test
    public void whenTerminateSessionMethodIsInvokeThenUserSessionShouldBeClosed() {
        //given
        MemberSession memberSession = getSessionToDelete();
        Map<String, ExpiringSession> fakeLogInUser = getSessionMapWithOneUser();
        Map<String, ExpiringSession> clearSessionMap = Maps.newHashMap();

        when(jbbSessionRepositoryMock.getSessionMap()).thenReturn(fakeLogInUser);

        //when
        sessionService.terminateSession(memberSession.getSessionId());
        when(jbbSessionRepositoryMock.getSessionMap()).thenReturn(clearSessionMap);
        List<MemberSession> allMemberSessions = sessionService.getAllUserSessions();

        //then
        assertThat(allMemberSessions.size()).isEqualTo(0);

    }

    @Test
    public void whenTerminateSessionMethodIsInvokeWithNoExistingUserSessionThenNothingShouldHappen() {

        //given

        //when
        sessionService.terminateSession("fake_session_id");

        //then

    }

    @Test
    public void shouldEmitSessionTerminatedEvent_whenSessionTerminated() {
        //when
        sessionService.terminateSession("fake_session_id");

        //then
        verify(eventBusMock).post(isA(SessionTerminatedEvent.class));

    }

    @Test
    public void whenSomeoneChangeDefaultInactiveSessionIntervalThenParameterShouldBeChangeWithoutException() {
        //given
        Duration newInterval = Duration.ofHours(1);
        when(jbbSessionRepositoryMock.getDefaultMaxInactiveInterval()).thenReturn(3600);

        //when
        sessionService.setMaxInactiveSessionInterval(newInterval);
        Duration inactiveSessionInterval = sessionService.getMaxInactiveSessionInterval();

        //then
        assertThat(inactiveSessionInterval.get(ChronoUnit.SECONDS)).isEqualTo(3600);
    }

    @Test
    public void whenSomeoneSetNewValueOfPropertiesThenValueShouldBeSaveInFileAndInRepository() {
        //given
        int newValueOfMaxInactiveIntervalSecondsProperties = 7200;

        //when
        sessionService.setMaxInactiveSessionInterval(Duration.ofSeconds(newValueOfMaxInactiveIntervalSecondsProperties));

        //then
        verify(systemPropertiesMock, atLeast(1)).setProperty(anyString(), anyString());
        verify(jbbSessionRepositoryMock, atLeast(1)).setDefaultMaxInactiveInterval(anyInt());
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


    private MemberSession getSessionToDelete() {
        return new MemberSession() {
            @Override
            public String getSessionId() {
                return "fakesessionid";
            }

            @Override
            public LocalDateTime getCreationTime() {
                return null;
            }

            @Override
            public LocalDateTime getLastAccessedTime() {
                return null;
            }

            @Override
            public Duration getUsedTime() {
                return null;
            }

            @Override
            public Duration getInactiveTime() {
                return null;
            }

            @Override
            public Duration getTimeToLive() {
                return null;
            }

            @Override
            public String getUsername() {
                return "fakeuser";
            }

            @Override
            public String getDisplayedName() {
                return "fakeuser";
            }
        };
    }
}
