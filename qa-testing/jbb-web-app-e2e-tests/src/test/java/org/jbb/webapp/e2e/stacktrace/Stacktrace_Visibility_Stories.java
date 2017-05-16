/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.webapp.e2e.stacktrace;

import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.WithTagValuesOf;

import org.jbb.webapp.e2e.Jbb_Base_Stories;
import org.jbb.webapp.e2e.Tags;
import org.jbb.webapp.e2e.logging.LoggingSettingsSteps;
import org.jbb.webapp.e2e.registration.RegistrationSteps;
import org.jbb.webapp.e2e.signin.SignInSteps;
import org.junit.Before;
import org.junit.Test;

import static org.apache.commons.lang3.StringUtils.isNoneBlank;

public class Stacktrace_Visibility_Stories extends Jbb_Base_Stories {

    private static String testUserPassword;
    private static String testUserDisplayedName;
    private static String testUserEmail;

    @Steps
    RegistrationSteps anonRegistrationUser;

    @Steps
    SignInSteps signInUser;

    @Steps
    LoggingSettingsSteps loggingSettingsSteps;

    @Steps
    StacktraceVisibilitySteps stacktraceVisibilitySteps;

    @Before
    public void setUp() throws Exception {
        // assume
        registerTestUserIfNeeded();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.STACKTRACE_VISIBILITY_SETTINGS, Tags.Release.VER_0_6_0})
    public void setting_stacktrace_visibility_for_nobody() throws Exception {
        signInAsAdministrator();
        loggingSettingsSteps.open_logging_settings_page();
        loggingSettingsSteps.select_stacktrace_visibility_level("Nobody");
        loggingSettingsSteps.send_logging_settings_form();
        signInUser.sign_out();

        stacktraceVisibilitySteps.open_error_page();
        stacktraceVisibilitySteps.should_not_contain_stacktrace();

        signInAsTestUser();
        stacktraceVisibilitySteps.open_error_page();
        stacktraceVisibilitySteps.should_not_contain_stacktrace();
        signInUser.sign_out();

        signInAsAdministrator();
        stacktraceVisibilitySteps.open_error_page();
        stacktraceVisibilitySteps.should_not_contain_stacktrace();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.STACKTRACE_VISIBILITY_SETTINGS, Tags.Release.VER_0_6_0})
    public void setting_stacktrace_visibility_for_administrators() throws Exception {
        signInAsAdministrator();
        loggingSettingsSteps.open_logging_settings_page();
        loggingSettingsSteps.select_stacktrace_visibility_level("Administrators");
        loggingSettingsSteps.send_logging_settings_form();
        signInUser.sign_out();

        stacktraceVisibilitySteps.open_error_page();
        stacktraceVisibilitySteps.should_not_contain_stacktrace();

        signInAsTestUser();
        stacktraceVisibilitySteps.open_error_page();
        stacktraceVisibilitySteps.should_not_contain_stacktrace();
        signInUser.sign_out();

        signInAsAdministrator();
        stacktraceVisibilitySteps.open_error_page();
        stacktraceVisibilitySteps.should_contain_stacktrace();
    }

    @Test
    @WithTagValuesOf({Tags.Type.SMOKE, Tags.Feature.STACKTRACE_VISIBILITY_SETTINGS, Tags.Release.VER_0_6_0})
    public void setting_stacktrace_visibility_for_members() throws Exception {
        signInAsAdministrator();
        loggingSettingsSteps.open_logging_settings_page();
        loggingSettingsSteps.select_stacktrace_visibility_level("Users");
        loggingSettingsSteps.send_logging_settings_form();
        signInUser.sign_out();

        stacktraceVisibilitySteps.open_error_page();
        stacktraceVisibilitySteps.should_not_contain_stacktrace();

        signInAsTestUser();
        stacktraceVisibilitySteps.open_error_page();
        stacktraceVisibilitySteps.should_contain_stacktrace();
        signInUser.sign_out();

        signInAsAdministrator();
        stacktraceVisibilitySteps.open_error_page();
        stacktraceVisibilitySteps.should_contain_stacktrace();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.STACKTRACE_VISIBILITY_SETTINGS, Tags.Release.VER_0_6_0})
    public void setting_stacktrace_visibility_for_everybody() throws Exception {
        signInAsAdministrator();
        loggingSettingsSteps.open_logging_settings_page();
        loggingSettingsSteps.select_stacktrace_visibility_level("Everybody");
        loggingSettingsSteps.send_logging_settings_form();
        signInUser.sign_out();

        stacktraceVisibilitySteps.open_error_page();
        stacktraceVisibilitySteps.should_contain_stacktrace();

        signInAsTestUser();
        stacktraceVisibilitySteps.open_error_page();
        stacktraceVisibilitySteps.should_contain_stacktrace();
        signInUser.sign_out();

        signInAsAdministrator();
        stacktraceVisibilitySteps.open_error_page();
        stacktraceVisibilitySteps.should_contain_stacktrace();
    }

    private void signInAsAdministrator() {
        signInUser.sign_in_with_credentials_with_success("administrator", "administrator", "Administrator");
    }

    private void signInAsTestUser() {
        signInUser.sign_in_with_credentials_with_success("stacktracetest", "stacktracetest", "StackTraceUser");
    }

    private void registerTestUserIfNeeded() {
        if (!isNoneBlank(testUserPassword, testUserDisplayedName, testUserEmail)) {
            testUserPassword = "stacktracetest";
            testUserDisplayedName = "StackTraceUser";
            testUserEmail = "stack@trace.com";

            anonRegistrationUser.register_new_member("stacktracetest", testUserDisplayedName, testUserEmail,
                    testUserPassword, testUserPassword);
        }
    }
}
