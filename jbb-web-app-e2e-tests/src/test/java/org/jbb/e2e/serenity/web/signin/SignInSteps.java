/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.web.signin;

import static org.assertj.core.api.Assertions.assertThat;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;
import org.assertj.core.api.Assertions;
import org.jbb.e2e.serenity.Utils;
import org.jbb.e2e.serenity.web.commons.HomePage;

public class SignInSteps extends ScenarioSteps {
    public static final String ADMINISTRATOR_USERNAME = "administrator";
    public static final String ADMINISTRATOR_PASSWORD = "administrator";
    public static final String ADMINISTRATOR_DISPLAYED_NAME = "Administrator";

    SignInPage signInPage;
    HomePage homePage;

    @Step
    public void opens_sign_in_page() {
        signInPage.open();
    }

    @Step
    public void type_username(String username) {
        signInPage.typeUsername(username);
    }

    @Step
    public void type_password(String password) {
        signInPage.typePassword(password);
    }

    @Step
    public void send_form() {
        signInPage.sendForm();
    }

    @Step
    public void should_be_informed_about_invalid_credencials() {
        signInPage.containsInfoAboutInvalidCredencials();
    }

    @Step
    private void should_be_informed_about_lockout() {
        signInPage.containsInfoAboutLockout();
    }

    @Step
    public void should_move_to_home_page() {
        Assertions.assertThat(Utils.current_url()).endsWith(HomePage.URL);
    }

    @Step
    public void should_see_own_displayed_name_in_navbar(String displayedName) {
        assertThat(homePage.displayedName()).isEqualTo(displayedName);
    }

    @Step
    public void sign_in_as_administrator_with_success() {
        sign_in_with_credentials_with_success(ADMINISTRATOR_USERNAME, ADMINISTRATOR_PASSWORD, ADMINISTRATOR_DISPLAYED_NAME);
    }

    @Step
    public void sign_in_with_credentials_with_success(String username, String password, String displayedName) {
        opens_sign_in_page();
        type_username(username);
        type_password(password);
        send_form();
        should_see_own_displayed_name_in_navbar(displayedName);
    }

    @Step
    public void sign_in_with_credentials_with_failure(String username, String password) {
        opens_sign_in_page();
        type_username(username);
        type_password(password);
        send_form();
        should_be_informed_about_invalid_credencials();
    }

    @Step
    public void sign_in_with_credentials_with_failure_due_to_lockout(String username, String password) {
        opens_sign_in_page();
        type_username(username);
        type_password(password);
        send_form();
        should_be_informed_about_lockout();
    }

    @Step
    public void sign_out() {
        signInPage.clickSignOut();
    }

    @Step
    public void member_should_not_be_sign_in() {
        signInPage.shouldNotBeAuthenticated();
    }
}
