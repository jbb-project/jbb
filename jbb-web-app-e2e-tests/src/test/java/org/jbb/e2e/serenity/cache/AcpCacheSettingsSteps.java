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

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;

public class AcpCacheSettingsSteps extends ScenarioSteps {

    AcpCacheSettingsPage acpCacheSettingsPage;

    @Step
    public void open_cache_settings() {
        acpCacheSettingsPage.open();
    }

    @Step
    public void save_cache_settings_form() {
        acpCacheSettingsPage.clickSaveButton();
    }

    @Step
    public void type_hazelcast_server_group_name(String groupName) {
        acpCacheSettingsPage.typeHazelcastServerGroupName(groupName);
    }

    @Step
    public void should_be_informed_that_value_must_be_not_empty() {
        acpCacheSettingsPage.shouldBeVisibleInfoAboutEmptyValue();
    }

    @Step
    public void type_hazelcast_server_port(String port) {
        acpCacheSettingsPage.typeHazelcastServerPort(port);
    }

    @Step
    public void should_be_informed_about_invalid_value() {
        acpCacheSettingsPage.shouldBeVisibleInfoAboutInvalidValue();
    }

    @Step
    public void should_be_informed_that_value_must_be_positive_number() {
        acpCacheSettingsPage.shouldBeVisibleInfoAboutPositiveValue();
    }

    @Step
    public void type_hazelcast_server_bootstrap_nodes(String bootstrapNodes) {
        acpCacheSettingsPage.typeHazelcastServerBootstrapNodes(bootstrapNodes);
    }

    @Step
    public void type_hazelcast_client_group_name(String groupName) {
        acpCacheSettingsPage.typeHazelcastClientGroupName(groupName);
    }

    @Step
    public void type_hazelcast_client_bootstrap_nodes(String boostrapNodes) {
        acpCacheSettingsPage.typeHazelcastClientBootstrapNodes(boostrapNodes);
    }

    @Step
    public void type_hazelcast_client_attempt_limit(String attemptLimit) {
        acpCacheSettingsPage.typeHazelcastClientAttemptLimit(attemptLimit);
    }

    @Step
    public void type_hazelcast_client_attempt_period(String attemptPeriod) {
        acpCacheSettingsPage.typeHazelcastClientAttemptPeriod(attemptPeriod);
    }

    @Step
    public void type_hazelcast_client_connection_timeout(String connectionTimeout) {
        acpCacheSettingsPage.typeHazelcastClientConnectionTimeout(connectionTimeout);
    }
}
