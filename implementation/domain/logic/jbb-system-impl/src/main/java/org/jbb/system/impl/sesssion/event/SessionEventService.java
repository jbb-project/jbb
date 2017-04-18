package org.jbb.system.impl.sesssion.event;

import org.springframework.context.ApplicationListener;
import org.springframework.session.MapSession;
import org.springframework.session.events.SessionCreatedEvent;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SessionEventService implements ApplicationListener<SessionCreatedEvent> {

    private Map<String,MapSession> sessionMap;

    public SessionEventService(){
        this.sessionMap = new HashMap<>();
    }

    @Override
    public void onApplicationEvent(SessionCreatedEvent sessionCreatedEvent) {
        this.sessionMap.put(sessionCreatedEvent.getSessionId(),sessionCreatedEvent.getSession());
    }

    public Map<String,MapSession> getSessionMap(){
        return sessionMap;
    }
}
