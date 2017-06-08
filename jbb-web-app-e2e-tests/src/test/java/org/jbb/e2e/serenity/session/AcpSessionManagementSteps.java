/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.session;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;


public class AcpSessionManagementSteps extends ScenarioSteps {

    AcpSessionManagementPage acpSessionManagementPage;

    @Step
    public void open_session_management_page(){
        acpSessionManagementPage.open();
    }

    @Step
    public void save_session_settings_form() {
        acpSessionManagementPage.clickSaveButton();
    }

    @Step
    public void user_should_save_alert_with_message(String message) {
        acpSessionManagementPage.shouldContainText(message);
    }

    @Step
    public void type_maximum_inactive_interval(String maximumInactive) {
        acpSessionManagementPage.typeMaximumInactiveInterval(maximumInactive);
    }

    @Step
    public void should_be_informed_about_saving_settings() {
        acpSessionManagementPage.containsText("Settings saved correctly");
    }

    @Step
    public void should_be_informed_about_null_value() {
        acpSessionManagementPage.containsText("may not be null");
    }

    @Step
    public void should_be_informed_about_invalid_value() {
        acpSessionManagementPage.containsText("Invalid value");
    }

    @Step
    public void should_be_informed_about_non_positive_value() {
        acpSessionManagementPage.containsText("must be greater than or equal to 1");
    }

    @Step
    public void session_for_member_should_be_visible(String username) {
        acpSessionManagementPage.containsSessionForUsername(username);
    }

    @Step
    public void session_for_member_should_not_be_visible(String username) {
        acpSessionManagementPage.doesNotContainSessionForUsername(username);
    }

    @Step
    public void delete_latest_session_for_member(String username) {
        acpSessionManagementPage.deleteLatestSessionForUsername(username);
    }

    @Step
    public void set_session_maximum_inactive_interval(String inactiveInterval) {
        open_session_management_page();
        type_maximum_inactive_interval(inactiveInterval);
        should_be_informed_about_saving_settings();
    }
}
