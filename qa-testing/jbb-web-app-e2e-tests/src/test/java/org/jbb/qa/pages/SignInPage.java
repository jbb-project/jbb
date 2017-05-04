/*
 * Copyright (C) 2017 the original author or authors.
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

    @FindBy(id = "username")
    WebElement usernameTextField;

    @FindBy(id = "pswd")
    WebElement passwordField;

    @FindBys({@FindBy(xpath = "//button[contains(text(),'Sign in')]")})
    WebElement signInButton;

    @FindBy(id = "signoutButton")
    WebElement signOutButton;

    public void typeUsername(String username) {
        usernameTextField.click();
        usernameTextField.clear();
        usernameTextField.sendKeys(username);
    }

    public void typePassword(String password) {
        passwordField.click();
        passwordField.clear();
        passwordField.sendKeys(password);
    }

    public void sendForm() {
        signInButton.click();
    }

    public void clickSignOut() {
        signOutButton.click();
    }

    public void containsInfoAboutInvalidCredencials() {
        shouldContainText("Invalid username or password");
    }

    public void containsInfoAboutLockout() {
        shouldContainText("Your account has been temporary locked due to many invalid sign in attempts.");
    }
}
