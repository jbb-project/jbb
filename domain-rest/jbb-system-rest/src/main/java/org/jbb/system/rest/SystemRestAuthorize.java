/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.rest;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SystemRestAuthorize {

    public static final String PERMIT_ALL_OR_OAUTH_HEALTH_READ_SCOPE =
            "#api.notOAuthOrHasAnyScope('HEALTH_READ')";

    public static final String PERMIT_ALL_OR_OAUTH_API_ERROR_CODES_READ_SCOPE =
            "#api.notOAuthOrHasAnyScope('API_ERROR_CODES_READ')";

}
