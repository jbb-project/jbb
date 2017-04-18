package org.jbb.system.impl.session.event;


import org.jbb.system.impl.sesssion.event.SessionEventService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.session.MapSession;
import org.springframework.session.Session;
import org.springframework.session.events.SessionCreatedEvent;

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
    public void whenSessionEventIsCatchThanSessionShouldBeSaveInMap(){

        //given
        Session session = new MapSession("FakeSessionID");
        Object sessionSource = new Object();
        SessionCreatedEvent sessionCreatedEvent = new SessionCreatedEvent(sessionSource,session);

        //when
        sessionEventService.onApplicationEvent(sessionCreatedEvent);
        Map<String,MapSession> sessionMap = sessionEventService.getSessionMap();

        //then
        assertEquals(sessionMap.size(),1);
        assertEquals("FakeSessionID",sessionMap.get("FakeSessionID").getId());
    }
}
