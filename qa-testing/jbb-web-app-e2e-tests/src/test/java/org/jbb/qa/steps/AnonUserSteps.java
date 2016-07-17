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

import org.jbb.qa.pages.HomePage;
import org.jbb.qa.pages.RegistrationPage;

import static org.assertj.core.api.Assertions.assertThat;

public class AnonUserSteps {
    HomePage homePage;
    RegistrationPage registrationPage;

    @Step
    public void opens_home_page() {
        homePage.open();
    }

    @Step
    public void opens_registration_page() {
        registrationPage.open();
    }

    @Step
    public void should_see_jbb_footer() {
        assertThat(homePage.footer_content()).contains("jBB v.");
    }

    @Step
    public void should_see_registration_link() {
        homePage.has_registration_link();
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
}
