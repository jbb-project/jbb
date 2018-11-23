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

import lombok.experimental.UtilityClass;

@UtilityClass
public class BoardRestAuthorize {

    public static final String PERMIT_ALL_OR_OAUTH_BOARD_READ_SCOPE =
            "#api.notOAuthOrHasAnyScope('BOARD_READ', 'BOARD_READ_WRITE')";

    public static final String IS_AN_ADMINISTRATOR_OR_OAUTH_BOARD_READ_WRITE_SCOPE =
            "#api.isAdministratorOrHasAnyScope('BOARD_READ_WRITE')";

    public static final String PERMIT_ALL_OR_OAUTH_BOARD_SETTINGS_READ_SCOPE =
            "#api.notOAuthOrHasAnyScope('BOARD_SETTINGS_READ', 'BOARD_SETTINGS_READ_WRITE')";

    public static final String IS_AN_ADMINISTRATOR_OR_OAUTH_BOARD_SETTINGS_READ_WRITE_SCOPE =
            "#api.isAdministratorOrHasAnyScope('BOARD_SETTINGS_READ_WRITE')";

}
