/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.lockout;

import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.DefaultUrl;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;

@DefaultUrl(AcpMemberLockoutSettingsPage.URL)
public class AcpMemberLockoutSettingsPage extends PageObject {
    public static final String URL = "/acp/general/lockout";

    @FindBy(id = "lockoutEnabled")
    WebElement lockoutEnabledRadioButton;

    @FindBy(id = "lockoutDisabled")
    WebElement lockoutDisabledRadioButton;

    @FindBy(id = "failedAttemptsThreshold")
    WebElement failedAttemptsThresholdField;

    @FindBy(id = "failedAttemptsExpirationMinutes")
    WebElement failedAttemptsExpirationField;

    @FindBy(id = "lockoutDurationMinutes")
    WebElement lockoutDurationField;

    @FindBys({@FindBy(xpath = "//button[contains(text(),'Save')]")})
    WebElement saveButton;

    public void clickSaveButton() {
        saveButton.click();
    }

    public void clickEnableLockoutRadioButton() {
        lockoutEnabledRadioButton.click();
    }

    public void clickDisableLockoutRadioButton() {
        lockoutDisabledRadioButton.click();
    }

    public void typeFailedAttemptsThreshold(String failedAttemptsThreshold) {
        failedAttemptsThresholdField.clear();
        failedAttemptsThresholdField.sendKeys(failedAttemptsThreshold);
    }

    public void typeFailedAttemptsExpiration(String failedAttemptsExpiration) {
        failedAttemptsExpirationField.clear();
        failedAttemptsExpirationField.sendKeys(failedAttemptsExpiration);
    }

    public void typeLockoutDuration(String lockoutDuration) {
        lockoutDurationField.clear();
        lockoutDurationField.sendKeys(lockoutDuration);
    }

    public void shouldBeVisibleInfoAboutPositiveValue() {
        shouldContainText("must be greater than or equal to 1");
    }

    public void shouldBeVisibleInfoAboutInvalidValue() {
        shouldContainText("Invalid value");
    }
}
