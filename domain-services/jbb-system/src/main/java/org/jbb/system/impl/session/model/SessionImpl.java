/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.session.model;

import org.jbb.system.api.session.UserSession;

import java.time.Duration;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class SessionImpl implements UserSession {

    private String id;

    private LocalDateTime creationTime;

    private LocalDateTime lastAccessedTime;

    private String username;

    private String displayName;

    private Duration maxInactiveInterval;

    @Override
    public String sessionId() {
        return id;
    }

    @Override
    public LocalDateTime creationTime() {
        return creationTime;
    }

    @Override
    public LocalDateTime lastAccessedTime() {
        return lastAccessedTime;
    }

    @Override
    public Duration usedTime() {
        return Duration.between(creationTime, lastAccessedTime);
    }

    @Override
    public Duration inactiveTime() {
        return Duration.between(lastAccessedTime, LocalDateTime.now());
    }

    @Override
    public Duration timeToLive() {
        return Duration.between(LocalDateTime.now(), lastAccessedTime.plus(maxInactiveInterval));
    }

    @Override
    public String userName() {
        return username;
    }

    @Override
    public String displayUserName() {
        return displayName;
    }


}
