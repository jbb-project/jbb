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

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;

public class DatabaseProviderSettingsSteps extends ScenarioSteps {

    AcpDatabaseSettingsPage databaseSettingsPage;

    @Step
    public void open_database_settings_page() {
        databaseSettingsPage.open();
    }

    @Step
    public void send_database_settings_form() {
        databaseSettingsPage.clickSaveButton();
    }

    @Step
    public void should_be_informed_about_invalid_value() {
        databaseSettingsPage.containsInfoAboutInvalidValue();
    }

    @Step
    public void should_be_informed_about_negative_value() {
        databaseSettingsPage.containsInfoAboutNegativeValue();
    }

    @Step
    public void should_be_informed_about_blank_value() {
        databaseSettingsPage.containsInfoAboutBlankValue();
    }

    @Step
    public void type_h2_in_memory_database_name(String databaseName) {
        databaseSettingsPage.typeH2InMemoryDatabaseName(databaseName);
    }

    @Step
    public void type_h2_embedded_server_database_name(String databaseName) {
        databaseSettingsPage.typeH2EmbeddedDatabaseName(databaseName);
    }

    @Step
    public void type_h2_embedded_server_username(String username) {
        databaseSettingsPage.typeH2EmbeddedUsername(username);
    }

    @Step
    public void type_h2_managed_server_database_name(String databaseName) {
        databaseSettingsPage.typeH2ManagedDatabaseName(databaseName);
    }

    @Step
    public void type_h2_managed_server_port(String port) {
        databaseSettingsPage.typeH2ManagedServerPort(port);
    }

    @Step
    public void should_be_informed_about_non_positive_value() {
        databaseSettingsPage.containsInfoAboutNonPositiveValue();
    }

    @Step
    public void type_h2_managed_server_username(String username) {
        databaseSettingsPage.typeH2ManagedServerUsername(username);
    }

    @Step
    public void type_h2_remote_server_url(String remoteServerUrl) {
        databaseSettingsPage.typeH2RemoteServerUrl(remoteServerUrl);
    }

    @Step
    public void type_h2_remote_server_username(String username) {
        databaseSettingsPage.typeH2RemoteServerUsername(username);
    }
}
