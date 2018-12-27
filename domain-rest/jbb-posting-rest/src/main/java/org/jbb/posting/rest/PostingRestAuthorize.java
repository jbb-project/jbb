/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.posting.rest;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PostingRestAuthorize {

    public static final String PERMIT_ALL_OR_OAUTH_POST_READ_SCOPE =
            "#api.notOAuthOrHasAnyScope('post:read', 'post:search', 'post:create', 'post:update', 'post:delete', 'post:write')";

    public static final String PERMIT_ALL_OR_OAUTH_POST_SEARCH_SCOPE =
            "#api.notOAuthOrHasAnyScope('post:search', 'post:write')";

    public static final String PERMIT_ALL_OR_OAUTH_POST_CREATE_SCOPE =
            "#api.notOAuthOrHasAnyScope('post:create', 'post:write')";


    public static final String PERMIT_ALL_OR_OAUTH_POST_UPDATE_SCOPE =
            "#api.notOAuthOrHasAnyScope('post:update', 'post:write')";

    public static final String PERMIT_ALL_OR_OAUTH_POST_DELETE_SCOPE =
            "#api.notOAuthOrHasAnyScope('post:delete', 'post:write')";

    public static final String IS_AN_ADMINISTRATOR_OR_OAUTH_BOARD_READ_WRITE_SCOPE =
            "#api.isAdministratorOrHasAnyScope('board:write')";

    public static final String PERMIT_ALL_OR_OAUTH_BOARD_SETTINGS_READ_SCOPE =
            "#api.notOAuthOrHasAnyScope('board_settings:read', 'board_settings:write')";

    public static final String IS_AN_ADMINISTRATOR_OR_OAUTH_BOARD_SETTINGS_READ_WRITE_SCOPE =
            "#api.isAdministratorOrHasAnyScope('board_settings:write')";

}
