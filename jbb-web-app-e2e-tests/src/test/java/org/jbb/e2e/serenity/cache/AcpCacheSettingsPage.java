/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.cache;

import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.DefaultUrl;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;

@DefaultUrl(AcpCacheSettingsPage.URL)
public class AcpCacheSettingsPage extends PageObject {

    public static final String URL = "/acp/general/cache";

    @FindBy(id = "hazelcastServerGroupName")
    WebElement hazelcastServerGroupField;

    @FindBy(id = "hazelcastServerPort")
    WebElement hazelcastServerPortField;

    @FindBy(id = "hazelcastServerMembers")
    WebElement hazelcastServerBootstrapNodesField;

    @FindBy(id = "hazelcastClientGroupName")
    WebElement hazelcastClientGroupField;

    @FindBy(id = "hazelcastClientMembers")
    WebElement hazelcastClientBootstrapNodesField;

    @FindBy(id = "hazelcastClientConnectionAttemptLimit")
    WebElement hazelcastClientConnectionAttemptLimitField;

    @FindBy(id = "hazelcastClientConnectionAttemptPeriod")
    WebElement hazelcastClientConnectionAttemptPeriodField;

    @FindBy(id = "hazelcastClientConnectionTimeout")
    WebElement hazelcastClientConnectionTimeoutField;

    @FindBys({@FindBy(xpath = "//button[contains(text(),'Save')]")})
    WebElement saveButton;

    public void clickSaveButton() {
        saveButton.click();
    }

    public void typeHazelcastServerGroupName(String groupName) {
        hazelcastServerGroupField.clear();
        hazelcastServerGroupField.sendKeys(groupName);
    }

    public void shouldBeVisibleInfoAboutPositiveValue() {
        shouldContainText("must be greater than or equal to 1");
    }

    public void shouldBeVisibleInfoAboutInvalidValue() {
        shouldContainText("Invalid value");
    }

    public void shouldBeVisibleInfoAboutBlankValue() {
        shouldContainText("must not be blank");
    }

    public void typeHazelcastServerPort(String port) {
        hazelcastServerPortField.clear();
        hazelcastServerPortField.sendKeys(port);
    }

    public void typeHazelcastServerBootstrapNodes(String bootstrapNodes) {
        hazelcastServerBootstrapNodesField.clear();
        hazelcastServerBootstrapNodesField.sendKeys(bootstrapNodes);
    }

    public void typeHazelcastClientGroupName(String groupName) {
        hazelcastClientGroupField.clear();
        hazelcastClientGroupField.sendKeys(groupName);
    }

    public void typeHazelcastClientBootstrapNodes(String boostrapNodes) {
        hazelcastClientBootstrapNodesField.clear();
        hazelcastClientBootstrapNodesField.sendKeys(boostrapNodes);
    }

    public void typeHazelcastClientAttemptLimit(String attemptLimit) {
        hazelcastClientConnectionAttemptLimitField.clear();
        hazelcastClientConnectionAttemptLimitField.sendKeys(attemptLimit);
    }

    public void typeHazelcastClientAttemptPeriod(String attemptPeriod) {
        hazelcastClientConnectionAttemptPeriodField.clear();
        hazelcastClientConnectionAttemptPeriodField.sendKeys(attemptPeriod);
    }

    public void typeHazelcastClientConnectionTimeout(String connectionTimeout) {
        hazelcastClientConnectionTimeoutField.clear();
        hazelcastClientConnectionTimeoutField.sendKeys(connectionTimeout);
    }

    public void shouldBeVisibleInfoAboutEmptyValue() {
        shouldContainText("must not be empty");
    }
}
