package org.jbb.system.impl.session.event;


import com.google.common.collect.Lists;

import org.jbb.system.impl.sesssion.event.SessionEventService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.session.MapSession;
import org.springframework.session.Session;
import org.springframework.session.events.SessionCreatedEvent;
import org.springframework.session.events.SessionDeletedEvent;
import org.springframework.session.events.SessionExpiredEvent;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class SessionEventServiceTest {

    private SessionEventService sessionEventService;

    @Before
    public void init(){
        this.sessionEventService = new SessionEventService();
    }

    @Test
    public void whenNoSessionExistMapShouldBeEmpty(){

        //given

        //when
        Map<String,MapSession> sessionMap = sessionEventService.getSessionMap();

        //then
        assertEquals(sessionMap.size(),0);
    }

    @Test
    public void whenSessionIsCreatedThanSessionShouldBeSaveInMap(){

        //given
        Session session = new MapSession("FakeSessionID");
        Object sessionSource = new Object();
        SessionCreatedEvent sessionCreatedEvent = new SessionCreatedEvent(sessionSource,session);

        //when
        sessionEventService.onSessionCreatedEvent(sessionCreatedEvent);
        Map<String,MapSession> sessionMap = sessionEventService.getSessionMap();

        //then
        assertEquals(sessionMap.size(),1);
        assertEquals("FakeSessionID",sessionMap.get("FakeSessionID").getId());
    }

    @Test
    public void whenSessionIsDeletedThanSessionShouldBeRemoveMap(){

        //given
        Session session = new MapSession("FakeSessionID");
        Object sessionSource = new Object();
        SessionCreatedEvent sessionCreatedEvent = new SessionCreatedEvent(sessionSource,session);

        sessionEventService.onSessionCreatedEvent(sessionCreatedEvent);

        SessionDeletedEvent sessionDeletedEvent = new SessionDeletedEvent(sessionSource,session);

        //when
        sessionEventService.onSessionDeletedEvent(sessionDeletedEvent);
        Map<String, MapSession> sessionMap = sessionEventService.getSessionMap();

        //then
        assertEquals(sessionMap.size(),0);
    }

    @Test
    public void whenSessionIsExpiredThanSessionShouldBeRemoveMap(){

        //given
        Session session = new MapSession("FakeSessionID");
        Object sessionSource = new Object();
        SessionCreatedEvent sessionCreatedEvent = new SessionCreatedEvent(sessionSource,session);

        sessionEventService.onSessionCreatedEvent(sessionCreatedEvent);

        SessionExpiredEvent sessionExpiredEvent = new SessionExpiredEvent(sessionSource,session);

        //when
        sessionEventService.onSessionExpiredEvent(sessionExpiredEvent);
        Map<String, MapSession> sessionMap = sessionEventService.getSessionMap();

        //then
        assertEquals(sessionMap.size(),0);
    }

    @Test
    public void whenSessionIsDestroyedThanSessionShouldBeRemoveMap(){

        //given
        Session session = new MapSession("FakeSessionID");
        Object sessionSource = new Object();
        SessionCreatedEvent sessionCreatedEvent = new SessionCreatedEvent(sessionSource,session);

        sessionEventService.onSessionCreatedEvent(sessionCreatedEvent);

        SessionDestroyedEvent sessionExpiredEvent = new SessionDestroyedEvent(sessionSource) {
            @Override
            public List<SecurityContext> getSecurityContexts() {
                return Lists.newArrayList();
            }

            @Override
            public String getId() {
                return "FakeSessionID";
            }
        };

        //when
        sessionEventService.onSessionDestroyedEvent(sessionExpiredEvent);
        Map<String, MapSession> sessionMap = sessionEventService.getSessionMap();

        //then
        assertEquals(sessionMap.size(),0);
    }
}
