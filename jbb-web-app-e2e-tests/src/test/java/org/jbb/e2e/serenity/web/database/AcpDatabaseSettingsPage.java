/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.web.database;

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

    @FindBy(id = "h2InMemoryName")
    WebElement h2InMemoryNameField;

    @FindBy(id = "h2EmbeddedDatabaseFileName")
    WebElement h2EmbeddedDatabaseFileNameField;

    @FindBy(id = "h2EmbeddedUsername")
    WebElement h2EmbeddedUsernameField;

    @FindBy(id = "h2ManagedServerDatabaseFileName")
    WebElement h2ManagedServerDatabaseFileNameField;

    @FindBy(id = "h2ManagedServerPort")
    WebElement h2ManagedServerPortField;

    @FindBy(id = "h2ManagedServerUsername")
    WebElement h2ManagedServerUsernameField;

    @FindBy(id = "h2RemoteServerUrl")
    WebElement h2RemoteServerUrlField;

    @FindBy(id = "h2RemoteServerUsername")
    WebElement h2RemoteServerUsernameField;

    @FindBy(id = "postgresqlHostName")
    WebElement postgresqlHostNameField;

    @FindBy(id = "postgresqlPort")
    WebElement postgresqlPortField;

    @FindBy(id = "postgresqlDatabaseName")
    WebElement postgresqlDatabaseNameField;

    @FindBy(id = "postgresqlUsername")
    WebElement postgresqlUsernameField;


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
        shouldContainText("must not be blank");
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

    public void containsInfoAboutNonPositiveValue() {
        shouldContainText("must be greater than or equal to 1");
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

    public void typeH2InMemoryDatabaseName(String databaseName) {
        h2InMemoryNameField.clear();
        h2InMemoryNameField.sendKeys(databaseName);
    }

    public void typeH2EmbeddedDatabaseName(String databaseName) {
        h2EmbeddedDatabaseFileNameField.clear();
        h2EmbeddedDatabaseFileNameField.sendKeys(databaseName);
    }

    public void typeH2EmbeddedUsername(String username) {
        h2EmbeddedUsernameField.clear();
        h2EmbeddedUsernameField.sendKeys(username);
    }

    public void typeH2ManagedDatabaseName(String databaseName) {
        h2ManagedServerDatabaseFileNameField.clear();
        h2ManagedServerDatabaseFileNameField.sendKeys(databaseName);
    }

    public void typeH2ManagedServerPort(String port) {
        h2ManagedServerPortField.clear();
        h2ManagedServerPortField.sendKeys(port);
    }

    public void typeH2ManagedServerUsername(String username) {
        h2ManagedServerUsernameField.clear();
        h2ManagedServerUsernameField.sendKeys(username);
    }

    public void typeH2RemoteServerUrl(String remoteServerUrl) {
        h2RemoteServerUrlField.clear();
        h2RemoteServerUrlField.sendKeys(remoteServerUrl);
    }

    public void typeH2RemoteServerUsername(String username) {
        h2RemoteServerUsernameField.clear();
        h2RemoteServerUsernameField.sendKeys(username);
    }

    public void typePostgresqlHostName(String hostName) {
        postgresqlHostNameField.clear();
        postgresqlHostNameField.sendKeys(hostName);
    }

    public void typePostgresqlPort(String port) {
        postgresqlPortField.clear();
        postgresqlPortField.sendKeys(port);
    }

    public void typePostgresqlDatabaseName(String databaseName) {
        postgresqlDatabaseNameField.clear();
        postgresqlDatabaseNameField.sendKeys(databaseName);
    }

    public void typePostgresqlUsername(String username) {
        postgresqlUsernameField.clear();
        postgresqlUsernameField.sendKeys(username);
    }

    public void containsInfoAboutBlankValue() {
        shouldContainText("must not be blank");
    }
}
