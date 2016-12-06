/*
 * Copyright (C) 2016 the original author or authors.
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
import org.jbb.qa.steps.AnonUserRegistrationSteps;
import org.jbb.qa.steps.AnonUserSignInSteps;
import org.jbb.qa.steps.EditAccountSteps;
import org.jbb.qa.steps.RegistrationSettingsSteps;
import org.jbb.qa.steps.UserInAcpSteps;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@RunWith(SerenityRunner.class)
public class Registration_Settings_Stories {
    @Managed(uniqueSession = true)
    WebDriver driver;
    @Steps
    AnonUserSignInSteps signInUser;
    @Steps
    UserInAcpSteps acpUser;
    @Steps
    RegistrationSettingsSteps registrationSettingsUser;
    @Steps
    AnonUserRegistrationSteps registrationUser;
    @Steps
    EditAccountSteps editAccountUser;
    private boolean rollbackNeeded = false;

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.REGISTRATION, Tags.Release.VER_0_6_0})
    public void update_minimum_pass_length_to_negative_is_impossible() throws Exception {
        // given
        signInAsAdministrator();

        // when
        acpUser.open_acp();
        acpUser.choose_general_tab();
        acpUser.choose_registration_settings_option();
        registrationSettingsUser.type_minimum_password_length("-1");
        registrationSettingsUser.send_registration_settings_form();

        // then
        registrationSettingsUser.should_be_informed_about_too_small_password_length();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.REGISTRATION, Tags.Release.VER_0_6_0})
    public void update_minimum_pass_length_to_zero_is_impossible() throws Exception {
        // given
        signInAsAdministrator();

        // when
        acpUser.open_acp();
        acpUser.choose_general_tab();
        acpUser.choose_registration_settings_option();
        registrationSettingsUser.type_minimum_password_length("0");
        registrationSettingsUser.send_registration_settings_form();

        // then
        registrationSettingsUser.should_be_informed_about_too_small_password_length();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.REGISTRATION, Tags.Release.VER_0_6_0})
    public void update_minimum_pass_length_to_text_is_impossible() throws Exception {
        // given
        signInAsAdministrator();

        // when
        acpUser.open_acp();
        acpUser.choose_general_tab();
        acpUser.choose_registration_settings_option();
        registrationSettingsUser.type_minimum_password_length("xyz");
        registrationSettingsUser.send_registration_settings_form();

        // then
        registrationSettingsUser.should_be_informed_about_incorrect_minimum_password_length_value();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.REGISTRATION, Tags.Release.VER_0_6_0})
    public void update_minimum_pass_length_to_empty_value_is_impossible() throws Exception {
        // given
        signInAsAdministrator();

        // when
        acpUser.open_acp();
        acpUser.choose_general_tab();
        acpUser.choose_registration_settings_option();
        registrationSettingsUser.type_minimum_password_length("");
        registrationSettingsUser.send_registration_settings_form();

        // then
        registrationSettingsUser.should_be_informed_about_incorrect_minimum_password_length_value();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.REGISTRATION, Tags.Release.VER_0_6_0})
    public void update_maximum_pass_length_to_negative_is_impossible() throws Exception {
        // given
        signInAsAdministrator();

        // when
        acpUser.open_acp();
        acpUser.choose_general_tab();
        acpUser.choose_registration_settings_option();
        registrationSettingsUser.type_maximum_password_length("-1");
        registrationSettingsUser.send_registration_settings_form();

        // then
        registrationSettingsUser.should_be_informed_about_too_small_password_length();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.REGISTRATION, Tags.Release.VER_0_6_0})
    public void update_maximum_pass_length_to_zero_is_impossible() throws Exception {
        // given
        signInAsAdministrator();

        // when
        acpUser.open_acp();
        acpUser.choose_general_tab();
        acpUser.choose_registration_settings_option();
        registrationSettingsUser.type_maximum_password_length("0");
        registrationSettingsUser.send_registration_settings_form();

        // then
        registrationSettingsUser.should_be_informed_about_too_small_password_length();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.REGISTRATION, Tags.Release.VER_0_6_0})
    public void update_maximum_pass_length_to_text_is_impossible() throws Exception {
        // given
        signInAsAdministrator();

        // when
        acpUser.open_acp();
        acpUser.choose_general_tab();
        acpUser.choose_registration_settings_option();
        registrationSettingsUser.type_maximum_password_length("xyz");
        registrationSettingsUser.send_registration_settings_form();

        // then
        registrationSettingsUser.should_be_informed_about_incorrect_maximum_password_length_value();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.REGISTRATION, Tags.Release.VER_0_6_0})
    public void update_maximum_pass_length_to_empty_value_is_impossible() throws Exception {
        // given
        signInAsAdministrator();

        // when
        acpUser.open_acp();
        acpUser.choose_general_tab();
        acpUser.choose_registration_settings_option();
        registrationSettingsUser.type_maximum_password_length("");
        registrationSettingsUser.send_registration_settings_form();

        // then
        registrationSettingsUser.should_be_informed_about_incorrect_maximum_password_length_value();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.REGISTRATION, Tags.Release.VER_0_6_0})
    public void update_minimum_pass_length_greater_than_maximum_pass_length_impossible() throws Exception {
        // given
        signInAsAdministrator();

        // when
        acpUser.open_acp();
        acpUser.choose_general_tab();
        acpUser.choose_registration_settings_option();
        registrationSettingsUser.type_minimum_password_length("5");
        registrationSettingsUser.type_maximum_password_length("2");
        registrationSettingsUser.send_registration_settings_form();

        // then
        registrationSettingsUser.should_be_informed_that_minimum_cannot_be_greater_than_maximum_pass_length();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.REGISTRATION, Tags.Release.VER_0_6_0})
    public void changing_minimum_and_maximum_password_length_should_work() throws Exception {
        //mark rollback
        rollbackNeeded = true;

        // given
        signInAsAdministrator();
        registrationSettingsUser.set_new_password_lengths_with_success("3", "7");
        signInUser.sign_out();
        registrationUser.register_new_member_and_should_fail_due_to_passwordLength(
                "passX", "PassX", "x@pass.com", "ab", "ab"
        );
        registrationUser.register_new_member(
                "passX", "PassX", "x@pass.com", "aba", "aba"
        );
        registrationUser.register_new_member_and_should_fail_due_to_passwordLength(
                "passY", "PassY", "y@pass.com", "abababab", "abababab"
        );
        registrationUser.register_new_member(
                "passY", "PassY", "y@pass.com", "abababa", "abababa"
        );
        signInAsAdministrator();
        registrationSettingsUser.set_new_password_lengths_with_success("2", "8");
        signInUser.sign_out();
        registrationUser.register_new_member(
                "passZ", "PassZ", "z@pass.com", "ab", "ab"
        );
        registrationUser.register_new_member(
                "passT", "PassT", "t@pass.com", "abababab", "abababab"
        );
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.EDIT_PROFILE, Tags.Release.VER_0_6_0})
    public void minimum_and_maximum_password_length_policy_should_work_when_member_is_trying_to_change_password_in_ucp() throws Exception {
        //mark rollback
        rollbackNeeded = true;

        // given
        signInAsAdministrator();
        registrationSettingsUser.set_new_password_lengths_with_success("3", "7");
        signInUser.sign_out();
        registrationUser.register_new_member(
                "passXX", "PassXX", "xx@pass.com", "aba", "aba"
        );
        signInUser.sign_in_with_credentials_with_success("passXX", "aba", "PassXX");
        editAccountUser.change_password_with_fail_due_to_invalid_length("aba", "aa");
        editAccountUser.change_password_with_fail_due_to_invalid_length("aba", "aaaaaaaa");
        signInUser.sign_out();
        signInAsAdministrator();
        registrationSettingsUser.set_new_password_lengths_with_success("2", "8");
        signInUser.sign_out();
        signInUser.sign_in_with_credentials_with_success("passXX", "aba", "PassXX");
        editAccountUser.change_password_with_success("aba", "aa");
        editAccountUser.change_password_with_success("aa", "aaaaaaaa");
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.REGISTRATION, Tags.Release.VER_0_6_0})
    public void when_email_duplication_is_allowed_then_many_users_can_use_the_same_email() throws Exception {
        //mark rollback
        rollbackNeeded = true;

        // given
        String pass = "pass1";

        // when
        signInAsAdministrator();
        registrationSettingsUser.set_allow_for_email_duplication();
        signInUser.sign_out();

        // then
        registrationUser.register_new_member(
                "mailA", "MailA", "foo@acme.com", pass, pass
        );
        registrationUser.register_new_member(
                "mailB", "MailB", "foo@acme.com", pass, pass
        );
        registrationUser.register_new_member(
                "mailC", "MailC", "bar@acme.com", pass, pass
        );
        signInUser.sign_in_with_credentials_with_success("mailC", pass, "MailC");
        editAccountUser.change_email_with_success(pass, "foo@acme.com");

        // rollback workaround IDEA
        signInUser.sign_out();
        signInAsAdministrator();
        registrationSettingsUser.set_disallow_for_email_duplication();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.EDIT_PROFILE, Tags.Release.VER_0_6_0})
    public void when_email_duplication_is_disallowed_then_other_users_cannot_use_it_anymore() throws Exception {
        //mark rollback
        rollbackNeeded = true;

        // given
        String pass = "pass1";

        // when
        signInAsAdministrator();
        registrationSettingsUser.set_allow_for_email_duplication();
        signInUser.sign_out();

        // then
        registrationUser.register_new_member(
                "mailX", "MailX", "foo@acme.eu", pass, pass
        );
        registrationUser.register_new_member(
                "mailY", "MailY", "foo@acme.eu", pass, pass
        );
        signInAsAdministrator();
        registrationSettingsUser.set_disallow_for_email_duplication();
        signInUser.sign_out();
        registrationUser.register_new_member_and_should_fail_due_to_busy_email(
                "mailZ", "MailZ", "foo@acme.eu", pass, pass
        );
        registrationUser.register_new_member(
                "mailZ", "MailZ", "bar@acme.eu", pass, pass
        );
        signInUser.sign_in_with_credentials_with_success("mailZ", pass, "MailZ");
        editAccountUser.change_email_with_fail_due_to_used_by_another_member(pass, "foo@acme.eu");
        signInUser.sign_out();
        signInUser.sign_in_with_credentials_with_success("mailY", pass, "MailY");
        editAccountUser.change_password_with_success(pass, pass + "@");
        editAccountUser.change_email_with_success(pass + "@", "win@acme.eu");
        editAccountUser.change_email_with_fail_due_to_used_by_another_member(pass + "@", "foo@acme.eu");
    }

    private void signInAsAdministrator() {
        signInUser.sign_in_with_credentials_with_success("administrator", "administrator", "Administrator");
    }

    @After
    public void tearDown() throws Exception {
        if (rollbackNeeded) {
            signInAsAdministrator();
            registrationSettingsUser.set_new_password_lengths_with_success("4", "16");
            registrationSettingsUser.set_disallow_for_email_duplication();
            signInUser.sign_out();
        }
    }
}
