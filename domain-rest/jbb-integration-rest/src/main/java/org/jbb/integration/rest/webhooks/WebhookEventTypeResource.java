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

import org.jbb.integration.api.webhooks.WebhookEventService;
import org.jbb.lib.restful.domain.ErrorInfoCodes;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import static org.jbb.integration.rest.IntegrationRestAuthorize.IS_AN_ADMINISTRATOR_OR_OAUTH_WEBHOOK_EVENT_READ_SCOPE;
import static org.jbb.integration.rest.IntegrationRestConstants.WEBHOOK_EVENT_TYPES;
import static org.jbb.lib.restful.RestConstants.API_V1;
import static org.jbb.lib.restful.domain.ErrorInfo.FORBIDDEN;
import static org.jbb.lib.restful.domain.ErrorInfo.UNAUTHORIZED;

@RestController
@RequiredArgsConstructor
@Api(tags = API_V1 + WEBHOOK_EVENT_TYPES)
@RequestMapping(value = API_V1 + WEBHOOK_EVENT_TYPES, produces = MediaType.APPLICATION_JSON_VALUE)
public class WebhookEventTypeResource {

    private final WebhookEventService webhookEventService;

    private final WebhookEventTypeTranslator typeTranslator;

    @GetMapping
    @ApiOperation("Gets webhook event types")
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN})
    @PreAuthorize(IS_AN_ADMINISTRATOR_OR_OAUTH_WEBHOOK_EVENT_READ_SCOPE)
    public WebhookEventTypesDto eventTypesGet() {
        return WebhookEventTypesDto.of(typeTranslator.toDto(webhookEventService.getAllEventTypes()));
    }

}
