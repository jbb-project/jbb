/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.web.registrationsettings;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;


public class RegistrationSettingsSteps extends ScenarioSteps {

    AcpRegistrationSettingsPage registrationSettingsPage;

    @Step
    public void open_registration_settings_page() {
        registrationSettingsPage.open();
    }

    @Step
    public void type_minimum_password_length(String minimumPasswordLength) {
        registrationSettingsPage.typeMinimumPasswordLength(minimumPasswordLength);
    }

    @Step
    public void type_maximum_password_length(String maximumPasswordLength) {
        registrationSettingsPage.typeMaximumPasswordLength(maximumPasswordLength);
    }

    @Step
    public void send_registration_settings_form() {
        registrationSettingsPage.clickSendButton();
    }

    @Step
    public void should_be_informed_about_too_small_password_length() {
        registrationSettingsPage.shouldContainInfoAboutTooSmallPasswordLength();
    }

    @Step
    public void should_be_informed_about_incorrect_minimum_password_length_value() {
        registrationSettingsPage.shouldContainInfoAboutIncorrectMinimumPasswordLengthValue();
    }

    @Step
    public void should_be_informed_about_incorrect_maximum_password_length_value() {
        registrationSettingsPage.shouldContainInfoAboutIncorrectMaximumPasswordLengthValue();
    }

    @Step
    public void should_be_informed_that_minimum_cannot_be_greater_than_maximum_pass_length() {
        registrationSettingsPage.shouldContainInfoAboutMinLengthOfPasswordShouldBeLowerOrEqualToMax();
    }

    @Step
    public void should_be_informed_about_saving_settings() {
        registrationSettingsPage.containsInfoAboutSavingSettingsCorrectly();
    }

    @Step
    public void set_new_password_lengths_with_success(String minPasswordLength, String maxPasswordLength) {
        open_registration_settings_page();
        type_minimum_password_length(minPasswordLength);
        type_maximum_password_length(maxPasswordLength);
        send_registration_settings_form();
        should_be_informed_about_saving_settings();
    }

    @Step
    public void set_allow_for_email_duplication() {
        open_registration_settings_page();
        allow_for_email_duplication();
        send_registration_settings_form();
        should_be_informed_about_saving_settings();
    }

    @Step
    public void allow_for_email_duplication() {
        registrationSettingsPage.setEmailDuplicationAllowedCheckboxValue(true);
    }

    @Step
    public void set_disallow_for_email_duplication() {
        open_registration_settings_page();
        disallow_for_email_duplication();
        send_registration_settings_form();
        should_be_informed_about_saving_settings();
    }

    @Step
    public void disallow_for_email_duplication() {
        registrationSettingsPage.setEmailDuplicationAllowedCheckboxValue(false);
    }
}
