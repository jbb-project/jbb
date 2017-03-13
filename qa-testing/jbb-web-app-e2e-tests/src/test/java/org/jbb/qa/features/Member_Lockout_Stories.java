/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.qa.features;

import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.WithTagValuesOf;

import org.jbb.qa.Tags;
import org.jbb.qa.steps.AcpMemberLockoutSteps;
import org.jbb.qa.steps.AnonUserSignInSteps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@RunWith(SerenityRunner.class)
public class Member_Lockout_Stories {
    @Managed(uniqueSession = true)
    WebDriver driver;

    @Steps
    AnonUserSignInSteps signInSteps;

    @Steps
    AcpMemberLockoutSteps memberLockoutSteps;

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.MEMBER_LOCKOUT, Tags.Release.VER_0_7_0})
    public void update_failed_attempts_threshold_to_negative_value_is_impossible() throws Exception {
        // given
        signInAsAdministrator();

        // when
        memberLockoutSteps.open_member_lockout_settings();
        memberLockoutSteps.type_failed_attempts_threshold("-1");
        memberLockoutSteps.save_lockout_settings_form();

        // then
        memberLockoutSteps.should_be_informed_that_value_must_be_equal_or_greater_than_one();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.MEMBER_LOCKOUT, Tags.Release.VER_0_7_0})
    public void update_failed_attempts_threshold_to_zero_value_is_impossible() throws Exception {
        // given
        signInAsAdministrator();

        // when
        memberLockoutSteps.open_member_lockout_settings();
        memberLockoutSteps.type_failed_attempts_threshold("0");
        memberLockoutSteps.save_lockout_settings_form();

        // then
        memberLockoutSteps.should_be_informed_that_value_must_be_equal_or_greater_than_one();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.MEMBER_LOCKOUT, Tags.Release.VER_0_7_0})
    public void update_failed_attempts_threshold_to_text_value_is_impossible() throws Exception {
        // given
        signInAsAdministrator();

        // when
        memberLockoutSteps.open_member_lockout_settings();
        memberLockoutSteps.type_failed_attempts_threshold("aaaaa");
        memberLockoutSteps.save_lockout_settings_form();

        // then
        memberLockoutSteps.should_be_informed_that_value_is_invalid();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.MEMBER_LOCKOUT, Tags.Release.VER_0_7_0})
    public void update_failed_attempts_threshold_to_empty_value_is_impossible() throws Exception {
        // given
        signInAsAdministrator();

        // when
        memberLockoutSteps.open_member_lockout_settings();
        memberLockoutSteps.type_failed_attempts_threshold("");
        memberLockoutSteps.save_lockout_settings_form();

        // then
        memberLockoutSteps.should_be_informed_that_value_is_invalid();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.MEMBER_LOCKOUT, Tags.Release.VER_0_7_0})
    public void update_failed_attempts_threshold_to_blank_value_is_impossible() throws Exception {
        // given
        signInAsAdministrator();

        // when
        memberLockoutSteps.open_member_lockout_settings();
        memberLockoutSteps.type_failed_attempts_threshold("          ");
        memberLockoutSteps.save_lockout_settings_form();

        // then
        memberLockoutSteps.should_be_informed_that_value_is_invalid();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.MEMBER_LOCKOUT, Tags.Release.VER_0_7_0})
    public void update_failed_attempts_expiration_to_negative_value_is_impossible() throws Exception {
        // given
        signInAsAdministrator();

        // when
        memberLockoutSteps.open_member_lockout_settings();
        memberLockoutSteps.type_failed_attempts_expiration("-1");
        memberLockoutSteps.save_lockout_settings_form();

        // then
        memberLockoutSteps.should_be_informed_that_value_must_be_equal_or_greater_than_one();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.MEMBER_LOCKOUT, Tags.Release.VER_0_7_0})
    public void update_failed_attempts_expiration_to_zero_value_is_impossible() throws Exception {
        // given
        signInAsAdministrator();

        // when
        memberLockoutSteps.open_member_lockout_settings();
        memberLockoutSteps.type_failed_attempts_expiration("0");
        memberLockoutSteps.save_lockout_settings_form();

        // then
        memberLockoutSteps.should_be_informed_that_value_must_be_equal_or_greater_than_one();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.MEMBER_LOCKOUT, Tags.Release.VER_0_7_0})
    public void update_failed_attempts_expiration_to_text_value_is_impossible() throws Exception {
        // given
        signInAsAdministrator();

        // when
        memberLockoutSteps.open_member_lockout_settings();
        memberLockoutSteps.type_failed_attempts_expiration("aaaaa");
        memberLockoutSteps.save_lockout_settings_form();

        // then
        memberLockoutSteps.should_be_informed_that_value_is_invalid();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.MEMBER_LOCKOUT, Tags.Release.VER_0_7_0})
    public void update_failed_attempts_expiration_to_empty_value_is_impossible() throws Exception {
        // given
        signInAsAdministrator();

        // when
        memberLockoutSteps.open_member_lockout_settings();
        memberLockoutSteps.type_failed_attempts_expiration("");
        memberLockoutSteps.save_lockout_settings_form();

        // then
        memberLockoutSteps.should_be_informed_that_value_is_invalid();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.MEMBER_LOCKOUT, Tags.Release.VER_0_7_0})
    public void update_failed_attempts_expiration_to_blank_value_is_impossible() throws Exception {
        // given
        signInAsAdministrator();

        // when
        memberLockoutSteps.open_member_lockout_settings();
        memberLockoutSteps.type_failed_attempts_expiration("          ");
        memberLockoutSteps.save_lockout_settings_form();

        // then
        memberLockoutSteps.should_be_informed_that_value_is_invalid();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.MEMBER_LOCKOUT, Tags.Release.VER_0_7_0})
    public void update_lockout_duration_to_negative_value_is_impossible() throws Exception {
        // given
        signInAsAdministrator();

        // when
        memberLockoutSteps.open_member_lockout_settings();
        memberLockoutSteps.type_lockout_duration("-1");
        memberLockoutSteps.save_lockout_settings_form();

        // then
        memberLockoutSteps.should_be_informed_that_value_must_be_equal_or_greater_than_one();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.MEMBER_LOCKOUT, Tags.Release.VER_0_7_0})
    public void update_lockout_duration_to_zero_value_is_impossible() throws Exception {
        // given
        signInAsAdministrator();

        // when
        memberLockoutSteps.open_member_lockout_settings();
        memberLockoutSteps.type_lockout_duration("0");
        memberLockoutSteps.save_lockout_settings_form();

        // then
        memberLockoutSteps.should_be_informed_that_value_must_be_equal_or_greater_than_one();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.MEMBER_LOCKOUT, Tags.Release.VER_0_7_0})
    public void update_lockout_duration_to_text_value_is_impossible() throws Exception {
        // given
        signInAsAdministrator();

        // when
        memberLockoutSteps.open_member_lockout_settings();
        memberLockoutSteps.type_lockout_duration("aaaaa");
        memberLockoutSteps.save_lockout_settings_form();

        // then
        memberLockoutSteps.should_be_informed_that_value_is_invalid();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.MEMBER_LOCKOUT, Tags.Release.VER_0_7_0})
    public void update_lockout_duration_to_empty_value_is_impossible() throws Exception {
        // given
        signInAsAdministrator();

        // when
        memberLockoutSteps.open_member_lockout_settings();
        memberLockoutSteps.type_lockout_duration("");
        memberLockoutSteps.save_lockout_settings_form();

        // then
        memberLockoutSteps.should_be_informed_that_value_is_invalid();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.MEMBER_LOCKOUT, Tags.Release.VER_0_7_0})
    public void update_lockout_duration_to_blank_value_is_impossible() throws Exception {
        // given
        signInAsAdministrator();

        // when
        memberLockoutSteps.open_member_lockout_settings();
        memberLockoutSteps.type_lockout_duration("          ");
        memberLockoutSteps.save_lockout_settings_form();

        // then
        memberLockoutSteps.should_be_informed_that_value_is_invalid();
    }

    private void signInAsAdministrator() {
        signInSteps.sign_in_with_credentials_with_success("administrator", "administrator", "Administrator");
    }
}
