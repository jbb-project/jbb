/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.session;

import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.WithTagValuesOf;

import org.jbb.e2e.serenity.JbbBaseSerenityStories;
import org.jbb.e2e.serenity.Tags;
import org.jbb.e2e.serenity.Utils;
import org.jbb.e2e.serenity.membermanagement.AcpMemberBrowserSteps;
import org.jbb.e2e.serenity.registration.RegistrationSteps;
import org.jbb.e2e.serenity.signin.SignInSteps;
import org.junit.Test;


public class AcpSessionManagementStories extends JbbBaseSerenityStories {

    @Steps
    RegistrationSteps registrationSteps;

    @Steps
    AcpMemberBrowserSteps acpMemberBrowserSteps;

    @Steps
    AcpSessionManagementSteps sessionManagementSteps;

    @Steps
    SignInSteps signInSteps;

    @Test
    @WithTagValuesOf({Tags.Type.SMOKE, Tags.Feature.SESSION_SETTINGS, Tags.Release.VER_0_8_0})
    public void update_session_maximum_inactive_interval_time_should_be_possible() {
        //given
        signInSteps.sign_in_as_administrator_with_success();

        //when
        sessionManagementSteps.open_session_management_page();
        sessionManagementSteps.type_maximum_inactive_interval("5000");
        sessionManagementSteps.save_session_settings_form();

        //then
        sessionManagementSteps.should_be_informed_about_saving_settings();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.SESSION_SETTINGS, Tags.Release.VER_0_8_0})
    public void editing_session_maximum_inactive_interval_time_with_empty_value_is_impossible() {
        //given
        signInSteps.sign_in_as_administrator_with_success();

        //when
        sessionManagementSteps.open_session_management_page();
        sessionManagementSteps.type_maximum_inactive_interval("");
        sessionManagementSteps.save_session_settings_form();

        //then
        sessionManagementSteps.should_be_informed_about_null_value();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.SESSION_SETTINGS, Tags.Release.VER_0_8_0})
    public void editing_session_maximum_inactive_interval_time_with_text_value_is_impossible() {

        //given
        signInSteps.sign_in_as_administrator_with_success();

        //when
        sessionManagementSteps.open_session_management_page();
        sessionManagementSteps.type_maximum_inactive_interval("abcs");
        sessionManagementSteps.save_session_settings_form();

        //then
        sessionManagementSteps.should_be_informed_about_invalid_value();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.SESSION_SETTINGS, Tags.Release.VER_0_8_0})
    public void editing_session_maximum_inactive_interval_time_with_negative_value_is_impossible() {

        //given
        signInSteps.sign_in_as_administrator_with_success();

        //when
        sessionManagementSteps.open_session_management_page();
        sessionManagementSteps.type_maximum_inactive_interval("-1");
        sessionManagementSteps.save_session_settings_form();

        //then
        sessionManagementSteps.should_be_informed_about_non_positive_value();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.SESSION_SETTINGS, Tags.Release.VER_0_8_0})
    public void editing_session_maximum_inactive_interval_time_with_zero_is_impossible() {

        //given
        signInSteps.sign_in_as_administrator_with_success();

        //when
        sessionManagementSteps.open_session_management_page();
        sessionManagementSteps.type_maximum_inactive_interval("0");
        sessionManagementSteps.save_session_settings_form();

        //then
        sessionManagementSteps.should_be_informed_about_non_positive_value();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.SESSION_SETTINGS, Tags.Release.VER_0_8_0})
    public void session_for_signed_in_member_should_be_visible_in_session_management() {

        //given
        String testUsername = "session1";
        make_rollback_after_test_case(delete_testbed_member(testUsername));
        registrationSteps.register_new_member(testUsername, "Session test user", "session@session.com", "session", "session");
        signInSteps.sign_in_with_credentials_with_success(testUsername, "session", "Session test user");
        Utils.delete_all_cookies();
        signInSteps.sign_in_as_administrator_with_success();

        //when
        sessionManagementSteps.open_session_management_page();

        //then
        sessionManagementSteps.session_for_member_should_be_visible(testUsername);
    }


    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.SESSION_SETTINGS, Tags.Release.VER_0_8_0})
    public void session_for_signed_in_member_should_not_be_visible_in_session_management_when_member_sign_out() {

        //given
        String testUsername = "session2";
        make_rollback_after_test_case(delete_testbed_member(testUsername));
        registrationSteps.register_new_member(testUsername, "Session test user 2", "session@session.com", "session", "session");
        signInSteps.sign_in_with_credentials_with_success(testUsername, "session", "Session test user 2");
        signInSteps.sign_out();
        signInSteps.sign_in_as_administrator_with_success();

        //when
        sessionManagementSteps.open_session_management_page();

        //then
        sessionManagementSteps.session_for_member_should_not_be_visible(testUsername);
    }

    RollbackAction delete_testbed_member(String username) {
        return () -> {
            acpMemberBrowserSteps.remove_member_with_username(username);
        };
    }

}
