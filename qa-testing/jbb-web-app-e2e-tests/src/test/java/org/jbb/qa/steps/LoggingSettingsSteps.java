/*
 * Copyright (C) 2017 the original author or authors.
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

import org.jbb.qa.pages.AcpLoggingSettingsPage;

public class LoggingSettingsSteps extends ScenarioSteps {
    private AcpLoggingSettingsPage loggingSettingsPage;

    @Step
    public void open_logging_settings_page() {
        loggingSettingsPage.open();
    }

    @Step
    public void select_stacktrace_visibility_level(String level) {
        loggingSettingsPage.chooseStackTraceVisibilityLevel(level);
    }

    @Step
    public void send_logging_settings_form() {
        loggingSettingsPage.clickSaveButton();
    }


}
