/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.web.logging;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;

public class LoggingSettingsSteps extends ScenarioSteps {

    AcpLoggingSettingsPage loggingSettingsPage;
    RemovingRootLoggerInvalidPage removingRootLoggerInvalidPage;

    @Step
    public void open_logging_settings_page() {
        loggingSettingsPage.open();
    }

    @Step
    public void send_logging_settings_form() {
        loggingSettingsPage.clickSaveButton();
    }

    @Step
    public void click_for_add_new_console_appender() {
        loggingSettingsPage.clickAddConsoleAppenderLink();
    }

    @Step
    public void type_console_appender_name(String appenderName) {
        loggingSettingsPage.typeConsoleAppenderName(appenderName);
    }

    @Step
    public void type_log_pattern(String pattern) {
        loggingSettingsPage.typeLogPattern(pattern);
    }

    @Step
    public void send_appender_form() {
        loggingSettingsPage.clickSaveButton();
    }

    @Step
    public void should_contain_warn_about_blank_field() {
        loggingSettingsPage.shouldContainWarnAboutBlankField();
    }

    @Step
    public void should_contain_warn_about_busy_appender_name() {
        loggingSettingsPage.shouldContainWarnAboutBusyAppenderName();
    }

    @Step
    public void should_contain_info_about_success() {
        loggingSettingsPage.shouldContainInfoAboutSuccess();
    }

    @Step
    public void should_contain_appender_name(String appenderName) {
        loggingSettingsPage.shouldContainAppenderName(appenderName);
    }

    @Step
    public void click_edit_appender(String appenderName) {
        loggingSettingsPage.clickEditLinkForAppender(appenderName);
    }

    @Step
    public void click_delete_appender(String appenderName) {
        loggingSettingsPage.clickDeleteForAppender(appenderName);
    }

    @Step
    public void should_not_contain_appender_name(String appenderName) {
        loggingSettingsPage.shouldNotContainAppenderName(appenderName);
    }

    @Step
    public void click_for_add_new_file_appender() {
        loggingSettingsPage.clickAddFileAppenderLink();
    }

    @Step
    public void type_file_appender_name(String appenderName) {
        loggingSettingsPage.typeFileAppenderName(appenderName);
    }

    @Step
    public void type_current_log_file_name(String currentLogFileName) {
        loggingSettingsPage.typeCurrentLogFileName(currentLogFileName);
    }

    @Step
    public void type_rotation_file_name_pattern(String rotationPattern) {
        loggingSettingsPage.typeRotationFileNamePattern(rotationPattern);
    }

    @Step
    public void type_max_size_file(String maxSizeFile) {
        loggingSettingsPage.typeMaxSizeFile(maxSizeFile);
    }

    @Step
    public void type_max_history(String maxHistory) {
        loggingSettingsPage.typeMaxHistory(maxHistory);
    }

    @Step
    public void should_contain_warn_about_incorrect_max_file_size() {
        loggingSettingsPage.shouldContainWarnAboutIncorrectMaxFileSize();
    }

    @Step
    public void should_contain_warn_about_negative_value() {
        loggingSettingsPage.shouldContainsWarnAboutNegativeValue();
    }

    @Step
    public void should_contain_warn_about_invalid_value() {
        loggingSettingsPage.shouldContainWarnAboutInvalidValue();
    }

    @Step
    public void click_for_add_new_logger() {
        loggingSettingsPage.clickAddLoggerLink();
    }

    @Step
    public void type_logger_name(String loggerName) {
        loggingSettingsPage.typeLoggerName(loggerName);
    }

    @Step
    public void send_logger_form() {
        loggingSettingsPage.clickSaveButton();
    }

    @Step
    public void should_contain_warn_about_incorrect_logger_name() {
        loggingSettingsPage.shouldContainWarnAboutIncorrectLoggerName();
    }

    @Step
    public void should_contain_warn_about_busy_logger_name() {
        loggingSettingsPage.shouldContainWarnAboutBusyLoggerName();
    }

    @Step
    public void should_contain_logger_name(String loggerName) {
        loggingSettingsPage.shouldContainLoggerName(loggerName);
    }

    @Step
    public void click_delete_logger(String loggerName) {
        loggingSettingsPage.clickDeleteForLogger(loggerName);
    }

    @Step
    public void should_not_contain_logger_name(String loggerName) {
        loggingSettingsPage.shouldNotContainLoggerName(loggerName);
    }

    @Step
    public void open_address_for_removing_root_logger() {
        removingRootLoggerInvalidPage.open();
    }

    @Step
    public void should_contain_error_about_attempting_to_remove_root_logger() {
        removingRootLoggerInvalidPage.shouldContainErrorAboutAttemptingToRemoveRootLogger();
    }
}
