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

import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.WithTagValuesOf;

import org.jbb.e2e.serenity.Tags.Feature;
import org.jbb.e2e.serenity.Tags.Release;
import org.jbb.e2e.serenity.web.EndToEndWebStories;
import org.jbb.e2e.serenity.web.signin.SignInSteps;
import org.junit.Test;

import static org.jbb.e2e.serenity.Tags.Interface;
import static org.jbb.e2e.serenity.Tags.Type;

public class MetricsSettingsStories extends EndToEndWebStories {

    @Steps
    SignInSteps signInSteps;
    @Steps
    AcpMetricsSettingsSteps acpMetricsSettingsSteps;

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.METRICS_MANAGEMENT, Release.VER_0_11_0})
    public void update_metric_console_reporter_period_to_empty_is_impossible() {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        acpMetricsSettingsSteps.open_metrics_settings();
        acpMetricsSettingsSteps.type_console_reporter_period_seconds("");
        acpMetricsSettingsSteps.save_metrics_settings_form();

        // then
        acpMetricsSettingsSteps.should_be_informed_about_invalid_value();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.METRICS_MANAGEMENT, Release.VER_0_11_0})
    public void update_metric_console_reporter_period_to_text_is_impossible() {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        acpMetricsSettingsSteps.open_metrics_settings();
        acpMetricsSettingsSteps.type_console_reporter_period_seconds("ahah");
        acpMetricsSettingsSteps.save_metrics_settings_form();

        // then
        acpMetricsSettingsSteps.should_be_informed_about_invalid_value();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.METRICS_MANAGEMENT, Release.VER_0_11_0})
    public void update_metric_console_reporter_period_to_negative_number_is_impossible() {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        acpMetricsSettingsSteps.open_metrics_settings();
        acpMetricsSettingsSteps.type_console_reporter_period_seconds("-1");
        acpMetricsSettingsSteps.save_metrics_settings_form();

        // then
        acpMetricsSettingsSteps.should_be_informed_that_value_must_be_positive_number();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.METRICS_MANAGEMENT, Release.VER_0_11_0})
    public void update_metric_console_reporter_period_to_zero_is_impossible() {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        acpMetricsSettingsSteps.open_metrics_settings();
        acpMetricsSettingsSteps.type_console_reporter_period_seconds("0");
        acpMetricsSettingsSteps.save_metrics_settings_form();

        // then
        acpMetricsSettingsSteps.should_be_informed_that_value_must_be_positive_number();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.METRICS_MANAGEMENT, Release.VER_0_11_0})
    public void update_metric_csv_reporter_period_to_empty_is_impossible() {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        acpMetricsSettingsSteps.open_metrics_settings();
        acpMetricsSettingsSteps.type_csv_reporter_period_seconds("");
        acpMetricsSettingsSteps.save_metrics_settings_form();

        // then
        acpMetricsSettingsSteps.should_be_informed_about_invalid_value();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.METRICS_MANAGEMENT, Release.VER_0_11_0})
    public void update_metric_csv_reporter_period_to_text_is_impossible() {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        acpMetricsSettingsSteps.open_metrics_settings();
        acpMetricsSettingsSteps.type_csv_reporter_period_seconds("ahah");
        acpMetricsSettingsSteps.save_metrics_settings_form();

        // then
        acpMetricsSettingsSteps.should_be_informed_about_invalid_value();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.METRICS_MANAGEMENT, Release.VER_0_11_0})
    public void update_metric_csv_reporter_period_to_negative_number_is_impossible() {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        acpMetricsSettingsSteps.open_metrics_settings();
        acpMetricsSettingsSteps.type_csv_reporter_period_seconds("-1");
        acpMetricsSettingsSteps.save_metrics_settings_form();

        // then
        acpMetricsSettingsSteps.should_be_informed_that_value_must_be_positive_number();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.METRICS_MANAGEMENT, Release.VER_0_11_0})
    public void update_metric_csv_reporter_period_to_zero_is_impossible() {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        acpMetricsSettingsSteps.open_metrics_settings();
        acpMetricsSettingsSteps.type_csv_reporter_period_seconds("0");
        acpMetricsSettingsSteps.save_metrics_settings_form();

        // then
        acpMetricsSettingsSteps.should_be_informed_that_value_must_be_positive_number();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.METRICS_MANAGEMENT, Release.VER_0_11_0})
    public void update_metric_log_reporter_period_to_empty_is_impossible() {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        acpMetricsSettingsSteps.open_metrics_settings();
        acpMetricsSettingsSteps.type_log_reporter_period_seconds("");
        acpMetricsSettingsSteps.save_metrics_settings_form();

        // then
        acpMetricsSettingsSteps.should_be_informed_about_invalid_value();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.METRICS_MANAGEMENT, Release.VER_0_11_0})
    public void update_metric_log_reporter_period_to_text_is_impossible() {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        acpMetricsSettingsSteps.open_metrics_settings();
        acpMetricsSettingsSteps.type_log_reporter_period_seconds("ahah");
        acpMetricsSettingsSteps.save_metrics_settings_form();

        // then
        acpMetricsSettingsSteps.should_be_informed_about_invalid_value();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.METRICS_MANAGEMENT, Release.VER_0_11_0})
    public void update_metric_log_reporter_period_to_negative_number_is_impossible() {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        acpMetricsSettingsSteps.open_metrics_settings();
        acpMetricsSettingsSteps.type_log_reporter_period_seconds("-1");
        acpMetricsSettingsSteps.save_metrics_settings_form();

        // then
        acpMetricsSettingsSteps.should_be_informed_that_value_must_be_positive_number();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.METRICS_MANAGEMENT, Release.VER_0_11_0})
    public void update_metric_log_reporter_period_to_zero_is_impossible() {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        acpMetricsSettingsSteps.open_metrics_settings();
        acpMetricsSettingsSteps.type_log_reporter_period_seconds("0");
        acpMetricsSettingsSteps.save_metrics_settings_form();

        // then
        acpMetricsSettingsSteps.should_be_informed_that_value_must_be_positive_number();
    }

}
