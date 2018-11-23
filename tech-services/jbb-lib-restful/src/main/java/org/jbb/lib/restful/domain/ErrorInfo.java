/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.restful.domain;

import org.springframework.http.HttpStatus;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;

@Getter
public enum ErrorInfo {
    // generic technical errors
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

    // installation related errors
    NOT_INSTALLED(HttpStatus.BAD_REQUEST, "JBB-090", "Application not installed"),

    // member related errors
    REGISTRATION_FAILED(HttpStatus.BAD_REQUEST, "JBB-100", "Incorrect registration data"),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "JBB-101", "Member not found"),
    UPDATE_ACCOUNT_FAILED(HttpStatus.BAD_REQUEST, "JBB-102", "Incorrect update account data"),
    BAD_CREDENTIALS_WHEN_UPDATE_ACCOUNT(HttpStatus.BAD_REQUEST, "JBB-103",
            "Bad credentials provided with update account request"),
    UPDATE_NOT_OWN_ACCOUNT(HttpStatus.FORBIDDEN, "JBB-104", "Cannot update not own account"),
    GET_NOT_OWN_ACCOUNT(HttpStatus.FORBIDDEN, "JBB-105", "Cannot get not own account"),
    UPDATE_NOT_OWN_PROFILE(HttpStatus.FORBIDDEN, "JBB-106", "Cannot update not own profile"),
    GET_NOT_OWN_PROFILE(HttpStatus.FORBIDDEN, "JBB-107", "Cannot get not own profile"),
    UPDATE_PROFILE_FAILED(HttpStatus.BAD_REQUEST, "JBB-108", "incorrect update profile data"),

    // board related errors
    INVALID_BOARD_SETTINGS(HttpStatus.BAD_REQUEST, "JBB-200", "Board settings are invalid"),
    FORUM_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "JBB-201", "Forum category not found"),
    TARGET_FORUM_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "JBB-202", "Target forum category not found"),
    INVALID_FORUM_CATEGORY(HttpStatus.BAD_REQUEST, "JBB-203", "Invalid forum category"),
    FORUM_NOT_FOUND(HttpStatus.NOT_FOUND, "JBB-204", "Forum not found"),
    INVALID_FORUM(HttpStatus.BAD_REQUEST, "JBB-205", "Invalid forum"),
    TOO_LARGE_POSITION(HttpStatus.BAD_REQUEST, "JBB-206", "New position is too large"),

    // security related errors
    INVALID_PASSWORD_POLICY(HttpStatus.BAD_REQUEST, "JBB-300", "Password policy is invalid"),
    INVALID_LOCKOUT_SETTINGS(HttpStatus.BAD_REQUEST, "JBB-301", "Member lockout settings are invalid"),
    ACTIVE_MEMBER_LOCK_NOT_FOUND(HttpStatus.NOT_FOUND, "JBB-302", "Active member lock not found"),
    OAUTH_CLIENT_NOT_FOUND(HttpStatus.NOT_FOUND, "JBB-303", "OAuth client not found"),
    INVALID_OAUTH_CLIENT(HttpStatus.BAD_REQUEST, "JBB-304", "OAuth client is invalid"),
    UNKNOWN_OAUTH_SCOPE(HttpStatus.BAD_REQUEST, "JBB-305", "Unknown OAuth scope provided"),

    // frontend related errors
    INVALID_FORMAT_SETTINGS(HttpStatus.BAD_REQUEST, "JBB-400", "Format settings are invalid");

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
