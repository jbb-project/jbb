/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.commons.security;

import java.util.Arrays;
import java.util.Optional;

public enum OAuthScope {
    // board scopes
    BOARD_READ("board:read", "Read board structure"),
    BOARD_READ_WRITE("board:write", "Read & write board structure"),
    BOARD_SETTINGS_READ("board_settings:read", "Read board settings"),
    BOARD_SETTINGS_READ_WRITE("board_settings:write", "Read & write board settings"),

    // posting scopes
    POST_READ("post:read", "Read topics and posts"),
    POST_READ_SEARCH("post:search", "Read & search topics and posts"),
    POST_READ_CREATE("post:create", "Read & create topics and posts"),
    POST_READ_UPDATE("post:update", "Read & update topics and posts"),
    POST_READ_DELETE("post:delete", "Read & delete topics and posts"),
    POST_READ_WRITE("post:write", "Read & search & create & update & delete topics and posts"),

    // frontend scopes
    FAQ_READ("faq:read", "Read FAQ"),
    FAQ_READ_WRITE("faq:write", "Read & write FAQ"),
    FORMAT_SETTINGS_READ("format_settings:read", "Read format settings"),
    FORMAT_SETTINGS_READ_WRITE("format_settings:write", "Read & write format settings"),

    // member scopes
    MEMBER_READ("member:read", "Read members"),
    MEMBER_READ_CREATE("member:create", "Read & create members"),
    MEMBER_READ_DELETE("member:delete", "Read & delete members"),
    MEMBER_READ_WRITE("member:write", "Read & create & delete members"),
    MEMBER_PROFILE_READ("member.profile:read", "Read member profiles"),
    MEMBER_PROFILE_READ_WRITE("member.profile:write", "Read & write member profiles"),
    MEMBER_ACCOUNT_READ("member.account:read", "Read member accounts"),
    MEMBER_ACCOUNT_READ_WRITE("member.account:write", "Read & write member accounts"),
    REGISTRATION_SETTINGS_READ("registration_settings:read", "Read registration settings"),
    REGISTRATION_SETTINGS_READ_WRITE("registration_settings:write", "Read & write registration settings"),
    MEMBER_SSE_STREAM_READ("member.sse_stream:read", "Read SSE stream with events related to member"),

    // security scopes
    MEMBER_LOCK_READ("member.lock:read", "Read member locks"),
    MEMBER_LOCK_READ_DELETE("member.lock:delete", "Read & delete member locks"),
    LOCKOUT_SETTINGS_READ("lockout_settings:read", "Read lockout settings"),
    LOCKOUT_SETTINGS_READ_WRITE("lockout_settings:write", "Read & write lockout settings"),
    PASSWORD_POLICY_READ("password_policy:read", "Read password policy"),
    PASSWORD_POLICY_READ_WRITE("password_policy:write", "Read & write password policy"),
    ADMINISTRATOR_PRIVILEGE_READ("admin_privilege:read", "Read administrator privileges"),
    ADMINISTRATOR_PRIVILEGE_READ_WRITE("admin_privilege:write", "Read & write administrator privileges"),
    OAUTH_CLIENT_READ("oauth_client:read", "Read OAuth clients"),
    OAUTH_CLIENT_READ_WRITE("oauth_client:write", "Read & write OAuth clients"),
    SIGN_IN_SETTINGS_READ("sign_in_settings:read", "Read sign in settings"),
    SIGN_IN_SETTINGS_READ_WRITE("sign_in_settings:write", "Read & write sign in settings"),
    API_SCOPES_READ("api_scopes:read", "Read API OAuth scopes"),

    // system scopes
    HEALTH_READ("health:read", "Read health status"),
    API_ERROR_CODES_READ("api_error_codes:read", "Read API error codes");

    private final String scopeName;
    private final String description;

    OAuthScope(String scopeName, String description) {
        this.scopeName = scopeName;
        this.description = description;
    }

    public static Optional<OAuthScope> ofName(String name) {
        return Arrays.stream(OAuthScope.values())
                .filter(scope -> scope.getScopeName().equals(name))
                .findFirst();
    }

    public String getDescription() {
        return description;
    }

    public String getScopeName() {
        return scopeName;
    }
}
