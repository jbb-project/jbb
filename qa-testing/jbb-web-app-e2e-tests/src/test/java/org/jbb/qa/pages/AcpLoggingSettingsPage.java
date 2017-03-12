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
import org.openqa.selenium.support.ui.Select;

@DefaultUrl(AcpLoggingSettingsPage.URL)
public class AcpLoggingSettingsPage extends PageObject {
    public static final String URL = "/acp/general/logging";

    @FindBy(id = "stackTraceVisibilityLevel")
    WebElement stackTraceVisibilityLevelSelect;

    @FindBys({@FindBy(xpath = "//button[contains(text(),'Save')]")})
    WebElement saveButton;

    public void clickSaveButton() {
        saveButton.click();
    }

    public void chooseStackTraceVisibilityLevel(String stacktraceLevelValue) {
        new Select(stackTraceVisibilityLevelSelect).selectByValue(stacktraceLevelValue);
    }

}
