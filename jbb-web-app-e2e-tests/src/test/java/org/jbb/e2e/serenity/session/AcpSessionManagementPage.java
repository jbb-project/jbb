/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.session;

import net.serenitybdd.core.annotations.findby.By;
import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.DefaultUrl;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;

import static org.assertj.core.api.Assertions.fail;

@DefaultUrl(AcpSessionManagementPage.URL)
public class AcpSessionManagementPage extends PageObject {
    public static final String URL = "/acp/system/sessions";

    @FindBy(id = "intervalField")
    WebElement intervalField;

    @FindBys({@FindBy(xpath = "//button[contains(text(),'Save')]")})
    WebElement saveButton;

    public void typeMaximumInactiveInterval(String maximumInactive) {
        intervalField.clear();
        intervalField.sendKeys(maximumInactive);
    }

    public void clickSaveButton() {
        saveButton.click();
    }

    public void containsSessionForUsername(String username) {
        getDriver().findElement(By.xpath(String.format("//table/tbody/tr/td[2][contains(text(),'%s')]", username)));
    }

    public void doesNotContainSessionForUsername(String username) {
        try {
            containsSessionForUsername(username);
        } catch (NoSuchElementException e) {
            // ok
            return;
        }
        fail("Should not find session for username " + username);
    }
}
