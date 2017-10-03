/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.logging;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import net.serenitybdd.core.annotations.findby.By;
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

    @FindBy(linkText = "Add new console appender")
    WebElement addConsoleAppenderLink;

    @FindBy(linkText = "Add new file appender")
    WebElement addFileAppenderLink;

    @FindBy(linkText = "Add new logger")
    WebElement addLoggerLink;

    @FindBy(id = "appenderName")
    WebElement appenderNameField;

    @FindBy(id = "currentLogFileName")
    WebElement currentLogFileNameField;

    @FindBy(id = "rotationFileNamePattern")
    WebElement rotationFileNamePatternField;

    @FindBy(id = "maxFileSize")
    WebElement maxFileSizeField;

    @FindBy(id = "maxHistory")
    WebElement maxHistoryField;

    @FindBy(id = "pattern")
    WebElement patternField;

    @FindBy(id = "loggerName")
    WebElement loggerNameField;

    public void clickSaveButton() {
        saveButton.click();
    }

    public void chooseStackTraceVisibilityLevel(String stacktraceLevelValue) {
        new Select(stackTraceVisibilityLevelSelect).selectByValue(stacktraceLevelValue);
    }

    public void clickAddConsoleAppenderLink() {
        addConsoleAppenderLink.click();
    }

    public void typeConsoleAppenderName(String appenderName) {
        appenderNameField.clear();
        appenderNameField.sendKeys(appenderName);
    }

    public void typeLogPattern(String pattern) {
        patternField.clear();
        patternField.sendKeys(pattern);
    }

    public void shouldContainWarnAboutBlankField() {
        shouldContainText("must not be blank");
    }

    public void shouldContainWarnAboutBusyAppenderName() {
        shouldContainText("log appender name must be unique");
    }

    public void shouldContainInfoAboutSuccess() {
        shouldContainText("Settings saved correctly");
    }

    public void shouldContainAppenderName(String appenderName) {
        shouldContainText(appenderName);
    }

    public void clickEditLinkForAppender(String appenderName) {
        List<WebElement> elements = getDriver().findElements(By.cssSelector(String.format("a[href*='append?id=%s&act=edit']", appenderName)));
        elements.get(0).click();
    }

    public void clickDeleteForAppender(String appenderName) {
        List<WebElement> elements = getDriver().findElements(By.cssSelector(String.format("a[href*='append?id=%s&act=del']", appenderName)));
        elements.get(0).click();
    }

    public void shouldNotContainAppenderName(String appenderName) {
        assertThat(containsText(appenderName)).isFalse();
    }

    public void clickAddFileAppenderLink() {
        addFileAppenderLink.click();
    }

    public void typeFileAppenderName(String appenderName) {
        appenderNameField.clear();
        appenderNameField.sendKeys(appenderName);
    }

    public void typeCurrentLogFileName(String currentLogFileName) {
        currentLogFileNameField.clear();
        currentLogFileNameField.sendKeys(currentLogFileName);
    }

    public void typeRotationFileNamePattern(String rotationPattern) {
        rotationFileNamePatternField.clear();
        rotationFileNamePatternField.sendKeys(rotationPattern);
    }

    public void typeMaxSizeFile(String maxSizeFile) {
        maxFileSizeField.clear();
        maxFileSizeField.sendKeys(maxSizeFile);
    }

    public void typeMaxHistory(String maxHistory) {
        maxHistoryField.clear();
        maxHistoryField.sendKeys(maxHistory);
    }

    public void shouldContainWarnAboutIncorrectMaxFileSize() {
        shouldContainText("file size should be in format: <number><space><KB|MB|GB>");
    }

    public void shouldContainsWarnAboutNegativeValue() {
        shouldContainText("must be greater than or equal to 0");
    }

    public void shouldContainWarnAboutInvalidValue() {
        shouldContainText("Invalid value");
    }

    public void clickAddLoggerLink() {
        addLoggerLink.click();
    }

    public void typeLoggerName(String loggerName) {
        loggerNameField.clear();
        loggerNameField.sendKeys(loggerName);
    }

    public void shouldContainWarnAboutIncorrectLoggerName() {
        shouldContainText("name of logger must be a java package or fully qualified class name");
    }

    public void shouldContainWarnAboutBusyLoggerName() {
        shouldContainText("logger name must be unique");
    }

    public void shouldContainLoggerName(String loggerName) {
        shouldContainText(loggerName);
    }

    public void clickDeleteForLogger(String loggerName) {
        List<WebElement> elements = getDriver().findElements(By.cssSelector(String.format("a[href*='logger?id=%s&act=del']", loggerName)));
        elements.get(0).click();
    }

    public void shouldNotContainLoggerName(String loggerName) {
        assertThat(containsText(loggerName)).isFalse();
    }
}
