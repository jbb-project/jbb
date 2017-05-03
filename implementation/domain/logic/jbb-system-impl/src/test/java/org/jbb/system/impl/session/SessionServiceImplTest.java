package org.jbb.system.impl.session;


import org.jbb.lib.mvc.repository.JbbSessionRepository;
import org.jbb.system.api.model.session.UserSession;
import org.jbb.system.impl.base.properties.SystemProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.session.SessionRepository;

import java.security.Principal;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
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
        UserSession sessionMock = Mockito.mock(UserSession.class);

        this.systemProperties = Mockito.mock(SystemProperties.class);
        this.jbbSessionRepository = Mockito.mock(JbbSessionRepository.class);

        when(systemProperties.sessionMaxInActiveTime()).thenReturn(7200);
        this.sessionService = new SessionServiceImpl(jbbSessionRepository,systemProperties);
    }

    @Test
    public void whenNoOneIsLogInThenServiceShouldReturnEmptyCollection(){

        //given

        //when
       // List<UserSession> allUserSessions = sessionService.getAllUserSessions();

        //then
       // assertThat(allUserSessions.size()).isEqualTo(0);
    }

    @Test
    public void whenSomeoneIsLogInThenServiceShouldReturnCollectionThatContainsUser(){

        //given
      //  when(sessionRegistry.getAllPrincipals()).thenReturn(getFakeLogInUsers());
        //when
      //  List<UserSession> allUserSessions = sessionService.getAllUserSessions();
        //then
//        assertThat(allUserSessions.size()).isEqualTo(3);
    }

    @Test
    public void whenTerminateSessionMethodIsInvokeThenUserSessionShouldBeTerminate(){

        //given

        //when

        //then

    }

    @Test
    public void whenSomeoneChangeDefaultInactiveSessionTimeThenParameterShouldBeChangeWithoutException(){

        //given
        Duration newDefaultInactiveSessionIntervalTime = Duration.of(3600, ChronoUnit.SECONDS);
        //when
        sessionService.setDefaultInactiveSessionInterval(newDefaultInactiveSessionIntervalTime);
        Duration defaultInactiveSessionInterval = sessionService.getDefaultInactiveSessionInterval();

        //then
        verify(jbbSessionRepository,times(1)).setDefaultMaxInactiveInterval(3600);
    }

    @Test
    public void whenGetDefaultInactiveSessionTimeMethodIsInvokeThenParameterShouldBeReturn(){

        //given
        when(jbbSessionRepository.getDefaultMaxInactiveInterval()).thenReturn(7200);

        //when
        Duration defaultInactiveSessionInterval = sessionService.getDefaultInactiveSessionInterval();

        //then
        assertEquals(120,defaultInactiveSessionInterval.toMinutes());
    }

    public List<Object> getFakeLogInUsers() {
        List<Object> principalCollection = new ArrayList<>();
        principalCollection.add(new Principal() {
            @Override
            public String getName() {
                return "FakeUser1";
            }
        });

        principalCollection.add(new Principal() {
            @Override
            public String getName() {
                return "FakeUser2";
            }
        });

        principalCollection.add(new Principal() {
            @Override
            public String getName() {
                return "FakeUser3";
            }
        });

        return principalCollection;
    }
}
