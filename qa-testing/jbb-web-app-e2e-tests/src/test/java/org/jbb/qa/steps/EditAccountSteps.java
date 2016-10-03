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

import net.thucydides.core.annotations.Steps;
import net.thucydides.core.steps.ScenarioSteps;

import org.jbb.qa.pages.UcpEditAccountPage;

public class EditAccountSteps extends ScenarioSteps {
    @Steps
    UserInUcpSteps ucpUser;

    UcpEditAccountPage ucpEditAccountPage;

    public void type_email(String email) {
        ucpEditAccountPage.typeEmail(email);
    }

    public void type_new_password(String password) {
        ucpEditAccountPage.typeNewPassword(password);
    }

    public void type_new_password_again(String password) {
        ucpEditAccountPage.typeNewPasswordAgain(password);
    }

    public void type_current_password(String password) {
        ucpEditAccountPage.typeCurrentPassword(password);
    }

    public void send_edit_account_form() {
        ucpEditAccountPage.sendForm();
    }

    public void should_be_informed_about_incorrect_email() {
        ucpEditAccountPage.shouldContainInfoAboutIncorrectEmail();
    }

    public void should_be_informed_about_incorrect_password_length() {
        ucpEditAccountPage.shouldContainInfoAboutIncorrectPasswordLength();
    }

    public void should_be_informed_about_not_matching_passwords() {
        ucpEditAccountPage.shouldContainInfoAboutNotMatchingNewPasswords();
    }

    public void should_be_informed_about_not_matching_current_password() {
        ucpEditAccountPage.shouldContainInfoAboutNotMatchingCurrentPassword();
    }

    public void should_be_informed_about_saving_settings() {
        ucpEditAccountPage.containsInfoAboutSavingSettingsCorrectly();
    }

    public void open_edit_account_page() {
        ucpEditAccountPage.open();
    }

    public void email_should_be_visible_in_edit_account_form(String email) {
        ucpUser.opens_ucp();
        ucpUser.choose_profile_tab();
        ucpUser.choose_edit_account_settings_option();

        ucpEditAccountPage.emailFieldContain(email);
    }
}
