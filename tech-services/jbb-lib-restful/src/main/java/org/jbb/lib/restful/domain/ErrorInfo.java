/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.restful.domain;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorInfo {
    // generic technical errors
    NOT_INSTALLED(HttpStatus.BAD_REQUEST, "JBB-000", "Application not installed"),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "JBB-001", "Internal error"),
    METHOD_NOT_SUPPORTED(HttpStatus.METHOD_NOT_ALLOWED, "JBB-002", "Http method is not supported"),
    UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "JBB-003",
        "Given media type is not supported"),
    NOT_ACCEPTABLE_MEDIA_TYPE(HttpStatus.NOT_ACCEPTABLE, "JBB-004",
        "Given media type is not acceptable"),
    MISSING_PATH_VARIABLE(HttpStatus.BAD_REQUEST, "JBB-005", "Missing path variable"),
    MISSING_REQUEST_PARAMETER(HttpStatus.BAD_REQUEST, "JBB-006", "Missing request parameter"),
    REQUEST_BINDING_ERROR(HttpStatus.BAD_REQUEST, "JBB-007", "Request binding error"),
    CONVERSION_NOT_SUPPORTED(HttpStatus.INTERNAL_SERVER_ERROR, "JBB-008",
        "Conversion is not supported"),
    TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "JBB-009", "Type mismatch"),
    MESSAGE_NOT_READABLE(HttpStatus.BAD_REQUEST, "JBB-010", "Message is malformed"),
    MESSAGE_NOT_WRITABLE(HttpStatus.INTERNAL_SERVER_ERROR, "JBB-011", "Message cannot be written"),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "JBB-012", "Validation error"),
    MISSING_REQUEST_PART(HttpStatus.BAD_REQUEST, "JBB-013", "Missing request part"),
    BIND_ERROR(HttpStatus.BAD_REQUEST, "JBB-014", "Bind error"),
    NO_HANDLER_FOUND(HttpStatus.NOT_FOUND, "JBB-015", "Not found"),
    ASYNC_REQUEST_TIMEOUT(HttpStatus.INTERNAL_SERVER_ERROR, "JBB-016", "Async request timeout"),

    // authentication & authorization errors
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "JBB-050", "Access denied - unauthorized"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "JBB-051", "Access denied - forbidden"),
    MISSING_PERMISSION(HttpStatus.FORBIDDEN, "JBB-052", "Missing permission for making request"),

    // member related errors
    REGISTRATION_FAILED(HttpStatus.BAD_REQUEST, "JBB-100", "Incorrect registration data"),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "JBB-101", "Member not found"),
    UPDATE_ACCOUNT_FAILED(HttpStatus.BAD_REQUEST, "JBB-102", "Incorrect update account data"),
    BAD_CREDENTIALS(HttpStatus.BAD_REQUEST, "JBB-103", "Bad credentials provided");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorInfo(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public static String joinedMessages(List<ErrorInfo> errors) {
        return errors.stream()
            .sorted(Comparator.comparing(ErrorInfo::getCode))
            .map(error -> error.getCode() + ": " + error.getMessage())
            .collect(Collectors.joining(",\n"));
    }
}
