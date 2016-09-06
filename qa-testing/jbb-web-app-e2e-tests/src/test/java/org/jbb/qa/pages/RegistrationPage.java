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

@DefaultUrl(RegistrationPage.URL)
public class RegistrationPage extends PageObject {
    public static final String URL = "/register";

    @FindBy(id = "username")
    WebElement usernameTextField;

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

    public void typeUsername(String username) {
        usernameTextField.sendKeys(username);
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
        shouldContainText("Registration completed!");
    }

    public void containsInfoAboutIncorrectUsernameLength() {
        shouldContainText("size must be between 3 and 20");
    }

    public void containsInfoAboutWhiteCharactersInUsername() {
        shouldContainText("Username cannot contain spaces and other white characters");
    }

    public void containsInfoAboutIncorrectDisplayedNameLength() {
        shouldContainText("size must be between 3 and 64");
    }

    public void containsInfoAboutIncorrectEmail() {
        shouldContainText("not a well-formed email address");
    }

    public void containsInfoAboutBusyUsername() {
        shouldContainText("This username is already taken");
    }

    public void containsInfoAboutBusyDisplayedName() {
        shouldContainText("This displayed name is already taken");
    }

    public void containsInfoAboutBusyEmail() {
        shouldContainText("This e-mail is already used by another member");
    }

    public void containsInfoAboutIncorrectLengthOfPassword() {
        shouldContainText("Password has incorrect length");
    }

    public void containsInfoAboutNotMatchingPasswords() {
        shouldContainText("Passwords don't match");
    }
}
