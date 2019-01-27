/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.integration.rest.webhooks;

import java.time.LocalDateTime;
import java.util.Map;

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
@ApiModel("WebhookEvent")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WebhookEventDto {

    private String eventId;

    private String eventName;

    private String eventVersion;

    private LocalDateTime publishDateTime;

    private String sourceRequestId;

    private Long sourceMemberId;

    private String sourceOAuthClientId;

    private String sourceIpAddress;

    private String sourceSessionId;

    private Map<String, Object> details;
}
