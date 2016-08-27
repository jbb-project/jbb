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

import org.jbb.qa.pages.RegistrationPage;

public class AnonUserRegistrationSteps {
    RegistrationPage registrationPage;

    @Step
    public void opens_registration_page() {
        registrationPage.open();
    }

    @Step
    public void type_login(String login) {
        registrationPage.typeLogin(login);
    }

    @Step
    public void type_displayed_name(String displayedName) {
        registrationPage.typeDisplayedName(displayedName);
    }

    @Step
    public void type_email(String email) {
        registrationPage.typeEmail(email);
    }

    @Step
    public void type_password(String password) {
        registrationPage.typePassword(password);
    }

    @Step
    public void type_password_again(String password) {
        registrationPage.typePasswordAgain(password);
    }

    @Step
    public void send_registration_form() {
        registrationPage.sendRegistrationForm();
    }

    @Step
    public void should_be_informed_about_registration_success() {
        registrationPage.containsConfirmation();
    }

    @Step
    public void should_be_informed_about_incorrect_login_length() {
        registrationPage.containsInfoAboutIncorrectLoginLength();
    }

    @Step
    public void should_be_informed_about_forbidden_white_characters_in_login() {
        registrationPage.containsInfoAboutWhiteCharactersInLogin();
    }

    @Step
    public void should_be_informed_about_incorrect_display_name_length() {
        registrationPage.containsInfoAboutIncorrectDisplayedNameLength();
    }

    @Step
    public void should_be_informed_about_incorrect_email() {
        registrationPage.containsInfoAboutIncorrectEmail();
    }

    @Step
    public void should_be_informed_about_busy_login() {
        registrationPage.containsInfoAboutBusyLogin();
    }

    @Step
    public void should_be_informed_about_busy_displayed_name() {
        registrationPage.containsInfoAboutBusyDisplayedName();
    }

    @Step
    public void should_be_informed_about_busy_email() {
        registrationPage.containsInfoAboutBusyEmail();
    }

    @Step
    public void should_be_informed_about_incorrect_length_of_password() {
        registrationPage.containsInfoAboutIncorrectLengthOfPassword();
    }

    @Step
    public void should_be_informed_about_not_match_passwords() {
        registrationPage.containsInfoAboutNotMatchingPasswords();
    }

    @Step
    public void register_new_member(String login, String displayedName, String email,
                                    String password, String passwordAgain) {
        opens_registration_page();
        type_login(login);
        type_displayed_name(displayedName);
        type_email(email);
        type_password(password);
        type_password_again(passwordAgain);
        send_registration_form();
        should_be_informed_about_registration_success();
    }
}
