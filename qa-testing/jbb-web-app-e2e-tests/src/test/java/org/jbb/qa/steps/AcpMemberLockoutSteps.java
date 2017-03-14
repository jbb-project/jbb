/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.qa.steps;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;

import org.jbb.qa.pages.AcpMemberLockoutSettingsPage;

public class AcpMemberLockoutSteps extends ScenarioSteps {
    AcpMemberLockoutSettingsPage lockoutSettingsPage;

    @Step
    public void open_member_lockout_settings() {
        lockoutSettingsPage.open();
    }

    @Step
    public void click_for_enabling_lockout_feature() {
        lockoutSettingsPage.clickEnableLockoutRadioButton();
    }

    @Step
    public void click_for_disabling_lockout_feature() {
        lockoutSettingsPage.clickDisableLockoutRadioButton();
    }

    @Step
    public void type_failed_attempts_threshold(String failedAttemptsThreshold) {
        lockoutSettingsPage.typeFailedAttemptsThreshold(failedAttemptsThreshold);
    }

    @Step
    public void type_failed_attempts_expiration(String failedAttemptsExpiration) {
        lockoutSettingsPage.typeFailedAttemptsExpiration(failedAttemptsExpiration);
    }

    @Step
    public void type_lockout_duration(String lockoutDuration) {
        lockoutSettingsPage.typeLockoutDuration(lockoutDuration);
    }

    @Step
    public void set_lockout_settings(boolean enabled, String attemptsThreshold, String attemptsExpiration, String lockoutDuration) {
        if (enabled) {
            click_for_enabling_lockout_feature();
        } else {
            click_for_disabling_lockout_feature();
        }
        type_failed_attempts_threshold(attemptsThreshold);
        type_failed_attempts_expiration(attemptsExpiration);
        type_lockout_duration(lockoutDuration);
    }

    @Step
    public void save_lockout_settings_form() {
        lockoutSettingsPage.clickSaveButton();
    }

    @Step
    public void should_be_informed_that_value_must_be_equal_or_greater_than_one() {
        lockoutSettingsPage.shouldBeVisibleInfoAboutPositiveValue();
    }

    @Step
    public void should_be_informed_that_value_is_invalid() {
        lockoutSettingsPage.shouldBeVisibleInfoAboutInvalidValue();
    }
}
