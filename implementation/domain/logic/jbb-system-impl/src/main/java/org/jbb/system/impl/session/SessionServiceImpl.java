/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

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
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SessionServiceImpl implements SessionService{

    private final JbbSessionRepository jbbSessionRepository;
    private final SystemProperties systemProperties;

    private static final String SESSION_CONTEXT_ATTRIBUTE_NAME = "SPRING_SECURITY_CONTEXT";

    @Autowired
    public SessionServiceImpl(JbbSessionRepository jbbSessionRepository, SystemProperties systemProperties){
        this.jbbSessionRepository = jbbSessionRepository;
        this.jbbSessionRepository.setDefaultMaxInactiveInterval(systemProperties.sessionMaxInActiveTimeAsSeconds());
        this.systemProperties=systemProperties;
    }

    @Override
    public List<UserSession> getAllUserSessions(){
        Map<String, ExpiringSession> jbbSessionRepositorySessionMap = jbbSessionRepository.getSessionMap();
        return jbbSessionRepositorySessionMap.entrySet()
                .stream()
                .map(entry -> mapSessionToInternalModel(entry.getKey(),entry.getValue()))
                .sorted((userSession1,userSession2) -> userSession2.creationTime().compareTo(userSession1.creationTime()))
                .collect(Collectors.toList());
    }

    @Override
    public void terminateSession(String sessionId) {
        jbbSessionRepository.delete(sessionId);
    }

    @Override
    public void setDefaultInactiveSessionInterval(Duration maximumInactiveSessionInterval) {
        jbbSessionRepository.setDefaultMaxInactiveInterval((int) maximumInactiveSessionInterval.getSeconds());
        systemProperties.setProperty(SystemProperties.SESSION_INACTIVE_INTERVAL_TIME_AS_SECONDS,String.valueOf(maximumInactiveSessionInterval.getSeconds()));
    }

    @Override
    public Duration getDefaultInactiveSessionInterval() {
        return Duration.ofSeconds(jbbSessionRepository.getDefaultMaxInactiveInterval());
    }

    private UserSession mapSessionToInternalModel(String sessionId, ExpiringSession expiringSession){
        return SessionImpl.builder()
                .creationTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(expiringSession.getCreationTime()), ZoneId.systemDefault()))
                .id(sessionId)
                .inactiveTime(Duration.between(LocalDateTime.now(),
                                        LocalDateTime.ofInstant(Instant.ofEpochMilli(expiringSession.getLastAccessedTime()), ZoneId.systemDefault())))
                .lastAccessedTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(expiringSession.getLastAccessedTime()), ZoneId.systemDefault()))
                .displayName(((SecurityContentUser) ((SecurityContextImpl) expiringSession
                        .getAttribute(SESSION_CONTEXT_ATTRIBUTE_NAME))
                        .getAuthentication().getPrincipal())
                        .getDisplayedName())
                .username(((SecurityContentUser) ((SecurityContextImpl) expiringSession
                        .getAttribute(SESSION_CONTEXT_ATTRIBUTE_NAME))
                        .getAuthentication().getPrincipal())
                        .getUsername())
                .usedTime(Duration.between(LocalDateTime.ofInstant(Instant.ofEpochMilli(expiringSession.getLastAccessedTime()), ZoneId.systemDefault()),
                                        LocalDateTime.ofInstant(Instant.ofEpochMilli(expiringSession.getCreationTime()), ZoneId.systemDefault())))
                .timeToLive(Duration.between(LocalDateTime.now(),(
                        LocalDateTime.ofInstant(Instant.ofEpochMilli(expiringSession.getCreationTime()), ZoneId.systemDefault())
                                .plus(expiringSession.getMaxInactiveIntervalInSeconds(), ChronoUnit.MILLIS))))
                .build();
    }

}


