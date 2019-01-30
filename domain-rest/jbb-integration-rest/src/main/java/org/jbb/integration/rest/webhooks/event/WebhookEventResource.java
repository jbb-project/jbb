/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.integration.rest.webhooks.event;

import com.google.common.collect.Lists;

import org.jbb.integration.api.webhooks.event.EventSearchCriteria;
import org.jbb.integration.api.webhooks.event.WebhookEventNotFoundException;
import org.jbb.integration.api.webhooks.event.WebhookEventService;
import org.jbb.integration.api.webhooks.event.WebhookEventSummary;
import org.jbb.integration.rest.webhooks.event.exception.InvalidNameVersionCriteriaParam;
import org.jbb.lib.restful.domain.ErrorInfoCodes;
import org.jbb.lib.restful.error.ErrorResponse;
import org.jbb.lib.restful.paging.PageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import static org.jbb.integration.rest.IntegrationRestAuthorize.IS_AN_ADMINISTRATOR_OR_OAUTH_WEBHOOK_EVENT_READ_WRITE_SCOPE;
import static org.jbb.integration.rest.IntegrationRestAuthorize.IS_AN_ADMINISTRATOR_OR_OAUTH_WEBHOOK_READ_SCOPE;
import static org.jbb.integration.rest.IntegrationRestConstants.EVENT_ID;
import static org.jbb.integration.rest.IntegrationRestConstants.EVENT_ID_VAR;
import static org.jbb.integration.rest.IntegrationRestConstants.WEBHOOK_EVENTS;
import static org.jbb.lib.restful.RestConstants.API_V1;
import static org.jbb.lib.restful.domain.ErrorInfo.FORBIDDEN;
import static org.jbb.lib.restful.domain.ErrorInfo.UNAUTHORIZED;
import static org.jbb.lib.restful.domain.ErrorInfo.WEBHOOK_EVENT_NOT_FOUND;

@RestController
@RequiredArgsConstructor
@Api(tags = API_V1 + WEBHOOK_EVENTS)
@RequestMapping(value = API_V1 + WEBHOOK_EVENTS, produces = MediaType.APPLICATION_JSON_VALUE)
public class WebhookEventResource {

    private final WebhookEventService webhookEventService;

    private final WebhookEventTranslator eventTranslator;

    @GetMapping
    @ApiOperation("Gets webhook events with criteria")
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN})
    @PreAuthorize(IS_AN_ADMINISTRATOR_OR_OAUTH_WEBHOOK_READ_SCOPE)
    public PageDto<WebhookEventSummaryDto> eventSummariesGet(@Validated @ModelAttribute WebhookEventCriteriaDto criteriaDto) {
        EventSearchCriteria criteria;
        try {
            criteria = eventTranslator.toCriteria(criteriaDto);
        } catch (InvalidNameVersionCriteriaParam ex) {
            return PageDto.getDto(new PageImpl<>(Lists.newArrayList(), PageRequest.of(
                    criteriaDto.getPage(), criteriaDto.getPageSize()), 0L));
        }
        Page<WebhookEventSummary> eventSummaries = webhookEventService.getEvents(criteria);
        return PageDto.getDto(eventSummaries.map(eventTranslator::toSummaryDto));
    }

    @GetMapping(EVENT_ID)
    @ApiOperation("Gets webhook event by id")
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN, WEBHOOK_EVENT_NOT_FOUND})
    @PreAuthorize(IS_AN_ADMINISTRATOR_OR_OAUTH_WEBHOOK_READ_SCOPE)
    public WebhookEventDto eventGet(@PathVariable(EVENT_ID_VAR) String eventId) {
        return eventTranslator.toDto(webhookEventService.getEvent(eventId));
    }

    @DeleteMapping(EVENT_ID)
    @ApiOperation("Removes webhook event by id")
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN, WEBHOOK_EVENT_NOT_FOUND})
    @PreAuthorize(IS_AN_ADMINISTRATOR_OR_OAUTH_WEBHOOK_EVENT_READ_WRITE_SCOPE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eventDelete(@PathVariable(EVENT_ID_VAR) String eventId) {
        webhookEventService.deleteEvent(eventId);
    }

    @ExceptionHandler(WebhookEventNotFoundException.class)
    ResponseEntity<ErrorResponse> handle(WebhookEventNotFoundException ex) {
        return ErrorResponse.getErrorResponseEntity(WEBHOOK_EVENT_NOT_FOUND);
    }

}
