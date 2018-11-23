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
public class SecurityRestAuthorize {

    public static final String IS_AN_ADMINISTRATOR_OR_OAUTH_MEMBER_LOCK_READ_SCOPE =
            "#api.isAdministratorOrHasAnyScope('MEMBER_LOCK_READ', 'MEMBER_LOCK_READ_WRITE')";

    public static final String IS_AN_ADMINISTRATOR_OR_OAUTH_MEMBER_LOCK_READ_WRITE_SCOPE =
            "#api.isAdministratorOrHasAnyScope('MEMBER_LOCK_READ_WRITE')";

    public static final String IS_AN_ADMINISTRATOR_OR_OAUTH_LOCKOUT_SETTINGS_READ_SCOPE =
            "#api.isAdministratorOrHasAnyScope('LOCKOUT_SETTINGS_READ', 'LOCKOUT_SETTINGS_READ_WRITE')";

    public static final String IS_AN_ADMINISTRATOR_OR_OAUTH_LOCKOUT_SETTINGS_READ_WRITE_SCOPE =
            "#api.isAdministratorOrHasAnyScope('LOCKOUT_SETTINGS_READ_WRITE')";

    public static final String PERMIT_ALL_OR_OAUTH_PSWD_POLICY_READ_SCOPE =
            "#api.notOAuthOrHasAnyScope('PASSWORD_POLICY_READ', 'PASSWORD_POLICY_READ_WRITE')";

    public static final String IS_AN_ADMINISTRATOR_OR_OAUTH_PSWD_POLICY_READ_WRITE_SCOPE =
            "#api.isAdministratorOrHasAnyScope('PASSWORD_POLICY_READ_WRITE')";

    public static final String PERMIT_ALL_OR_OAUTH_ADMINISTRATOR_PRIVILEGE_READ_SCOPE =
            "#api.notOAuthOrHasAnyScope('ADMINISTRATOR_PRIVILEGE_READ', 'ADMINISTRATOR_PRIVILEGE_READ_WRITE')";

    public static final String IS_AN_ADMINISTRATOR_OR_OAUTH_ADMINISTRATOR_PRIVILEGE_READ_WRITE_SCOPE =
            "#api.isAdministratorOrHasAnyScope('ADMINISTRATOR_PRIVILEGE_READ_WRITE')";

    public static final String IS_AN_ADMINISTRATOR_OR_OAUTH_OAUTH_CLIENT_READ_SCOPE =
            "#api.isAdministratorOrHasAnyScope('OAUTH_CLIENT_READ', 'OAUTH_CLIENT_READ_WRITE')";

    public static final String IS_AN_ADMINISTRATOR_OR_OAUTH_OAUTH_CLIENT_READ_WRITE_SCOPE =
            "#api.isAdministratorOrHasAnyScope('OAUTH_CLIENT_READ_WRITE')";

    public static final String PERMIT_ALL_OR_OAUTH_API_SCOPES_READ_SCOPE =
            "#api.notOAuthOrHasAnyScope('API_SCOPES_READ')";

}
