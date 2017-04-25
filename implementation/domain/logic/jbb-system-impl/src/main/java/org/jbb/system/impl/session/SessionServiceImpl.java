package org.jbb.system.impl.session;


import org.jbb.lib.core.security.SecurityContentUser;
import org.jbb.lib.mvc.repository.JbbSessionRepository;
import org.jbb.system.api.model.session.UserSession;
import org.jbb.system.api.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.session.ExpiringSession;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SessionServiceImpl implements SessionService{


    private final JbbSessionRepository jbbSessionRepository;

    private final static String SESSION_CONTEXT_ATTRIBUTE_NAME = "SPRING_SECURITY_CONTEXT";
    @Autowired
    public SessionServiceImpl(JbbSessionRepository jbbSessionRepository){
        this.jbbSessionRepository=jbbSessionRepository;
    }

    @Override
    public List<UserSession> getAllUserSessions()
    {
        Map<String, ExpiringSession> jbbSessionRepositorySessionMap = jbbSessionRepository.getSessionMap();
        return jbbSessionRepositorySessionMap.entrySet()
                .stream()
                .map(entry -> mapSessionToInternalModel(entry.getKey(),entry.getValue()))
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

    private UserSession mapSessionToInternalModel(String sessionID, ExpiringSession expiringSession){
        return new UserSession() {
            @Override
            public String sessionId() {
                return sessionID;
            }

            @Override
            public LocalDateTime creationTime() {
                return LocalDateTime.ofInstant(Instant.ofEpochMilli(expiringSession.getCreationTime()), ZoneId.systemDefault());
            }

            @Override
            public LocalDateTime lastAccessedTime() {
                return LocalDateTime.ofInstant(Instant.ofEpochMilli(expiringSession.getLastAccessedTime()), ZoneId.systemDefault());
            }

            @Override
            public Duration usedTime() {
                return Duration.between(LocalDateTime.now(),LocalDateTime.ofInstant(Instant.ofEpochMilli(expiringSession.getCreationTime()), ZoneId.systemDefault()));
            }

            @Override
            public Duration inactiveTime() {
                return Duration.ofMillis(expiringSession.getMaxInactiveIntervalInSeconds());
            }

            @Override
            public String userName() {
                return ((SecurityContentUser)((SecurityContextImpl)expiringSession
                                .getAttribute(SESSION_CONTEXT_ATTRIBUTE_NAME))
                                .getAuthentication().getPrincipal())
                        .getUsername();
            }

            @Override
            public String displayUserName() {
                return ((SecurityContentUser)((SecurityContextImpl)expiringSession
                            .getAttribute(SESSION_CONTEXT_ATTRIBUTE_NAME))
                            .getAuthentication().getPrincipal())
                        .getDisplayedName();
            }
        };
    }
}


