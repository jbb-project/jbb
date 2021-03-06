/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.web.session;

import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.WithTagValuesOf;

import org.jbb.e2e.serenity.Utils;
import org.jbb.e2e.serenity.web.EndToEndWebStories;
import org.jbb.e2e.serenity.web.commons.HomeSteps;
import org.jbb.e2e.serenity.web.membermanagement.AcpMemberBrowserSteps;
import org.jbb.e2e.serenity.web.registration.RegistrationSteps;
import org.jbb.e2e.serenity.web.signin.SignInSteps;
import org.junit.Test;
import org.openqa.selenium.Cookie;

import java.util.Set;

import static org.jbb.e2e.serenity.Tags.Feature;
import static org.jbb.e2e.serenity.Tags.Interface;
import static org.jbb.e2e.serenity.Tags.Release;
import static org.jbb.e2e.serenity.Tags.Type;


public class AcpSessionManagementStories extends EndToEndWebStories {

    @Steps
    RegistrationSteps registrationSteps;

    @Steps
    AcpMemberBrowserSteps acpMemberBrowserSteps;

    @Steps
    AcpSessionManagementSteps sessionManagementSteps;

    @Steps
    SignInSteps signInSteps;

    @Steps
    HomeSteps homeSteps;

    @Test
    @WithTagValuesOf({Interface.WEB, Type.SMOKE, Feature.SESSION_SETTINGS, Release.VER_0_8_0})
    public void update_session_maximum_inactive_interval_time_should_be_possible() {
        //given
        make_rollback_after_test_case(set_session_maximum_inactive_interval("3600"));
        signInSteps.sign_in_as_administrator_with_success();

        //when
        sessionManagementSteps.open_session_management_page();
        sessionManagementSteps.type_maximum_inactive_interval("5000");
        sessionManagementSteps.save_session_settings_form();

        //then
        sessionManagementSteps.should_be_informed_about_saving_settings();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.SESSION_SETTINGS, Release.VER_0_8_0})
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
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.SESSION_SETTINGS, Release.VER_0_8_0})
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
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.SESSION_SETTINGS, Release.VER_0_8_0})
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
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.SESSION_SETTINGS, Release.VER_0_8_0})
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
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.SESSION_SETTINGS, Release.VER_0_8_0})
    public void session_for_signed_in_member_should_be_visible_in_session_management() {

        //given
        String testUsername = "session1";
        make_rollback_after_test_case(delete_testbed_member(testUsername));
        registrationSteps
                .register_new_member(testUsername, "Session test user", "session1@session.com",
                        "session", "session");
        signInSteps.sign_in_with_credentials_with_success(testUsername, "session", "Session test user");
        Utils.delete_all_cookies();
        signInSteps.sign_in_as_administrator_with_success();

        //when
        sessionManagementSteps.open_session_management_page();

        //then
        sessionManagementSteps.session_for_member_should_be_visible(testUsername);
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.SESSION_SETTINGS, Release.VER_0_8_0})
    public void session_for_signed_in_member_should_not_be_visible_in_session_management_when_member_sign_out() {

        //given
        String testUsername = "session2";
        make_rollback_after_test_case(delete_testbed_member(testUsername));
        registrationSteps
                .register_new_member(testUsername, "Session test user 2", "session2@session.com",
                        "session", "session");
        signInSteps.sign_in_with_credentials_with_success(testUsername, "session", "Session test user 2");
        signInSteps.sign_out();
        signInSteps.sign_in_as_administrator_with_success();

        //when
        sessionManagementSteps.open_session_management_page();

        //then
        sessionManagementSteps.session_for_member_should_not_be_visible(testUsername);
    }


    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.SESSION_SETTINGS, Release.VER_0_8_0})
    public void after_session_deletion_member_should_not_be_visible_in_session_management_and_sign_out() {

        //given
        String testUsername = "session3";
        make_rollback_after_test_case(delete_testbed_member(testUsername));
        registrationSteps
                .register_new_member(testUsername, "Session test user 3", "session3@session.com",
                        "session", "session");
        signInSteps.sign_in_with_credentials_with_success(testUsername, "session", "Session test user 3");
        Set<Cookie> testUsernameCookies = Utils.get_current_cookies();
        Utils.delete_all_cookies();
        signInSteps.sign_in_as_administrator_with_success();

        //when
        sessionManagementSteps.open_session_management_page();
        sessionManagementSteps.delete_latest_session_for_member(testUsername);

        //then
        sessionManagementSteps.session_for_member_should_not_be_visible(testUsername);
        Utils.set_cookies(testUsernameCookies);
        signInSteps.member_should_not_be_sign_in();

        // for rollback
        signInSteps.sign_in_as_administrator_with_success();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.SESSION_SETTINGS, Release.VER_0_8_0})
    public void after_maximum_inactive_interval_member_should_be_sign_out_automatically() throws InterruptedException {

        //given
        String testUsername = "session4";
        make_rollback_after_test_case(
                set_session_maximum_inactive_interval("3600"),
                delete_testbed_member(testUsername)
        );

        signInSteps.sign_in_as_administrator_with_success();
        sessionManagementSteps.set_session_maximum_inactive_interval("5");
        signInSteps.sign_out();
        Thread.sleep(10000); //NOSONAR
        registrationSteps
                .register_new_member(testUsername, "Session test user 4", "session4@session.com",
                        "session", "session");

        //when
        signInSteps.sign_in_with_credentials_with_success(testUsername, "session", "Session test user 4");
        Thread.sleep(10000); //NOSONAR

        //then
        homeSteps.opens_home_page();
        signInSteps.member_should_not_be_sign_in();

        // for rollback
        signInSteps.sign_in_as_administrator_with_success();
    }

    RollbackAction delete_testbed_member(String username) {
        return () -> {
            acpMemberBrowserSteps.remove_member_with_username(username);
        };
    }

    RollbackAction set_session_maximum_inactive_interval(String inactiveInterval) {
        return () -> {
            sessionManagementSteps.set_session_maximum_inactive_interval(inactiveInterval);
        };
    }

}
