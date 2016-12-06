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

@DefaultUrl(AcpRegistrationSettingsPage.URL)
public class AcpRegistrationSettingsPage extends PageObject {
    public static final String URL = "/acp/general/registration";

    @FindBy(id = "minPassLength")
    WebElement minPassLengthField;

    @FindBy(id = "maxPassLength")
    WebElement maxPassLengthField;

    @FindBys({@FindBy(xpath = "//button[contains(text(),'Save')]")})
    WebElement saveButton;

    public void typeMinimumPasswordLength(String minimumPasswordLength) {
        minPassLengthField.clear();
        minPassLengthField.sendKeys(minimumPasswordLength);
    }

    public void typeMaximumPasswordLength(String maximumPasswordLength) {
        maxPassLengthField.clear();
        maxPassLengthField.sendKeys(maximumPasswordLength);
    }

    public void clickSendButton() {
        saveButton.click();
    }

    public void shouldContainInfoAboutTooSmallPasswordLength() {
        shouldContainText("must be greater than or equal to 1");
    }

    public void shouldContainInfoAboutIncorrectMinimumPasswordLengthValue() {
        shouldContainText("Minimum password length must be a positive number");
    }

    public void shouldContainInfoAboutIncorrectMaximumPasswordLengthValue() {
        shouldContainText("Maximum password length must be a positive number");
    }

    public void shouldContainInfoAboutMinLengthOfPasswordShouldBeLowerOrEqualToMax() {
        shouldContainText("Minimum length of password is greater than maximum length");
    }
}
