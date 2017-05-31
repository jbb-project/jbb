/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.stacktrace;

import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.WithTagValuesOf;

import org.jbb.e2e.serenity.JbbBaseSerenityStories;
import org.jbb.e2e.serenity.Tags;
import org.jbb.e2e.serenity.logging.LoggingSettingsSteps;
import org.jbb.e2e.serenity.registration.RegistrationSteps;
import org.jbb.e2e.serenity.signin.SignInSteps;
import org.junit.Before;
import org.junit.Test;

import static org.apache.commons.lang3.StringUtils.isNoneBlank;

public class StacktraceVisibilityStories extends JbbBaseSerenityStories {

    private static String testUserPassword;
    private static String testUserDisplayedName;
    private static String testUserEmail;

    @Steps
    RegistrationSteps registrationSteps;
    @Steps
    SignInSteps signInSteps;
    @Steps
    LoggingSettingsSteps loggingSettingsSteps;
    @Steps
    StacktraceVisibilitySteps stacktraceVisibilitySteps;

    @Before
    public void setUp() throws Exception {
        // assume
        register_test_member_if_needed();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.STACKTRACE_VISIBILITY_SETTINGS, Tags.Release.VER_0_6_0})
    public void setting_stacktrace_visibility_for_nobody() throws Exception {
        signInSteps.sign_in_as_administrator_with_success();
        loggingSettingsSteps.open_logging_settings_page();
        loggingSettingsSteps.select_stacktrace_visibility_level("Nobody");
        loggingSettingsSteps.send_logging_settings_form();
        signInSteps.sign_out();

        stacktraceVisibilitySteps.open_error_page();
        stacktraceVisibilitySteps.should_not_contain_stacktrace();

        sign_in_as_test_member();
        stacktraceVisibilitySteps.open_error_page();
        stacktraceVisibilitySteps.should_not_contain_stacktrace();
        signInSteps.sign_out();

        signInSteps.sign_in_as_administrator_with_success();
        stacktraceVisibilitySteps.open_error_page();
        stacktraceVisibilitySteps.should_not_contain_stacktrace();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.STACKTRACE_VISIBILITY_SETTINGS, Tags.Release.VER_0_6_0})
    public void setting_stacktrace_visibility_for_administrators() throws Exception {
        signInSteps.sign_in_as_administrator_with_success();
        loggingSettingsSteps.open_logging_settings_page();
        loggingSettingsSteps.select_stacktrace_visibility_level("Administrators");
        loggingSettingsSteps.send_logging_settings_form();
        signInSteps.sign_out();

        stacktraceVisibilitySteps.open_error_page();
        stacktraceVisibilitySteps.should_not_contain_stacktrace();

        sign_in_as_test_member();
        stacktraceVisibilitySteps.open_error_page();
        stacktraceVisibilitySteps.should_not_contain_stacktrace();
        signInSteps.sign_out();

        signInSteps.sign_in_as_administrator_with_success();
        stacktraceVisibilitySteps.open_error_page();
        stacktraceVisibilitySteps.should_contain_stacktrace();
    }

    @Test
    @WithTagValuesOf({Tags.Type.SMOKE, Tags.Feature.STACKTRACE_VISIBILITY_SETTINGS, Tags.Release.VER_0_6_0})
    public void setting_stacktrace_visibility_for_members() throws Exception {
        signInSteps.sign_in_as_administrator_with_success();
        loggingSettingsSteps.open_logging_settings_page();
        loggingSettingsSteps.select_stacktrace_visibility_level("Users");
        loggingSettingsSteps.send_logging_settings_form();
        signInSteps.sign_out();

        stacktraceVisibilitySteps.open_error_page();
        stacktraceVisibilitySteps.should_not_contain_stacktrace();

        sign_in_as_test_member();
        stacktraceVisibilitySteps.open_error_page();
        stacktraceVisibilitySteps.should_contain_stacktrace();
        signInSteps.sign_out();

        signInSteps.sign_in_as_administrator_with_success();
        stacktraceVisibilitySteps.open_error_page();
        stacktraceVisibilitySteps.should_contain_stacktrace();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.STACKTRACE_VISIBILITY_SETTINGS, Tags.Release.VER_0_6_0})
    public void setting_stacktrace_visibility_for_everybody() throws Exception {
        signInSteps.sign_in_as_administrator_with_success();
        loggingSettingsSteps.open_logging_settings_page();
        loggingSettingsSteps.select_stacktrace_visibility_level("Everybody");
        loggingSettingsSteps.send_logging_settings_form();
        signInSteps.sign_out();

        stacktraceVisibilitySteps.open_error_page();
        stacktraceVisibilitySteps.should_contain_stacktrace();

        sign_in_as_test_member();
        stacktraceVisibilitySteps.open_error_page();
        stacktraceVisibilitySteps.should_contain_stacktrace();
        signInSteps.sign_out();

        signInSteps.sign_in_as_administrator_with_success();
        stacktraceVisibilitySteps.open_error_page();
        stacktraceVisibilitySteps.should_contain_stacktrace();
    }

    private void sign_in_as_test_member() {
        signInSteps.sign_in_with_credentials_with_success("stacktracetest", "stacktracetest", "StackTraceUser");
    }

    private void register_test_member_if_needed() {
        if (!isNoneBlank(testUserPassword, testUserDisplayedName, testUserEmail)) {
            testUserPassword = "stacktracetest";
            testUserDisplayedName = "StackTraceUser";
            testUserEmail = "stack@trace.com";

            registrationSteps.register_new_member("stacktracetest", testUserDisplayedName, testUserEmail,
                    testUserPassword, testUserPassword);
        }
    }
}
