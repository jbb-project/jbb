/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.board.rest;

import org.jbb.board.api.forum.ForumCategoryNotFoundException;
import org.jbb.lib.restful.error.ErrorResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import static org.jbb.lib.restful.RestConfig.DOMAIN_REST_CONTROLLER_ADVICE_ORDER;
import static org.jbb.lib.restful.domain.ErrorInfo.FORUM_CATEGORY_NOT_FOUND;

@Order(DOMAIN_REST_CONTROLLER_ADVICE_ORDER)
@ControllerAdvice(annotations = RestController.class)
public class BoardRestExceptionHandler {

    @ExceptionHandler(ForumCategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handle(ForumCategoryNotFoundException ex) {
        return ErrorResponse.getErrorResponseEntity(FORUM_CATEGORY_NOT_FOUND);
    }

}
