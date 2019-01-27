/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.integration.api.webhooks;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WebhookEvent {

    @NotBlank
    private String eventId;

    @NotNull
    private EventType eventType;

    @NotNull
    private LocalDateTime creationDateTime;

    @NotNull
    private LocalDateTime publishDateTime;

    @NotNull
    @Builder.Default
    private Optional<String> sourceRequestId = Optional.empty();

    @NotNull
    @Builder.Default
    private Optional<Long> sourceMemberId = Optional.empty();

    @NotNull
    @Builder.Default
    private Optional<String> sourceOAuthClientId = Optional.empty();

    @NotNull
    @Builder.Default
    private Optional<String> sourceIpAddress = Optional.empty();

    @NotNull
    @Builder.Default
    private Optional<String> sourceSessionId = Optional.empty();

}
