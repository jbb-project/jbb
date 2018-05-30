/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.rest;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SecurityRestConstants {

    public static final String ACTIVE_LOCK = "/active-lock";

    public static final String ADMINISTRATOR_PRIVILEGES = "/administrator-privileges";

    public static final String API_OAUTH_SCOPES = "/api-oauth-scopes";

    public static final String CLIENT_ID_VAR = "clientId";
    public static final String CLIENT_ID = "/{" + CLIENT_ID_VAR + "}";

    public static final String CLIENT_SECRET = "/client-secret";

    public static final String MEMBERS = "/members";
    public static final String MEMBER_ID_VAR = "memberId";
    public static final String MEMBER_ID = "/{" + MEMBER_ID_VAR + "}";

    public static final String MEMBER_LOCKOUT_SETTINGS = "/member-lockout-settings";
    public static final String MEMBER_LOCKS = "/member-locks";

    public static final String OAUTH_CLIENTS = "/oauth-clients";

    public static final String PSWD_POLICY = "/password-policy";

}
