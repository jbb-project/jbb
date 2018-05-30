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
    BOARD_READ("board_read", "Read board structure"),
    BOARD_READ_WRITE("board_read_write", "Read & write board structure"),
    BOARD_SETTINGS_READ("board_settings_read", "Read board settings"),
    BOARD_SETTINGS_READ_WRITE("board_settings_read_write", "Read & write board settings"),

    // frontend scopes
    FAQ_READ("faq_read", "Read FAQ"),
    FAQ_READ_WRITE("faq_read_write", "Read & write FAQ"),
    FORMAT_SETTINGS_READ("format_settings_read", "Read format settings"),
    FORMAT_SETTINGS_READ_WRITE("format_settings_read_write", "Read & write format settings"),

    // member scopes
    MEMBER_READ("member_read", "Read members"),
    MEMBER_READ_WRITE("member_read_write", "Read & write members"),
    MEMBER_PROFILE_READ("member_profile_read", "Read member profiles"),
    MEMBER_PROFILE_READ_WRITE("member_profile_read_write", "Read & write member profiles"),
    MEMBER_ACCOUNT_READ("member_account_read", "Read member accounts"),
    MEMBER_ACCOUNT_READ_WRITE("member_account_read_write", "Read & write member accounts"),
    REGISTRATION_SETTINGS_READ("registration_settings_read", "Read registration settings"),
    REGISTRATION_SETTINGS_READ_WRITE("registration_settings_read_write", "Read & write registration settings"),

    // security scopes
    MEMBER_LOCK_READ("member_lock_read", "Read member locks"),
    MEMBER_LOCK_READ_WRITE("member_lock_read_write", "Read & write member locks"),
    LOCKOUT_SETTINGS_READ("lockout_settings_read", "Read lockout settings"),
    LOCKOUT_SETTINGS_READ_WRITE("lockout_settings_read_write", "Read & write lockout settings"),
    PASSWORD_POLICY_READ("password_policy_read", "Read password policy"),
    PASSWORD_POLICY_READ_WRITE("password_policy_read_write", "Read & write password policy"),
    ADMINISTRATOR_PRIVILEGE_READ("administrator_privilege_read", "Read administrator privileges"),
    ADMINISTRATOR_PRIVILEGE_READ_WRITE("administrator_privilege_read_write", "Read & write administrator privileges"),

    // system scopes
    HEALTH_READ("health_read", "Read health status"),
    API_ERROR_CODES_READ("api_error_codes_read", "Read API error codes");

    private final String name;
    private final String description;

    OAuthScope(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
