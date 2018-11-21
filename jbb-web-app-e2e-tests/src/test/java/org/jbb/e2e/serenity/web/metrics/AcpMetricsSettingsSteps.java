/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.web.metrics;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;

public class AcpMetricsSettingsSteps extends ScenarioSteps {

    AcpMetricsSettingsPage acpMetricsSettingsPage;

    @Step
    public void open_metrics_settings() {
        acpMetricsSettingsPage.open();
    }

    @Step
    public void save_metrics_settings_form() {
        acpMetricsSettingsPage.clickSaveButton();
    }

    @Step
    public void type_console_reporter_period_seconds(String seconds) {
        acpMetricsSettingsPage.typeConsoleReporterPeriodSeconds(seconds);
    }

    @Step
    public void type_csv_reporter_period_seconds(String seconds) {
        acpMetricsSettingsPage.typeCsvReporterPeriodSeconds(seconds);
    }

    @Step
    public void type_log_reporter_period_seconds(String seconds) {
        acpMetricsSettingsPage.typeLogReporterPeriodSeconds(seconds);
    }

    @Step
    public void should_be_informed_about_invalid_value() {
        acpMetricsSettingsPage.shouldBeVisibleInfoAboutInvalidValue();
    }

    @Step
    public void should_be_informed_that_value_must_be_positive_number() {
        acpMetricsSettingsPage.shouldBeVisibleInfoAboutPositiveValue();
    }
}
