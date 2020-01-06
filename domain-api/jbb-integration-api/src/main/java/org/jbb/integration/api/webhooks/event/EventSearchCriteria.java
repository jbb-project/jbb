/*
 * Copyright (C) 2020 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.integration.api.webhooks.event;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventSearchCriteria {

    @Builder.Default
    private Optional<String> eventName = Optional.empty();

    @Builder.Default
    private Optional<EventType> eventType = Optional.empty();

    @Builder.Default
    private Pageable pageRequest = PageRequest.of(0, 20);

}
