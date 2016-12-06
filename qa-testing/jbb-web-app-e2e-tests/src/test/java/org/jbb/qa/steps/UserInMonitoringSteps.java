/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.qa.steps;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;

import org.jbb.qa.Utils;
import org.jbb.qa.pages.MonitoringPage;

import static org.assertj.core.api.Assertions.assertThat;

public class UserInMonitoringSteps extends ScenarioSteps {
    private MonitoringPage monitoringPage;

    @Step
    public void open_monitoring_page() {
        monitoringPage.open();
    }

    @Step
    public void should_see_sign_in_page() {
        assertThat(Utils.currentUrl()).contains("signin");
    }

    @Step
    public void should_see_403_error() {
        monitoringPage.should_contain_info_about_403_forbidden_error();
    }


}
