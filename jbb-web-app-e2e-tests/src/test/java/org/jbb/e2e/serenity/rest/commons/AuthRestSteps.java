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
import net.thucydides.core.steps.ScenarioSteps;
import org.jbb.e2e.serenity.rest.RestUtils;

public class AuthRestSteps extends ScenarioSteps {

    @Step
    public void include_admin_basic_auth_header_for_every_request() {
        include_basic_auth_header_for_every_request("administrator", "administrator");
    }

    @Step
    public void include_basic_auth_header_for_every_request(String username, String password) {
        RestUtils.setBasicAuth(username, password);
    }

    @Step
    public void remove_basic_auth_header_from_request() {
        RestUtils.cleanBasicAuth();
    }

}
