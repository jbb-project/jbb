/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.restful;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class RestAuthorize {

    public static final String PERMIT_ALL = "permitAll()";
    public static final String IS_AUTHENTICATED = "isAuthenticated()";
    public static final String IS_AN_ADMINISTRATOR = "hasRole('ROLE_ADMINISTRATOR')";

}
