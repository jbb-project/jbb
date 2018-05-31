/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.rest;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MembersRestAuthorize {

    public static final String PERMIT_ALL_OR_OAUTH_MEMBER_READ_SCOPE =
            "not #oauth2.isOAuth() or #oauth2.hasAnyScope('MEMBER_READ', 'MEMBER_READ_WRITE')";

    public static final String IS_AN_ADMINISTRATOR_OR_OAUTH_MEMBER_READ_WRITE_SCOPE =
            "(hasRole('ROLE_ADMINISTRATOR') and not #oauth2.isOAuth()) or #oauth2.hasAnyScope('MEMBER_READ_WRITE')";

    public static final String PERMIT_ALL_OR_OAUTH_MEMBER_READ_WRITE_SCOPE =
            "not #oauth2.isOAuth() or #oauth2.hasAnyScope('MEMBER_READ_WRITE')";

    public static final String IS_AUTHENTICATED_OR_OAUTH_MEMBER_PROFILE_READ_SCOPE =
            "(isAuthenticated() and not #oauth2.isOAuth()) or #oauth2.hasAnyScope('MEMBER_PROFILE_READ', 'MEMBER_PROFILE_READ_WRITE')";

    public static final String IS_AUTHENTICATED_OR_OAUTH_MEMBER_PROFILE_READ_WRITE_SCOPE =
            "(isAuthenticated() and not #oauth2.isOAuth()) or #oauth2.hasAnyScope('MEMBER_PROFILE_READ_WRITE')";

    public static final String IS_AUTHENTICATED_OR_OAUTH_MEMBER_ACCOUNT_READ_SCOPE =
            "(isAuthenticated() and not #oauth2.isOAuth()) or #oauth2.hasAnyScope('MEMBER_ACCOUNT_READ', 'MEMBER_ACCOUNT_READ_WRITE')";

    public static final String IS_AUTHENTICATED_OR_OAUTH_MEMBER_ACCOUNT_READ_WRITE_SCOPE =
            "(isAuthenticated() and not #oauth2.isOAuth()) or #oauth2.hasAnyScope('MEMBER_ACCOUNT_READ_WRITE')";

    public static final String IS_AN_ADMINISTRATOR_OR_OAUTH_REGISTRATION_SETTINGS_READ_SCOPE =
            "(hasRole('ROLE_ADMINISTRATOR') and not #oauth2.isOAuth()) or #oauth2.hasAnyScope('REGISTRATION_SETTINGS_READ', 'REGISTRATION_SETTINGS_READ_WRITE')";

    public static final String IS_AN_ADMINISTRATOR_OR_OAUTH_REGISTRATION_SETTINGS_READ_WRITE_SCOPE =
            "(hasRole('ROLE_ADMINISTRATOR') and not #oauth2.isOAuth()) or #oauth2.hasAnyScope('REGISTRATION_SETTINGS_READ_WRITE')";

}
