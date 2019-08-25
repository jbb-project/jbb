/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.posting.rest;

import org.jbb.lib.restful.error.ErrorResponse;
import org.jbb.posting.api.exception.PostNotFoundException;
import org.jbb.posting.api.exception.TopicNotFoundException;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import static org.jbb.lib.restful.RestConfig.DOMAIN_REST_CONTROLLER_ADVICE_ORDER;
import static org.jbb.lib.restful.domain.ErrorInfo.POST_NOT_FOUND;
import static org.jbb.lib.restful.domain.ErrorInfo.TOPIC_NOT_FOUND;

@Order(DOMAIN_REST_CONTROLLER_ADVICE_ORDER)
@ControllerAdvice(annotations = RestController.class)
public class PostingRestExceptionHandler {

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<ErrorResponse> handle(PostNotFoundException ex) {
        return ErrorResponse.getErrorResponseEntity(POST_NOT_FOUND);
    }

    @ExceptionHandler(TopicNotFoundException.class)
    public ResponseEntity<ErrorResponse> handle(TopicNotFoundException ex) {
        return ErrorResponse.getErrorResponseEntity(TOPIC_NOT_FOUND);
    }

}
