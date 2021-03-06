/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.session;


import org.jbb.lib.commons.security.SecurityContentUser;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.lib.mvc.session.JbbSessionRepository;
import org.jbb.system.api.session.MemberSession;
import org.jbb.system.api.session.SessionService;
import org.jbb.system.event.SessionSettingsChangedEvent;
import org.jbb.system.event.SessionTerminatedEvent;
import org.jbb.system.impl.SystemProperties;
import org.jbb.system.impl.session.model.SessionImpl;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.session.ExpiringSession;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultSessionService implements SessionService {
    static final String SESSION_CONTEXT_ATTRIBUTE_NAME = "SPRING_SECURITY_CONTEXT";

    private final JbbSessionRepository jbbSessionRepository;
    private final SystemProperties systemProperties;
    private final JbbEventBus eventBus;

    @PostConstruct
    public void init() {
        jbbSessionRepository.setDefaultMaxInactiveInterval(
                systemProperties.sessionMaxInActiveTimeAsSeconds()
        );
    }

    @Override
    @Scheduled(fixedDelay = 5000)
    public List<MemberSession> getAllUserSessions() {
        Map<String, ExpiringSession> jbbSessionRepositorySessionMap = jbbSessionRepository.getSessionMap();
        return jbbSessionRepositorySessionMap.entrySet()
                .stream()
                .filter(entry -> entry.getValue().getAttribute(SESSION_CONTEXT_ATTRIBUTE_NAME) != null)
                .map(entry -> mapSessionToInternalModel(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(MemberSession::getCreationTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public void terminateSession(String sessionId) {
        jbbSessionRepository.delete(sessionId);
        eventBus.post(new SessionTerminatedEvent(sessionId));
    }

    @Override
    public Duration getMaxInactiveSessionInterval() {
        return Duration.ofSeconds(jbbSessionRepository.getDefaultMaxInactiveInterval());
    }

    @Override
    public void setMaxInactiveSessionInterval(Duration maximumInactiveSessionInterval) {
        jbbSessionRepository.setDefaultMaxInactiveInterval((int) maximumInactiveSessionInterval.getSeconds());
        systemProperties.setProperty(SystemProperties.SESSION_INACTIVE_INTERVAL_TIME_SECONDS_KEY,
                String.valueOf(maximumInactiveSessionInterval.getSeconds()));
        eventBus.post(new SessionSettingsChangedEvent());
    }

    private MemberSession mapSessionToInternalModel(String sessionId, ExpiringSession expiringSession) {
        SecurityContextImpl securityContext = expiringSession.getAttribute(SESSION_CONTEXT_ATTRIBUTE_NAME);
        SecurityContentUser securityContentUser = (SecurityContentUser) securityContext.getAuthentication().getPrincipal();

        return SessionImpl.builder()
                .id(sessionId)
                .creationTime(toDateTime(expiringSession.getCreationTime()))
                .username(securityContentUser.getUsername())
                .lastAccessedTime(toDateTime(expiringSession.getLastAccessedTime()))
                .displayedName(securityContentUser.getDisplayedName())
                .maxInactiveInterval(Duration.ofSeconds(expiringSession.getMaxInactiveIntervalInSeconds()))
                .build();
    }

    private LocalDateTime toDateTime(long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
    }

}


