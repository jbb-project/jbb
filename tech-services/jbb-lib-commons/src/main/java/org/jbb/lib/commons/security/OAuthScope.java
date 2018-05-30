/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.commons.security;

public enum OAuthScope {
    // board scopes
    BOARD_READ("Read board structure"),
    BOARD_READ_WRITE("Read & write board structure"),
    BOARD_SETTINGS_READ("Read board settings"),
    BOARD_SETTINGS_READ_WRITE("Read & write board settings"),

    // frontend scopes
    FAQ_READ("Read FAQ"),
    FAQ_READ_WRITE("Read & write FAQ"),
    FORMAT_SETTINGS_READ("Read format settings"),
    FORMAT_SETTINGS_READ_WRITE("Read & write format settings"),

    // member scopes
    MEMBER_READ("Read members"),
    MEMBER_READ_WRITE("Read & write members"),
    MEMBER_PROFILE_READ("Read member profiles"),
    MEMBER_PROFILE_READ_WRITE("Read & write member profiles"),
    MEMBER_ACCOUNT_READ("Read member accounts"),
    MEMBER_ACCOUNT_READ_WRITE("Read & write member accounts"),
    REGISTRATION_SETTINGS_READ("Read registration settings"),
    REGISTRATION_SETTINGS_READ_WRITE("Read & write registration settings"),

    // security scopes
    MEMBER_LOCK_READ("Read member locks"),
    MEMBER_LOCK_READ_WRITE("Read & write member locks"),
    LOCKOUT_SETTINGS_READ("Read lockout settings"),
    LOCKOUT_SETTINGS_READ_WRITE("Read & write lockout settings"),
    PASSWORD_POLICY_READ("Read password policy"),
    PASSWORD_POLICY_READ_WRITE("Read & write password policy"),
    ADMINISTRATOR_PRIVILEGE_READ("Read administrator privileges"),
    ADMINISTRATOR_PRIVILEGE_READ_WRITE("Read & write administrator privileges"),
    OAUTH_CLIENT_READ("Read OAuth clients"),
    OAUTH_CLIENT_READ_WRITE("Read & write OAuth clients"),
    API_SCOPES_READ("Read API OAuth scopes"),

    // system scopes
    HEALTH_READ("Read health status"),
    API_ERROR_CODES_READ("Read API error codes");

    private final String description;

    OAuthScope(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
