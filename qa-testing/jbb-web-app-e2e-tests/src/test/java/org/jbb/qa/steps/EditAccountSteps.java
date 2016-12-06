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
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.steps.ScenarioSteps;

import org.jbb.qa.pages.UcpEditAccountPage;

public class EditAccountSteps extends ScenarioSteps {
    @Steps
    UserInUcpSteps ucpUser;

    UcpEditAccountPage ucpEditAccountPage;

    @Step
    public void type_email(String email) {
        ucpEditAccountPage.typeEmail(email);
    }

    @Step
    public void type_new_password(String password) {
        ucpEditAccountPage.typeNewPassword(password);
    }

    @Step
    public void type_new_password_again(String password) {
        ucpEditAccountPage.typeNewPasswordAgain(password);
    }

    @Step
    public void type_current_password(String password) {
        ucpEditAccountPage.typeCurrentPassword(password);
    }

    @Step
    public void send_edit_account_form() {
        ucpEditAccountPage.sendForm();
    }

    @Step
    public void should_be_informed_about_incorrect_email() {
        ucpEditAccountPage.shouldContainInfoAboutIncorrectEmail();
    }

    @Step
    public void should_be_informed_about_incorrect_password_length() {
        ucpEditAccountPage.shouldContainInfoAboutIncorrectPasswordLength();
    }

    @Step
    public void should_be_informed_about_not_matching_passwords() {
        ucpEditAccountPage.shouldContainInfoAboutNotMatchingNewPasswords();
    }

    @Step
    public void should_be_informed_about_busy_email() {
        ucpEditAccountPage.containsInfoAboutBusyEmail();
    }

    @Step
    public void should_be_informed_about_not_matching_current_password() {
        ucpEditAccountPage.shouldContainInfoAboutNotMatchingCurrentPassword();
    }

    @Step
    public void should_be_informed_about_saving_settings() {
        ucpEditAccountPage.containsInfoAboutSavingSettingsCorrectly();
    }

    @Step
    public void open_edit_account_page() {
        ucpEditAccountPage.open();
    }

    @Step
    public void email_should_be_visible_in_edit_account_form(String email) {
        ucpUser.opens_ucp();
        ucpUser.choose_profile_tab();
        ucpUser.choose_edit_account_settings_option();

        ucpEditAccountPage.emailFieldContain(email);
    }

    @Step
    public void change_password_with_success(String currentPassword, String newPassword) {
        open_edit_account_page();
        type_current_password(currentPassword);
        type_new_password(newPassword);
        type_new_password_again(newPassword);
        send_edit_account_form();
        should_be_informed_about_saving_settings();
    }

    @Step
    public void change_password_with_fail_due_to_invalid_length(String currentPassword, String newPassword) {
        open_edit_account_page();
        type_current_password(currentPassword);
        type_new_password(newPassword);
        type_new_password_again(newPassword);
        send_edit_account_form();
        should_be_informed_about_incorrect_password_length();
    }

    @Step
    public void change_email_with_success(String currentPassword, String newEmail) {
        open_edit_account_page();
        type_current_password(currentPassword);
        type_email(newEmail);
        send_edit_account_form();
        should_be_informed_about_saving_settings();
    }

    @Step
    public void change_email_with_fail_due_to_used_by_another_member(String currentPassword, String newEmail) {
        open_edit_account_page();
        type_current_password(currentPassword);
        type_email(newEmail);
        send_edit_account_form();
        should_be_informed_about_busy_email();
    }
}
