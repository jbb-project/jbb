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

import org.jbb.qa.pages.AcpRegistrationSettingsPage;


public class RegistrationSettingsSteps extends ScenarioSteps {
    private AcpRegistrationSettingsPage registrationSettingsPage;

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
}
