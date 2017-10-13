/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.rest.commons;

import net.thucydides.core.annotations.Step;
import org.jbb.e2e.serenity.rest.RestUtils;

public class AuthRestSteps {

    @Step
    public void includeBasicAuthHeaderToEveryRequest(String username, String password) {
        RestUtils.setBasicAuth(username, password);
    }

    @Step
    public void removeBasicAuthHeaderFromRequest() {
        RestUtils.cleanBasicAuth();
    }

}
