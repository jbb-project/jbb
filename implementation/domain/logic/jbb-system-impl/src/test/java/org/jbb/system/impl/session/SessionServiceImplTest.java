package org.jbb.system.impl.session;


import org.jbb.system.api.model.session.UserSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.session.SessionRepository;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class SessionServiceImplTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private SessionRegistry sessionRegistry;

    @InjectMocks
    private SessionServiceImpl sessionService;

    public void init(){
        UserSession sessionMock = Mockito.mock(UserSession.class);
    }

    @Test
    public void whenNoOneIsLogInThenServiceShouldReturnEmptyCollection(){

        //given

        //when
        List<UserSession> allUserSessions = sessionService.getAllUserSessions();

        //then
        assertThat(allUserSessions.size()).isEqualTo(0);
    }

    @Test
    public void whenSomeoneIsLogInThenServiceShouldReturnCollectionThatContainsUser(){

        //given
        when(sessionRegistry.getAllPrincipals()).thenReturn(getFakeLogInUsers());
        //when
        List<UserSession> allUserSessions = sessionService.getAllUserSessions();
        //then
//        assertThat(allUserSessions.size()).isEqualTo(3);
    }

    @Test
    public void whenTerminateSessionMethodIsInvokeThenUserSessionShouldBeClosed(){

        //given

        //when

        //then

    }

    @Test
    public void whenSomeoneChangeDefaultInactiveSessionIntervalThenParameterShouldBeChangeWithoutException(){

        //given

        //when

        //then
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
