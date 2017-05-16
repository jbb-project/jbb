/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.webapp.e2e.commons;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;

public class UserInUcpSteps extends ScenarioSteps {
    UcpPage ucpPage;

    @Step
    public void opens_ucp() {
        ucpPage.open();
    }

    @Step
    public void choose_profile_tab() {
        ucpPage.click_on_profile_tab();
    }

    @Step
    public void choose_edit_profile_option() {
        ucpPage.click_on_edit_profile_option();
    }

    @Step
    public void choose_edit_account_settings_option() {
        ucpPage.click_on_edit_account_option();
    }
}
