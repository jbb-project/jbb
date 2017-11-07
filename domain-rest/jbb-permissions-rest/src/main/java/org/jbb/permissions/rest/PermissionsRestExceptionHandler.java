/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.rest;

import static org.jbb.lib.restful.RestConfig.DOMAIN_REST_CONTROLLER_ADVICE_ORDER;

import org.jbb.lib.restful.domain.ErrorInfo;
import org.jbb.lib.restful.error.ErrorDetail;
import org.jbb.lib.restful.error.ErrorResponse;
import org.jbb.permissions.api.exceptions.PermissionRequiredException;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@Order(DOMAIN_REST_CONTROLLER_ADVICE_ORDER)
@ControllerAdvice(annotations = RestController.class)
public class PermissionsRestExceptionHandler {

    @ExceptionHandler(PermissionRequiredException.class)
    public ResponseEntity<ErrorResponse> handle(PermissionRequiredException ex) {
        ErrorResponse errorResponse = ErrorResponse.createFrom(ErrorInfo.MISSING_PERMISSION);
        errorResponse.getDetails().add(new ErrorDetail("missingPermission", ex.getMessage()));
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

}
