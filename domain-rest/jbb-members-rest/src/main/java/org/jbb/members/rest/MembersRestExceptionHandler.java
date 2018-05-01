/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.rest;

import org.jbb.lib.restful.error.ErrorResponse;
import org.jbb.members.api.base.MemberNotFoundException;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import static org.jbb.lib.restful.RestConfig.DOMAIN_REST_CONTROLLER_ADVICE_ORDER;
import static org.jbb.lib.restful.domain.ErrorInfo.MEMBER_NOT_FOUND;

@Order(DOMAIN_REST_CONTROLLER_ADVICE_ORDER)
@ControllerAdvice(annotations = RestController.class)
public class MembersRestExceptionHandler {

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ErrorResponse> handle(MemberNotFoundException ex) {
        return ErrorResponse.getErrorResponseEntity(MEMBER_NOT_FOUND);
    }

}
