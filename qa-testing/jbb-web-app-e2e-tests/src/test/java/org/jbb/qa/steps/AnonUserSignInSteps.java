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

import org.jbb.qa.pages.SignInPage;

public class AnonUserSignInSteps {
    SignInPage signInPage;

    @Step
    public void opens_sign_in_page() {
        signInPage.open();
    }

    public void type_login(String login) {
        signInPage.typeLogin(login);
    }

    public void type_password(String password) {
        signInPage.typePassword(password);
    }

    public void send_form() {
        signInPage.sendForm();
    }

    public void should_be_informed_about_invalid_credencials() {
        signInPage.containsInfoAboutInvalidCredencials();
    }
}
