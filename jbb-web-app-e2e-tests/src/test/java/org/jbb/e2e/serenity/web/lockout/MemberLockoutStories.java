/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.web.lockout;

import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.WithTagValuesOf;

import org.jbb.e2e.serenity.web.EndToEndWebStories;
import org.jbb.e2e.serenity.web.membermanagement.AcpMemberBrowserSteps;
import org.jbb.e2e.serenity.web.registration.RegistrationSteps;
import org.jbb.e2e.serenity.web.signin.SignInSteps;
import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.jbb.e2e.serenity.Tags.Feature;
import static org.jbb.e2e.serenity.Tags.Interface;
import static org.jbb.e2e.serenity.Tags.Release;
import static org.jbb.e2e.serenity.Tags.Type;

public class MemberLockoutStories extends EndToEndWebStories {

    @Steps
    SignInSteps signInSteps;
    @Steps
    AcpMemberLockoutSteps memberLockoutSteps;
    @Steps
    RegistrationSteps registrationSteps;
    @Steps
    AcpMemberBrowserSteps acpMemberBrowserSteps;

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.MEMBER_LOCKOUT, Release.VER_0_7_0})
    public void update_failed_attempts_threshold_to_negative_value_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        memberLockoutSteps.open_member_lockout_settings();
        memberLockoutSteps.type_failed_attempts_threshold("-1");
        memberLockoutSteps.save_lockout_settings_form();

        // then
        memberLockoutSteps.should_be_informed_that_value_must_be_equal_or_greater_than_one();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.MEMBER_LOCKOUT, Release.VER_0_7_0})
    public void update_failed_attempts_threshold_to_zero_value_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        memberLockoutSteps.open_member_lockout_settings();
        memberLockoutSteps.type_failed_attempts_threshold("0");
        memberLockoutSteps.save_lockout_settings_form();

        // then
        memberLockoutSteps.should_be_informed_that_value_must_be_equal_or_greater_than_one();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.MEMBER_LOCKOUT, Release.VER_0_7_0})
    public void update_failed_attempts_threshold_to_text_value_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        memberLockoutSteps.open_member_lockout_settings();
        memberLockoutSteps.type_failed_attempts_threshold("aaaaa");
        memberLockoutSteps.save_lockout_settings_form();

        // then
        memberLockoutSteps.should_be_informed_that_value_is_invalid();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.MEMBER_LOCKOUT, Release.VER_0_7_0})
    public void update_failed_attempts_threshold_to_empty_value_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        memberLockoutSteps.open_member_lockout_settings();
        memberLockoutSteps.type_failed_attempts_threshold("");
        memberLockoutSteps.save_lockout_settings_form();

        // then
        memberLockoutSteps.should_be_informed_that_value_is_invalid();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.MEMBER_LOCKOUT, Release.VER_0_7_0})
    public void update_failed_attempts_threshold_to_blank_value_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        memberLockoutSteps.open_member_lockout_settings();
        memberLockoutSteps.type_failed_attempts_threshold("          ");
        memberLockoutSteps.save_lockout_settings_form();

        // then
        memberLockoutSteps.should_be_informed_that_value_is_invalid();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.MEMBER_LOCKOUT, Release.VER_0_7_0})
    public void update_failed_attempts_expiration_to_negative_value_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        memberLockoutSteps.open_member_lockout_settings();
        memberLockoutSteps.type_failed_attempts_expiration("-1");
        memberLockoutSteps.save_lockout_settings_form();

        // then
        memberLockoutSteps.should_be_informed_that_value_must_be_equal_or_greater_than_one();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.MEMBER_LOCKOUT, Release.VER_0_7_0})
    public void update_failed_attempts_expiration_to_zero_value_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        memberLockoutSteps.open_member_lockout_settings();
        memberLockoutSteps.type_failed_attempts_expiration("0");
        memberLockoutSteps.save_lockout_settings_form();

        // then
        memberLockoutSteps.should_be_informed_that_value_must_be_equal_or_greater_than_one();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.MEMBER_LOCKOUT, Release.VER_0_7_0})
    public void update_failed_attempts_expiration_to_text_value_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        memberLockoutSteps.open_member_lockout_settings();
        memberLockoutSteps.type_failed_attempts_expiration("aaaaa");
        memberLockoutSteps.save_lockout_settings_form();

        // then
        memberLockoutSteps.should_be_informed_that_value_is_invalid();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.MEMBER_LOCKOUT, Release.VER_0_7_0})
    public void update_failed_attempts_expiration_to_empty_value_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        memberLockoutSteps.open_member_lockout_settings();
        memberLockoutSteps.type_failed_attempts_expiration("");
        memberLockoutSteps.save_lockout_settings_form();

        // then
        memberLockoutSteps.should_be_informed_that_value_is_invalid();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.MEMBER_LOCKOUT, Release.VER_0_7_0})
    public void update_failed_attempts_expiration_to_blank_value_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        memberLockoutSteps.open_member_lockout_settings();
        memberLockoutSteps.type_failed_attempts_expiration("          ");
        memberLockoutSteps.save_lockout_settings_form();

        // then
        memberLockoutSteps.should_be_informed_that_value_is_invalid();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.MEMBER_LOCKOUT, Release.VER_0_7_0})
    public void update_lockout_duration_to_negative_value_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        memberLockoutSteps.open_member_lockout_settings();
        memberLockoutSteps.type_lockout_duration("-1");
        memberLockoutSteps.save_lockout_settings_form();

        // then
        memberLockoutSteps.should_be_informed_that_value_must_be_equal_or_greater_than_one();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.MEMBER_LOCKOUT, Release.VER_0_7_0})
    public void update_lockout_duration_to_zero_value_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        memberLockoutSteps.open_member_lockout_settings();
        memberLockoutSteps.type_lockout_duration("0");
        memberLockoutSteps.save_lockout_settings_form();

        // then
        memberLockoutSteps.should_be_informed_that_value_must_be_equal_or_greater_than_one();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.MEMBER_LOCKOUT, Release.VER_0_7_0})
    public void update_lockout_duration_to_text_value_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        memberLockoutSteps.open_member_lockout_settings();
        memberLockoutSteps.type_lockout_duration("aaaaa");
        memberLockoutSteps.save_lockout_settings_form();

        // then
        memberLockoutSteps.should_be_informed_that_value_is_invalid();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.MEMBER_LOCKOUT, Release.VER_0_7_0})
    public void update_lockout_duration_to_empty_value_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        memberLockoutSteps.open_member_lockout_settings();
        memberLockoutSteps.type_lockout_duration("");
        memberLockoutSteps.save_lockout_settings_form();

        // then
        memberLockoutSteps.should_be_informed_that_value_is_invalid();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.MEMBER_LOCKOUT, Release.VER_0_7_0})
    public void update_lockout_duration_to_blank_value_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        memberLockoutSteps.open_member_lockout_settings();
        memberLockoutSteps.type_lockout_duration("          ");
        memberLockoutSteps.save_lockout_settings_form();

        // then
        memberLockoutSteps.should_be_informed_that_value_is_invalid();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.MEMBER_LOCKOUT, Release.VER_0_7_0})
    public void when_lockout_is_disabled_exceeding_of_failure_sign_in_attempts_do_not_create_lock() {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        memberLockoutSteps.open_member_lockout_settings();
        memberLockoutSteps.set_lockout_settings(false, "6", "60", "10");
        memberLockoutSteps.save_lockout_settings_form();
        signInSteps.sign_out();
        registrationSteps.register_new_member("locktest1", "locktest1", "lock@test1.com", "locktest1", "locktest1");

        signInSteps.sign_in_with_credentials_with_failure("locktest1", "incorrectpass");
        signInSteps.sign_in_with_credentials_with_failure("locktest1", "incorrectpass");
        signInSteps.sign_in_with_credentials_with_failure("locktest1", "incorrectpass");
        signInSteps.sign_in_with_credentials_with_failure("locktest1", "incorrectpass");
        signInSteps.sign_in_with_credentials_with_failure("locktest1", "incorrectpass");
        signInSteps.sign_in_with_credentials_with_failure("locktest1", "incorrectpass");

        // then
        signInSteps.sign_in_with_credentials_with_success("locktest1", "locktest1", "locktest1");
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.SMOKE, Feature.MEMBER_LOCKOUT, Release.VER_0_7_0})
    public void when_lockout_is_enabled_exceeding_of_failure_sign_in_attempts_creates_lock() {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        memberLockoutSteps.open_member_lockout_settings();
        memberLockoutSteps.set_lockout_settings(true, "4", "60", "10");
        memberLockoutSteps.save_lockout_settings_form();
        signInSteps.sign_out();

        registrationSteps.register_new_member("locktest2", "locktest2", "lock@test2.com", "locktest2", "locktest2");

        signInSteps.sign_in_with_credentials_with_failure("locktest2", "incorrectpass");
        signInSteps.sign_in_with_credentials_with_failure("locktest2", "incorrectpass");
        signInSteps.sign_in_with_credentials_with_failure("locktest2", "incorrectpass");
        signInSteps.sign_in_with_credentials_with_failure("locktest2", "incorrectpass");

        // then
        signInSteps.sign_in_with_credentials_with_failure_due_to_lockout("locktest2", "locktest2");

        // when
        registrationSteps.register_new_member("locktest3", "locktest3", "lock@test3.com", "locktest3", "locktest3");

        signInSteps.sign_in_with_credentials_with_failure("locktest3", "incorrectpass");
        signInSteps.sign_in_with_credentials_with_failure("locktest3", "incorrectpass");
        signInSteps.sign_in_with_credentials_with_failure("locktest3", "incorrectpass");

        // then
        signInSteps.sign_in_with_credentials_with_success("locktest3", "locktest3", "locktest3");
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.MEMBER_LOCKOUT, Release.VER_0_7_0})
    public void successful_signin_is_removing_counter_of_previous_failed_signin_attempts() {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        memberLockoutSteps.open_member_lockout_settings();
        memberLockoutSteps.set_lockout_settings(true, "7", "60", "10");
        memberLockoutSteps.save_lockout_settings_form();
        signInSteps.sign_out();

        registrationSteps.register_new_member("locktest4", "locktest4", "lock@test4.com", "locktest4", "locktest4");

        signInSteps.sign_in_with_credentials_with_failure("locktest4", "incorrectpass");
        signInSteps.sign_in_with_credentials_with_failure("locktest4", "incorrectpass");
        signInSteps.sign_in_with_credentials_with_failure("locktest4", "incorrectpass");
        signInSteps.sign_in_with_credentials_with_failure("locktest4", "incorrectpass");
        // then
        signInSteps.sign_in_with_credentials_with_success("locktest4", "locktest4", "locktest4");

        // when
        signInSteps.sign_out();

        signInSteps.sign_in_with_credentials_with_failure("locktest4", "incorrectpass");
        signInSteps.sign_in_with_credentials_with_failure("locktest4", "incorrectpass");
        signInSteps.sign_in_with_credentials_with_failure("locktest4", "incorrectpass");
        signInSteps.sign_in_with_credentials_with_failure("locktest4", "incorrectpass");
        signInSteps.sign_in_with_credentials_with_failure("locktest4", "incorrectpass");
        signInSteps.sign_in_with_credentials_with_failure("locktest4", "incorrectpass");

        // then
        signInSteps.sign_in_with_credentials_with_success("locktest4", "locktest4", "locktest4");

        // when
        signInSteps.sign_out();

        signInSteps.sign_in_with_credentials_with_failure("locktest4", "incorrectpass");
        signInSteps.sign_in_with_credentials_with_failure("locktest4", "incorrectpass");
        signInSteps.sign_in_with_credentials_with_failure("locktest4", "incorrectpass");
        signInSteps.sign_in_with_credentials_with_failure("locktest4", "incorrectpass");
        signInSteps.sign_in_with_credentials_with_failure("locktest4", "incorrectpass");
        signInSteps.sign_in_with_credentials_with_failure("locktest4", "incorrectpass");
        signInSteps.sign_in_with_credentials_with_failure("locktest4", "incorrectpass");

        // then
        signInSteps.sign_in_with_credentials_with_failure_due_to_lockout("locktest4", "locktest4");
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.MEMBER_LOCKOUT, Release.VER_0_7_0})
    public void disabling_of_lockout_is_not_removing_current_existing_locks() {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        memberLockoutSteps.open_member_lockout_settings();
        memberLockoutSteps.set_lockout_settings(true, "4", "60", "10");
        memberLockoutSteps.save_lockout_settings_form();
        signInSteps.sign_out();

        registrationSteps.register_new_member("locktest5", "locktest5", "lock@test5.com", "locktest5", "locktest5");

        signInSteps.sign_in_with_credentials_with_failure("locktest5", "incorrectpass");
        signInSteps.sign_in_with_credentials_with_failure("locktest5", "incorrectpass");
        signInSteps.sign_in_with_credentials_with_failure("locktest5", "incorrectpass");
        signInSteps.sign_in_with_credentials_with_failure("locktest5", "incorrectpass");
        signInSteps.sign_in_with_credentials_with_failure_due_to_lockout("locktest5", "locktest5");

        signInSteps.sign_in_as_administrator_with_success();
        memberLockoutSteps.open_member_lockout_settings();
        memberLockoutSteps.click_for_disabling_lockout_feature();
        memberLockoutSteps.save_lockout_settings_form();
        signInSteps.sign_out();

        // then
        signInSteps.sign_in_with_credentials_with_failure_due_to_lockout("locktest5", "locktest5");
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.SMOKE, Feature.MEMBER_LOCKOUT, Release.VER_0_7_0})
    public void lock_can_be_removed_by_administrator() {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        memberLockoutSteps.open_member_lockout_settings();
        memberLockoutSteps.set_lockout_settings(true, "3", "60", "10");
        memberLockoutSteps.save_lockout_settings_form();
        signInSteps.sign_out();

        registrationSteps.register_new_member("locktest6", "locktest6", "lock@test6.com", "locktest6", "locktest6");

        signInSteps.sign_in_with_credentials_with_failure("locktest6", "incorrectpass");
        signInSteps.sign_in_with_credentials_with_failure("locktest6", "incorrectpass");
        signInSteps.sign_in_with_credentials_with_failure("locktest6", "incorrectpass");
        signInSteps.sign_in_with_credentials_with_failure_due_to_lockout("locktest6", "locktest6");

        signInSteps.sign_in_as_administrator_with_success();
        acpMemberBrowserSteps.open_acp_member_browser_page();
        acpMemberBrowserSteps.type_username_to_search("locktest6");
        acpMemberBrowserSteps.send_member_search_form();
        acpMemberBrowserSteps.select_first_result();
        acpMemberBrowserSteps.click_remove_lock_button();
        signInSteps.sign_out();

        // then
        signInSteps.sign_in_with_credentials_with_success("locktest6", "locktest6", "locktest6");
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.MEMBER_LOCKOUT, Release.VER_0_7_0})
    public void datetime_of_release_lockout_is_visible_in_acp_after_locking() {
        // given
        String tommorow = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        signInSteps.sign_in_as_administrator_with_success();

        // when
        memberLockoutSteps.open_member_lockout_settings();
        memberLockoutSteps.set_lockout_settings(true, "1", "60", "1440");
        memberLockoutSteps.save_lockout_settings_form();
        signInSteps.sign_out();

        registrationSteps.register_new_member("locktest7", "locktest7", "lock@test7.com", "locktest7", "locktest7");

        signInSteps.sign_in_with_credentials_with_failure("locktest7", "incorrectpass");
        signInSteps.sign_in_with_credentials_with_failure_due_to_lockout("locktest7", "locktest7");

        signInSteps.sign_in_as_administrator_with_success();
        acpMemberBrowserSteps.open_acp_member_browser_page();
        acpMemberBrowserSteps.type_username_to_search("locktest7");
        acpMemberBrowserSteps.send_member_search_form();
        acpMemberBrowserSteps.select_first_result();
        acpMemberBrowserSteps.should_contain_lock_expiration_date(tommorow);
    }

}
