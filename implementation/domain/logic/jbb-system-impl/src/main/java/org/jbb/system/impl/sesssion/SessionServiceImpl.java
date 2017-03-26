package org.jbb.system.impl.sesssion;


import org.jbb.system.api.model.session.SessionSettings;
import org.jbb.system.api.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SessionServiceImpl implements SessionService{

    private final MapSessionRepository mapSessionRepository;
    private final SessionRegistry sessionRegistry;
    private final SessionRepository sessionRepository;

    @Autowired
    public SessionServiceImpl(SessionRepository sessionRepository, MapSessionRepository mapSessionRepository, SessionRegistry sessionRegistry){
        this.sessionRepository = sessionRepository;
        this.sessionRegistry = sessionRegistry;
        this.mapSessionRepository = mapSessionRepository;
    }

    @Override
    public List<SessionSettings> getAllUserSessions() {
//        List<Object> currentLogInUsers = sessionRegistry.getAllPrincipals();
//        return currentLogInUsers.stream()
//                .map(currentLogInUser -> {
//                    if(currentLogInUser instanceof SecurityContentUser) {
//
//                    }
//
//                })
//                .collect(Collectors.toList());

        return null;
    }

    @Override
    public void terminateSession(SessionSettings sessionSettings) {
    }

    @Override
    public void setDefaultInactiveSessionInterval(Long defaultInactiveSessionInterval) {

    }

}
