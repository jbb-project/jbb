package org.jbb.system.impl.sesssion;


import org.jbb.lib.core.security.SecurityContentUser;
import org.jbb.system.api.model.session.UserSession;
import org.jbb.system.api.service.SessionService;
import org.jbb.system.impl.sesssion.model.SessionImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class SessionServiceImpl implements SessionService{

    private final SessionRepository mapSessionRepository;
    private final SessionRegistry sessionRegistry;

    @Autowired
    public SessionServiceImpl(SessionRepository mapSessionRepository,SessionRegistry sessionRegistry){
        this.mapSessionRepository = mapSessionRepository;
        this.sessionRegistry = sessionRegistry;
    }

    @Override
    public List<UserSession> getAllUserSessions() {
        List<Object> currentLogInUsers = sessionRegistry.getAllPrincipals();
        return currentLogInUsers.stream()
                .map(currentLogInUser -> {
                    if(currentLogInUser instanceof SecurityContentUser) {
                        SecurityContentUser securityContentUser = (SecurityContentUser) currentLogInUser;
                        Session session = mapSessionRepository.getSession(securityContentUser.getUsername());

                        /**
                         *
                         * TODO
                         *
                         * Ze screenu od Bartka wypełniać SessionImpl
                         * Szukać po "SPRING_SECURITY_CONTEXT" w historii chatu
                         */
                        return SessionImpl.builder()
                                .username(securityContentUser.getUsername())
                                .displayName(securityContentUser.getDisplayedName())
                                .inactiveTime(Duration.of(0, ChronoUnit.MINUTES))
                                .lastAccessedTime(LocalDateTime.now())
                                .creationTime(LocalDateTime.now())
                                .usedTime(LocalDateTime.now())
                                .build();
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public void terminateSession(UserSession session) {

    }

    @Override
    public void setDefaultInactiveSessionInterval(Duration maximumInactiveSessionInterval) {

    }

    @Override
    public void getDefaultInactiveSessionInterval(Duration maximumInactiveSessionInterval) {

    }
}


