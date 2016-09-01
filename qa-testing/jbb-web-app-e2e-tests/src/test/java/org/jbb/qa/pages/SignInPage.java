/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.qa.pages;

import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.DefaultUrl;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;

@DefaultUrl(SignInPage.URL)
public class SignInPage extends PageObject {
    public static final String URL = "/signin";

    @FindBy(id = "login")
    WebElement loginTextField;

    @FindBy(id = "pswd")
    WebElement passwordField;

    @FindBys({@FindBy(xpath = "//button[contains(text(),'Sign in')]")})
    WebElement signInButton;

    public void typeLogin(String login) {
        loginTextField.sendKeys(login);
    }

    public void typePassword(String password) {
        passwordField.sendKeys(password);
    }

    public void sendForm() {
        signInButton.click();
    }

    public void containsInfoAboutInvalidCredencials() {
        shouldContainText("Invalid username or password");
    }
}
