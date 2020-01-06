/*
 * Copyright (C) 2020 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.integration.rest.webhooks.event;

import java.util.List;

import io.swagger.annotations.ApiModel;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@ApiModel("WebhookEventTypes")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WebhookEventTypesDto {

    private List<WebhookEventTypeDto> eventTypes;

    public static WebhookEventTypesDto of(List<WebhookEventTypeDto> eventTypes) {
        return new WebhookEventTypesDto(eventTypes);
    }

}
