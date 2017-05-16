package org.jbb.system.impl.session;


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
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
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
        this.sessionService = new SessionServiceImpl(jbbSessionRepository,systemProperties);
        when(systemProperties.durationFormat()).thenReturn("HH:MM:ss");
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
        Map<String, ExpiringSession> fakeLogInUser = getLoggedUser();
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
        Map<String, ExpiringSession> fakeLogInUser = getLoggedUser();
        Map<String, ExpiringSession> clearSessionMap = Maps.newHashMap();

        when(jbbSessionRepository.getSessionMap()).thenReturn(fakeLogInUser);

        //when
        sessionService.terminateSession(userSession);
        when(jbbSessionRepository.getSessionMap()).thenReturn(clearSessionMap);
        List<UserSession> allUserSessions = sessionService.getAllUserSessions();

        //then
        assertThat(allUserSessions.size()).isEqualTo(0);

    }

    @Test
    public void whenSomeoneChangeDefaultInactiveSessionIntervalThenParameterShouldBeChangeWithoutException(){

        //given
        Duration newInterval = Duration.ofHours(1);
        when(jbbSessionRepository.getDefaultMaxInactiveInterval()).thenReturn(3600);
        //when
        sessionService.setDefaultInactiveSessionInterval(newInterval);
        Duration inactiveSessionInterval = sessionService.getDefaultInactiveSessionInterval();

        //then
        assertThat(inactiveSessionInterval.get(ChronoUnit.SECONDS)).isEqualTo(3600);
    }

    private Map<String,ExpiringSession> getLoggedUser() {
        Map<String,ExpiringSession> result = Maps.newHashMap();
        MapSession mapSession = new MapSession();
        String abc = "fakeuser";

        SecurityContextImpl securityContext = Mockito.mock(SecurityContextImpl.class);

        SecurityContentUser user = Mockito.mock(SecurityContentUser.class);
        doReturn(abc).when(user).getDisplayedName();
        doReturn(abc).when(user).getUsername();

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
            public LocalTime creationTime() {
                return null;
            }

            @Override
            public LocalTime lastAccessedTime() {
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