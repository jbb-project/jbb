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

import static org.assertj.core.api.Assertions.assertThat;

@DefaultUrl(RegistrationPage.URL)
public class RegistrationPage extends PageObject {
    public static final String URL = "/register";

    @FindBy(id = "login")
    WebElement loginTextField;

    @FindBy(id = "displayedName")
    WebElement displayedNameField;

    @FindBy(id = "email")
    WebElement emailField;

    @FindBy(id = "password")
    WebElement passwordField;

    @FindBy(id = "passwordAgain")
    WebElement passwordAgainField;

    @FindBys({@FindBy(xpath = "//button[contains(text(),'Send')]")})
    WebElement registerButton;

    public void typeLogin(String login) {
        loginTextField.sendKeys(login);
    }

    public void typeDisplayedName(String displayedName) {
        displayedNameField.sendKeys(displayedName);
    }

    public void typeEmail(String email) {
        emailField.sendKeys(email);
    }

    public void typePassword(String password) {
        passwordField.sendKeys(password);
    }

    public void typePasswordAgain(String passwordAgain) {
        passwordAgainField.sendKeys(passwordAgain);
    }

    public void sendRegistrationForm() {
        registerButton.click();
    }

    public void containsConfirmation() {
        assertThat(containsText("Registration completed!")).isTrue();
    }

    public void containsInfoAboutIncorrectLoginLength() {
        assertThat(containsText("size must be between 3 and 20")).isTrue();
    }

    public void containsInfoAboutWhiteCharactersInLogin() {
        assertThat(containsText("Login cannot contain spaces and other white characters")).isTrue();
    }

    public void containsInfoAboutIncorrectDisplayedNameLength() {
        assertThat(containsText("size must be between 3 and 64")).isTrue();
    }

    public void containsInfoAboutIncorrectEmail() {
        assertThat(containsText("not a well-formed email address")).isTrue();
    }

    public void containsInfoAboutBusyLogin() {
        assertThat(containsText("This login is already taken")).isTrue();
    }

    public void containsInfoAboutBusyDisplayedName() {
        assertThat(containsText("This displayed name is already taken")).isTrue();
    }

    public void containsInfoAboutBusyEmail() {
        assertThat(containsText("This e-mail is already used by another member")).isTrue();
    }

    public void containsInfoAboutIncorrectLengthOfPassword() {
        assertThat(containsText("Password has incorrect length")).isTrue();
    }

    public void containsInfoAboutNotMatchingPasswords() {
        assertThat(containsText("Passwords don't match")).isTrue();
    }
}
