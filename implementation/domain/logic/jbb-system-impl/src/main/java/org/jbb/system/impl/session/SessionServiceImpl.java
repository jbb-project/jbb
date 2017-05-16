package org.jbb.system.impl.session;


import org.jbb.lib.core.security.SecurityContentUser;
import org.jbb.lib.mvc.repository.JbbSessionRepository;
import org.jbb.system.api.model.session.UserSession;
import org.jbb.system.api.service.SessionService;
import org.jbb.system.impl.base.properties.SystemProperties;
import org.jbb.system.impl.session.model.SessionImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.session.ExpiringSession;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SessionServiceImpl implements SessionService{

    private final JbbSessionRepository jbbSessionRepository;
    private final SystemProperties systemProperties;

    private final static String SESSION_CONTEXT_ATTRIBUTE_NAME = "SPRING_SECURITY_CONTEXT";

    @Autowired
    public SessionServiceImpl(JbbSessionRepository jbbSessionRepository, SystemProperties systemProperties){
        this.jbbSessionRepository = jbbSessionRepository;
        this.jbbSessionRepository.setDefaultMaxInactiveInterval(systemProperties.sessionMaxInActiveTime());
        this.systemProperties=systemProperties;
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
        jbbSessionRepository.delete(session.sessionId());
    }

    @Override
    public void setDefaultInactiveSessionInterval(Duration maximumInactiveSessionInterval) {
        jbbSessionRepository.setDefaultMaxInactiveInterval(Long.valueOf(maximumInactiveSessionInterval.getSeconds()).intValue());
    }

    @Override
    public Duration getDefaultInactiveSessionInterval() {
        return Duration.ofSeconds(jbbSessionRepository.getDefaultMaxInactiveInterval());
    }

    private UserSession mapSessionToInternalModel(String sessionID, ExpiringSession expiringSession){
        return new SessionImpl().builder()
                .creationTime(getLocalTime(expiringSession.getCreationTime()))
                .id(sessionID)
                .inactiveTime(Duration.ofMillis(expiringSession.getMaxInactiveIntervalInSeconds()))
                .lastAccessedTime(getLocalTime(expiringSession.getLastAccessedTime()))
                .displayName(((SecurityContentUser) ((SecurityContextImpl) expiringSession
                        .getAttribute(SESSION_CONTEXT_ATTRIBUTE_NAME))
                        .getAuthentication().getPrincipal())
                        .getDisplayedName())
                .username(((SecurityContentUser) ((SecurityContextImpl) expiringSession
                        .getAttribute(SESSION_CONTEXT_ATTRIBUTE_NAME))
                        .getAuthentication().getPrincipal())
                        .getUsername())
                .usedTime(Duration.between(LocalTime.now(), getLocalTime(expiringSession.getLastAccessedTime())))
                .timeToLive(Duration.between(LocalTime.now(),
                        getLocalTime(expiringSession.getCreationTime()).plus(expiringSession.getMaxInactiveIntervalInSeconds(), ChronoUnit.MILLIS)))
                .build();
    }

    private LocalTime getLocalTime(long longValueToParse){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(systemProperties.durationFormat());

        return LocalTime.parse(String.valueOf(longValueToParse),dateTimeFormatter);
    }

}


