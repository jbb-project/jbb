/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.integration.rest.webhooks.settings;

import org.jbb.integration.api.webhooks.settings.WebhookSettingsException;
import org.jbb.integration.api.webhooks.settings.WebhookSettingsService;
import org.jbb.lib.restful.domain.ErrorInfoCodes;
import org.jbb.lib.restful.error.DefaultRestExceptionMapper;
import org.jbb.lib.restful.error.ErrorResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

import javax.validation.ConstraintViolation;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import static org.jbb.integration.rest.IntegrationRestAuthorize.IS_AN_ADMINISTRATOR_OR_OAUTH_WEBHOOK_SETTINGS_READ_SCOPE;
import static org.jbb.integration.rest.IntegrationRestAuthorize.IS_AN_ADMINISTRATOR_OR_OAUTH_WEBHOOK_SETTINGS_READ_WRITE_SCOPE;
import static org.jbb.integration.rest.IntegrationRestConstants.WEBHOOK_SETTINGS;
import static org.jbb.lib.restful.RestConstants.API_V1;
import static org.jbb.lib.restful.domain.ErrorInfo.FORBIDDEN;
import static org.jbb.lib.restful.domain.ErrorInfo.INVALID_WEBHOOK_SETTINGS;
import static org.jbb.lib.restful.domain.ErrorInfo.UNAUTHORIZED;

@RestController
@RequiredArgsConstructor
@Api(tags = API_V1 + WEBHOOK_SETTINGS)
@RequestMapping(value = API_V1 + WEBHOOK_SETTINGS, produces = MediaType.APPLICATION_JSON_VALUE)
public class WebhookSettingsResource {

    private final WebhookSettingsService webhookSettingsService;

    private final WebhookSettingsTranslator translator;
    private final DefaultRestExceptionMapper exceptionMapper;

    @GetMapping
    @ApiOperation("Gets webhook settings")
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN})
    @PreAuthorize(IS_AN_ADMINISTRATOR_OR_OAUTH_WEBHOOK_SETTINGS_READ_SCOPE)
    public WebhookSettingsDto webhookSettingsGet() {
        return translator.toDto(webhookSettingsService.getWebhookSettings());
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Updates webhook settings")
    @ErrorInfoCodes({INVALID_WEBHOOK_SETTINGS, UNAUTHORIZED, FORBIDDEN})
    @PreAuthorize(IS_AN_ADMINISTRATOR_OR_OAUTH_WEBHOOK_SETTINGS_READ_WRITE_SCOPE)
    public WebhookSettingsDto webhookSettingsPut(@RequestBody WebhookSettingsDto webhookSettingsDto) {
        webhookSettingsService.setWebhookSettings(translator.toModel(webhookSettingsDto));
        return webhookSettingsDto;
    }

    @ExceptionHandler(WebhookSettingsException.class)
    ResponseEntity<ErrorResponse> handle(WebhookSettingsException ex) {
        ErrorResponse errorResponse = ErrorResponse.createFrom(INVALID_WEBHOOK_SETTINGS);
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();

        constraintViolations.stream()
                .map(exceptionMapper::mapToErrorDetail)
                .forEach(errorResponse.getDetails()::add);

        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }
}
