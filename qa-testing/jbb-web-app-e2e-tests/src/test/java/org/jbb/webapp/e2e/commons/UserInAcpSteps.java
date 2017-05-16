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

import org.jbb.webapp.e2e.Utils;

import static org.assertj.core.api.Assertions.assertThat;

public class UserInAcpSteps extends ScenarioSteps {
    private AcpPage acpPage;

    @Step
    public void open_acp() {
        acpPage.open();
    }

    @Step
    public void should_see_sign_in_page() {
        assertThat(Utils.current_url()).contains("signin");
    }

    @Step
    public void should_see_403_error() {
        acpPage.should_contain_info_about_403_forbidden_error();
    }

    @Step
    public void choose_system_tab() {
        acpPage.click_on_system_tab();
    }

    @Step
    public void choose_monitoring_option() {
        acpPage.click_on_monitoring_option();
    }

    @Step
    public void should_see_monitoring_page() {
        acpPage.should_contain_monitoring_system_header();
    }

    @Step
    public void choose_general_tab() {
        acpPage.click_on_general_tab();
    }

    @Step
    public void choose_board_settings_option() {
        acpPage.click_on_board_settings_option();
    }

    public void choose_registration_settings_option() {
        acpPage.click_on_member_registration_settings_option();
    }

    public void choose_database_settings_option() {
        acpPage.click_on_database_settings_option();
    }
}
