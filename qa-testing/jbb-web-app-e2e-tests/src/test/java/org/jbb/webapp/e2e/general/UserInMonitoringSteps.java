/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.webapp.e2e.general;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;

import org.jbb.webapp.e2e.Utils;

import static org.assertj.core.api.Assertions.assertThat;

public class UserInMonitoringSteps extends ScenarioSteps {
    private MonitoringPage monitoringPage;

    @Step
    public void open_monitoring_page() {
        monitoringPage.open();
    }

    @Step
    public void should_see_sign_in_page() {
        assertThat(Utils.current_url()).contains("signin");
    }

    @Step
    public void should_see_403_error() {
        monitoringPage.should_contain_info_about_403_forbidden_error();
    }


}
