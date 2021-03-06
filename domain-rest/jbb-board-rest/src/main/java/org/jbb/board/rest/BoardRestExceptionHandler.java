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

import org.jbb.board.api.forum.ForumCategoryException;
import org.jbb.board.api.forum.ForumCategoryNotFoundException;
import org.jbb.board.api.forum.ForumException;
import org.jbb.board.api.forum.ForumNotFoundException;
import org.jbb.board.api.forum.PositionException;
import org.jbb.lib.restful.error.DefaultRestExceptionMapper;
import org.jbb.lib.restful.error.ErrorResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

import javax.validation.ConstraintViolation;

import lombok.RequiredArgsConstructor;

import static org.jbb.lib.restful.RestConfig.DOMAIN_REST_CONTROLLER_ADVICE_ORDER;
import static org.jbb.lib.restful.domain.ErrorInfo.FORUM_CATEGORY_NOT_FOUND;
import static org.jbb.lib.restful.domain.ErrorInfo.FORUM_NOT_FOUND;
import static org.jbb.lib.restful.domain.ErrorInfo.INVALID_FORUM;
import static org.jbb.lib.restful.domain.ErrorInfo.INVALID_FORUM_CATEGORY;
import static org.jbb.lib.restful.domain.ErrorInfo.TOO_LARGE_POSITION;

@RequiredArgsConstructor
@Order(DOMAIN_REST_CONTROLLER_ADVICE_ORDER)
@ControllerAdvice(annotations = RestController.class)
public class BoardRestExceptionHandler {

    private final DefaultRestExceptionMapper exceptionMapper;


    @ExceptionHandler(ForumCategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handle(ForumCategoryNotFoundException ex) {
        return ErrorResponse.getErrorResponseEntity(FORUM_CATEGORY_NOT_FOUND);
    }

    @ExceptionHandler(ForumNotFoundException.class)
    public ResponseEntity<ErrorResponse> handle(ForumNotFoundException ex) {
        return ErrorResponse.getErrorResponseEntity(FORUM_NOT_FOUND);
    }

    @ExceptionHandler(PositionException.class)
    public ResponseEntity<ErrorResponse> handle(PositionException ex) {
        return ErrorResponse.getErrorResponseEntity(TOO_LARGE_POSITION);
    }

    @ExceptionHandler(ForumCategoryException.class)
    public ResponseEntity<ErrorResponse> handle(ForumCategoryException ex) {
        ErrorResponse errorResponse = ErrorResponse.createFrom(INVALID_FORUM_CATEGORY);
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();

        constraintViolations.stream()
                .map(exceptionMapper::mapToErrorDetail)
                .forEach(errorResponse.getDetails()::add);

        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

    @ExceptionHandler(ForumException.class)
    public ResponseEntity<ErrorResponse> handle(ForumException ex) {
        ErrorResponse errorResponse = ErrorResponse.createFrom(INVALID_FORUM);
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();

        constraintViolations.stream()
                .map(exceptionMapper::mapToErrorDetail)
                .forEach(errorResponse.getDetails()::add);

        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

}
