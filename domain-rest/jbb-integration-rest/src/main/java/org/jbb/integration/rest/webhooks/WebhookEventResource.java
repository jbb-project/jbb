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

import org.jbb.integration.api.webhooks.EventSearchCriteria;
import org.jbb.integration.api.webhooks.WebhookEventNotFoundException;
import org.jbb.integration.api.webhooks.WebhookEventService;
import org.jbb.integration.api.webhooks.WebhookEventSummary;
import org.jbb.integration.rest.webhooks.exception.EventTypeNotFound;
import org.jbb.integration.rest.webhooks.exception.InvalidNameVersionCriteriaParam;
import org.jbb.lib.restful.domain.ErrorInfoCodes;
import org.jbb.lib.restful.error.ErrorResponse;
import org.jbb.lib.restful.paging.PageDto;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import static org.jbb.integration.rest.IntegrationRestAuthorize.IS_AN_ADMINISTRATOR_OR_OAUTH_WEBHOOK_EVENT_READ_SCOPE;
import static org.jbb.integration.rest.IntegrationRestConstants.EVENT_ID;
import static org.jbb.integration.rest.IntegrationRestConstants.EVENT_ID_VAR;
import static org.jbb.integration.rest.IntegrationRestConstants.WEBHOOK_EVENTS;
import static org.jbb.lib.restful.RestConstants.API_V1;
import static org.jbb.lib.restful.domain.ErrorInfo.INVALID_WEBHOOK_EVENT_TYPE_PARAM;
import static org.jbb.lib.restful.domain.ErrorInfo.WEBHOOK_EVENT_NOT_FOUND;
import static org.jbb.lib.restful.domain.ErrorInfo.WEBHOOK_EVENT_TYPE_NOT_FOUND;

@RestController
@RequiredArgsConstructor
@Api(tags = API_V1 + WEBHOOK_EVENTS)
@RequestMapping(value = API_V1 + WEBHOOK_EVENTS, produces = MediaType.APPLICATION_JSON_VALUE)
public class WebhookEventResource {

    private final WebhookEventService webhookEventService;

    private final WebhookEventTranslator eventTranslator;

    @GetMapping
    @ApiOperation("Gets webhook events with criteria")
    @ErrorInfoCodes({})
    @PreAuthorize(IS_AN_ADMINISTRATOR_OR_OAUTH_WEBHOOK_EVENT_READ_SCOPE)
    public PageDto<WebhookEventSummaryDto> eventSummariesGet(@Validated @ModelAttribute WebhookEventCriteriaDto criteriaDto) {
        EventSearchCriteria criteria = eventTranslator.toCriteria(criteriaDto);
        Page<WebhookEventSummary> eventSummaries = webhookEventService.getEvents(criteria);
        return PageDto.getDto(eventSummaries.map(eventTranslator::toSummaryDto));
    }

    @GetMapping(EVENT_ID)
    @ApiOperation("Gets webhook event by id")
    @ErrorInfoCodes({})
    @PreAuthorize(IS_AN_ADMINISTRATOR_OR_OAUTH_WEBHOOK_EVENT_READ_SCOPE)
    public WebhookEventDto eventsGet(@PathVariable(EVENT_ID_VAR) String eventId) {
        return eventTranslator.toDto(webhookEventService.getEvent(eventId));
    }

    @ExceptionHandler(WebhookEventNotFoundException.class)
    ResponseEntity<ErrorResponse> handle(WebhookEventNotFoundException ex) {
        return ErrorResponse.getErrorResponseEntity(WEBHOOK_EVENT_NOT_FOUND);
    }

    @ExceptionHandler(InvalidNameVersionCriteriaParam.class)
    ResponseEntity<ErrorResponse> handle(InvalidNameVersionCriteriaParam ex) {
        return ErrorResponse.getErrorResponseEntity(INVALID_WEBHOOK_EVENT_TYPE_PARAM);
    }

    @ExceptionHandler(EventTypeNotFound.class)
    ResponseEntity<ErrorResponse> handle(EventTypeNotFound ex) {
        return ErrorResponse.getErrorResponseEntity(WEBHOOK_EVENT_TYPE_NOT_FOUND);
    }

}
