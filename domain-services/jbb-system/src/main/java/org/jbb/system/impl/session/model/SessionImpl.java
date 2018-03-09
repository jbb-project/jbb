/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.session.model;

import org.jbb.system.api.session.MemberSession;

import java.time.Duration;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SessionImpl implements MemberSession {

    private String id;

    private LocalDateTime creationTime;

    private LocalDateTime lastAccessedTime;

    private String username;

    private String displayedName;

    private Duration maxInactiveInterval;

    @Override
    public String getSessionId() {
        return id;
    }

    @Override
    public Duration getUsedTime() {
        return Duration.between(creationTime, lastAccessedTime);
    }

    @Override
    public Duration getInactiveTime() {
        return Duration.between(lastAccessedTime, LocalDateTime.now());
    }

    @Override
    public Duration getTimeToLive() {
        return Duration.between(LocalDateTime.now(), lastAccessedTime.plus(maxInactiveInterval));
    }

}
