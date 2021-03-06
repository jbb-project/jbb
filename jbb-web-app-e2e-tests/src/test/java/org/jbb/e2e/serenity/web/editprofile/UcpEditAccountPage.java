/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.web.editprofile;

import static org.assertj.core.api.Assertions.assertThat;

import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.DefaultUrl;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;

@DefaultUrl(UcpEditAccountPage.URL)
public class UcpEditAccountPage extends PageObject {
    public static final String URL = "/ucp/profile/editAccount";

    @FindBy(id = "email")
    WebElement emailField;

    @FindBy(id = "newPassword")
    WebElement newPasswordField;

    @FindBy(id = "newPasswordAgain")
    WebElement newPasswordAgainField;

    @FindBy(id = "currentPassword")
    WebElement currentPasswordField;

    @FindBys({@FindBy(xpath = "//button[contains(text(),'Save')]")})
    WebElement saveButton;

    public void typeEmail(String email) {
        emailField.clear();
        emailField.sendKeys(email);
    }

    public void typeNewPassword(String password) {
        newPasswordField.clear();
        newPasswordField.sendKeys(password);
    }

    public void typeNewPasswordAgain(String password) {
        newPasswordAgainField.clear();
        newPasswordAgainField.sendKeys(password);
    }

    public void typeCurrentPassword(String password) {
        currentPasswordField.clear();
        currentPasswordField.sendKeys(password);
    }

    public void sendForm() {
        saveButton.click();
    }

    public void shouldContainInfoAboutIncorrectEmail() {
        shouldContainText("must be a well-formed email address");
    }

    public void shouldContainInfoAboutIncorrectPasswordLength() {
        shouldContainText("Password has incorrect length");
    }

    public void shouldContainInfoAboutNotMatchingNewPasswords() {
        shouldContainText("Passwords don't match");
    }

    public void shouldContainInfoAboutNotMatchingCurrentPassword() {
        shouldContainText("Given password is not match to current password");
    }

    public void containsInfoAboutBusyEmail() {
        shouldContainText("This e-mail is already used by another member");
    }

    public void containsInfoAboutSavingSettingsCorrectly() {
        shouldContainText("Your settings saved correctly");
    }

    public void emailFieldContain(String email) {
        assertThat(emailField.getAttribute("value")).isEqualTo(email);
    }

    public void emailFieldShouldBeReadOnly() {
        assertThat(emailField.getAttribute("readonly")).isEqualTo("true");
    }
}
