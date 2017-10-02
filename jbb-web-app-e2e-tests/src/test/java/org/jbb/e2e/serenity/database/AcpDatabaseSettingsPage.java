/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.database;

import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.DefaultUrl;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;

@DefaultUrl(AcpDatabaseSettingsPage.URL)
public class AcpDatabaseSettingsPage extends PageObject {
    public static final String URL = "/acp/system/database";

    @FindBy(id = "h2ManagedServerDatabaseFileName")
    WebElement databaseFileNameField;

    @FindBy(id = "minimumIdleConnections")
    WebElement minimumIdleConnectionsField;

    @FindBy(id = "maximumPoolSize")
    WebElement maximumPoolSizeField;

    @FindBy(id = "connectionTimeoutMilliseconds")
    WebElement connectionTimeOutMillisecondsField;

    @FindBy(id = "connectionMaxLifetimeMilliseconds")
    WebElement connectionMaxLifetimeMillisecondsField;

    @FindBy(id = "idleTimeoutMilliseconds")
    WebElement connectionIdleTimeoutMillisecondsField;

    @FindBy(id = "validationTimeoutMilliseconds")
    WebElement connectionValidationTimeoutMillisecondsField;

    @FindBy(id = "leakDetectionThreshold")
    WebElement connectionLeakDetectionThresholdMillisecondsField;

    @FindBys({@FindBy(xpath = "//button[contains(text(),'Save')]")})
    WebElement saveButton;

    public void clickSaveButton() {
        saveButton.click();
    }


    public void typeDatabaseFilename(String databaseFilename) {
        databaseFileNameField.clear();
        databaseFileNameField.sendKeys(databaseFilename);
    }

    public void shouldContainInfoAboutEmptyDatabaseFilename() {
        shouldContainText("may not be empty");
    }

    public void typeMinimumIdleDbConnections(String minimumIdleDbConnections) {
        minimumIdleConnectionsField.clear();
        minimumIdleConnectionsField.sendKeys(minimumIdleDbConnections);
    }

    public void shouldContainInfoAboutInvalidMinimumIdleDbConnections() {
        shouldContainText("Invalid value");
    }

    public void shouldContainInfoAboutNotPositiveMinimumIdleDbConnections() {
        shouldContainText("must be greater than or equal to 1");
    }

    public void typeMaximumSizeConnectionPool(String maximumSizeConnectionPool) {
        maximumPoolSizeField.clear();
        maximumPoolSizeField.sendKeys(maximumSizeConnectionPool);
    }

    public void shouldContainInfoAboutInvalidMaximumSizeConnectionPool() {
        shouldContainText("Invalid value");
    }

    public void shouldContainInfoAboutNotPositiveMaximumSizeConnectionPool() {
        shouldContainText("must be greater than or equal to 1");
    }

    public void typeConnectionTimeoutMiliseconds(String connectionTimeoutMiliseconds) {
        connectionTimeOutMillisecondsField.clear();
        connectionTimeOutMillisecondsField.sendKeys(connectionTimeoutMiliseconds);
    }

    public void shouldContainInfoAboutInvalidConnectionTimeoutMiliseconds() {
        shouldContainText("Invalid value");
    }

    public void shouldContainInfoAboutNegativeConnectionTimeoutMilisecondsValue() {
        shouldContainText("must be greater than or equal to 0");
    }

    public void containsInfoAboutSavingSettingsCorrectly() {
        shouldContainText("Settings saved correctly");
    }

    public void typeConnectionMaximumLifetimeMilliseconds(String connectionMaximumLifetime) {
        connectionMaxLifetimeMillisecondsField.clear();
        connectionMaxLifetimeMillisecondsField.sendKeys(connectionMaximumLifetime);
    }

    public void containsInfoAboutInvalidValue() {
        shouldContainText("Invalid value");
    }

    public void containsInfoAboutNegativeValue() {
        shouldContainText("must be greater than or equal to 0");
    }

    public void typeConnectionIdleTimeoutMilliseconds(String idleTimeout) {
        connectionIdleTimeoutMillisecondsField.clear();
        connectionIdleTimeoutMillisecondsField.sendKeys(idleTimeout);
    }

    public void typeConnectionValidationTimeoutMilliseconds(String validationTimeout) {
        connectionValidationTimeoutMillisecondsField.clear();
        connectionValidationTimeoutMillisecondsField.sendKeys(validationTimeout);
    }

    public void typeConnectionLeakDetectionThresholdMilliseconds(String leakDetectionThreshold) {
        connectionLeakDetectionThresholdMillisecondsField.clear();
        connectionLeakDetectionThresholdMillisecondsField.sendKeys(leakDetectionThreshold);
    }
}
