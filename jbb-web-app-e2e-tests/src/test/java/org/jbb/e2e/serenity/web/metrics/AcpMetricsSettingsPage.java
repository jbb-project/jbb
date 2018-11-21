/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.web.metrics;

import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.DefaultUrl;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;

@DefaultUrl(AcpMetricsSettingsPage.URL)
public class AcpMetricsSettingsPage extends PageObject {

    public static final String URL = "/acp/system/metrics";

    @FindBy(id = "consoleReporterPeriodSeconds")
    WebElement consoleReporterPeriodSecondsField;

    @FindBy(id = "csvReporterPeriodSeconds")
    WebElement csvReporterPeriodSecondsField;

    @FindBy(id = "logReporterPeriodSeconds")
    WebElement logReporterPeriodSecondsField;

    @FindBys({@FindBy(xpath = "//button[contains(text(),'Save')]")})
    WebElement saveButton;

    public void clickSaveButton() {
        saveButton.click();
    }

    public void typeConsoleReporterPeriodSeconds(String seconds) {
        consoleReporterPeriodSecondsField.clear();
        consoleReporterPeriodSecondsField.sendKeys(seconds);
    }

    public void typeCsvReporterPeriodSeconds(String seconds) {
        csvReporterPeriodSecondsField.clear();
        csvReporterPeriodSecondsField.sendKeys(seconds);
    }

    public void typeLogReporterPeriodSeconds(String seconds) {
        logReporterPeriodSecondsField.clear();
        logReporterPeriodSecondsField.sendKeys(seconds);
    }

    public void shouldBeVisibleInfoAboutPositiveValue() {
        shouldContainText("must be greater than or equal to 1");
    }

    public void shouldBeVisibleInfoAboutInvalidValue() {
        shouldContainText("Invalid value");
    }

}
