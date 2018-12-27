/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.eventbus;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class JbbEvent {

    @Getter
    @NotBlank
    final String eventId;

    @Getter
    @NotNull
    final LocalDateTime creationDateTime;

    @Getter
    @Setter(AccessLevel.PACKAGE)
    LocalDateTime publishDateTime;

    @Getter
    @Setter(AccessLevel.PACKAGE)
    Optional<String> sourceRequestId = Optional.empty();

    @Getter
    @Setter(AccessLevel.PACKAGE)
    Optional<Long> sourceMemberId = Optional.empty();

    @Getter
    @Setter(AccessLevel.PACKAGE)
    Optional<String> sourceOAuthClientId = Optional.empty();

    @Getter
    @Setter(AccessLevel.PACKAGE)
    Optional<String> sourceIpAddress = Optional.empty();

    @Getter
    @Setter(AccessLevel.PACKAGE)
    Optional<String> sourceSessionId = Optional.empty();

    protected JbbEvent() {
        this.eventId = UUID.randomUUID().toString();
        this.creationDateTime = LocalDateTime.now();
    }

}
