package org.jbb.system.impl.sesssion.event;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.session.SessionCreationEvent;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.session.MapSession;
import org.springframework.session.events.SessionCreatedEvent;
import org.springframework.session.events.SessionDeletedEvent;
import org.springframework.session.events.SessionExpiredEvent;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionEventService implements ApplicationListener<SessionCreationEvent>{

    private Map<String,MapSession> sessionMap;

    public SessionEventService(){
        this.sessionMap = new ConcurrentHashMap<>();
    }

    @EventListener
    public void onSessionCreatedEvent(SessionCreatedEvent sessionCreatedEvent) {
        this.sessionMap.put(sessionCreatedEvent.getSessionId(),sessionCreatedEvent.getSession());
    }

    @EventListener
    public void onSessionDeletedEvent(SessionDeletedEvent sessionDeletedEvent){
        this.sessionMap.remove(sessionDeletedEvent.getSessionId());
    }

    @EventListener
    public void onSessionDestroyedEvent(SessionDestroyedEvent sessionDestroyedEvent){
        this.sessionMap.remove(sessionDestroyedEvent.getId());
    }

    @EventListener
    public void onSessionExpiredEvent(SessionExpiredEvent sessionExpiredEvent){
        this.sessionMap.remove(sessionExpiredEvent.getSessionId());
    }

    public Map<String,MapSession> getSessionMap(){
        return sessionMap;
    }

    @Override
    public void onApplicationEvent(SessionCreationEvent sessionCreatedEvent) {
        System.out.println("kurwa chuj");
    }
}
