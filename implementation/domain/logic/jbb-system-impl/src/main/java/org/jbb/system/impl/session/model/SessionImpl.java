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

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.jbb.system.api.model.session.UserSession;

import java.time.Duration;
import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

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

    private Duration usedTime;

    private Duration inactiveTime;

    private String username;

    private String displayName;

    private Duration timeToLive;

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
        return usedTime;
    }

    @Override
    public Duration inactiveTime() {
        return inactiveTime;
    }

    @Override
    public Duration timeToLive() {
        return timeToLive;
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
