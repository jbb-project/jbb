/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.mvc.session;


import com.google.common.collect.ImmutableMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.session.ExpiringSession;
import org.springframework.session.MapSession;
import org.springframework.session.SessionRepository;
import org.springframework.session.events.SessionDeletedEvent;
import org.springframework.session.events.SessionExpiredEvent;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This is own implementation of MapSessionRepository. The only change with compare to original implementation is method @getSessionMap
 * Method is using in ACP in session management
 */
@Repository
public class JbbSessionRepository implements SessionRepository<ExpiringSession> {

    private final Map<String, ExpiringSession> sessions;
    private final ApplicationEventPublisher eventPublisher;
    private Integer defaultMaxInactiveInterval;

    @Autowired
    public JbbSessionRepository(ApplicationEventPublisher applicationEventPublisher) {
        this(new ConcurrentHashMap<>(), applicationEventPublisher);
    }

    public JbbSessionRepository(Map<String, ExpiringSession> sessions, ApplicationEventPublisher applicationEventPublisher) {
        this.eventPublisher = applicationEventPublisher;
        if (sessions == null) {
            throw new IllegalArgumentException("sessions cannot be null");
        }
        this.sessions = sessions;
    }

    public Integer getDefaultMaxInactiveInterval() {
        return defaultMaxInactiveInterval;
    }

    public void setDefaultMaxInactiveInterval(int defaultMaxInactiveInterval) {
        this.defaultMaxInactiveInterval = Integer.valueOf(defaultMaxInactiveInterval);
    }

    public void save(ExpiringSession session) {
        this.sessions.put(session.getId(), new MapSession(session));
    }

    public ExpiringSession getSession(String id) {
        ExpiringSession saved = this.sessions.get(id);
        if (saved == null) {
            return null;
        }
        if (saved.isExpired()) {
            this.eventPublisher.publishEvent(new SessionExpiredEvent(this, saved));
            this.sessions.remove(id);
            return null;
        }
        return new MapSession(saved);
    }

    public Map<String, ExpiringSession> getSessionMap() {
        sessions.entrySet().forEach(entry -> getSession(entry.getKey()));
        return ImmutableMap.copyOf(sessions);
    }

    public void delete(String id) {
        ExpiringSession toRemove = this.sessions.get(id);
        this.sessions.remove(id);
        this.eventPublisher.publishEvent(new SessionDeletedEvent(this, toRemove));
    }

    public ExpiringSession createSession() {
        ExpiringSession result = new MapSession();
        if (this.defaultMaxInactiveInterval != null) {
            result.setMaxInactiveIntervalInSeconds(this.defaultMaxInactiveInterval);
        }
        return result;
    }
}
