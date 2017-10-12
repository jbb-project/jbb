/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.web.registrationsettings;

import static org.jbb.e2e.serenity.Tags.Feature;
import static org.jbb.e2e.serenity.Tags.Interface;
import static org.jbb.e2e.serenity.Tags.Release;
import static org.jbb.e2e.serenity.Tags.Type;

import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.WithTagValuesOf;
import org.jbb.e2e.serenity.web.EndToEndWebStories;
import org.jbb.e2e.serenity.web.commons.AcpSteps;
import org.jbb.e2e.serenity.web.editprofile.EditAccountSteps;
import org.jbb.e2e.serenity.web.registration.RegistrationSteps;
import org.jbb.e2e.serenity.web.signin.SignInSteps;
import org.junit.Test;

public class RegistrationSettingsStories extends EndToEndWebStories {

    @Steps
    SignInSteps signInSteps;
    @Steps
    AcpSteps acpSteps;
    @Steps
    RegistrationSettingsSteps registrationSettingsSteps;
    @Steps
    RegistrationSteps registrationSteps;
    @Steps
    EditAccountSteps editAccountSteps;

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.REGISTRATION, Release.VER_0_6_0})
    public void update_minimum_pass_length_to_negative_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        acpSteps.open_acp();
        acpSteps.choose_general_tab();
        acpSteps.choose_registration_settings_option();
        registrationSettingsSteps.type_minimum_password_length("-1");
        registrationSettingsSteps.send_registration_settings_form();

        // then
        registrationSettingsSteps.should_be_informed_about_too_small_password_length();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.REGISTRATION, Release.VER_0_6_0})
    public void update_minimum_pass_length_to_zero_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        acpSteps.open_acp();
        acpSteps.choose_general_tab();
        acpSteps.choose_registration_settings_option();
        registrationSettingsSteps.type_minimum_password_length("0");
        registrationSettingsSteps.send_registration_settings_form();

        // then
        registrationSettingsSteps.should_be_informed_about_too_small_password_length();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.REGISTRATION, Release.VER_0_6_0})
    public void update_minimum_pass_length_to_text_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        acpSteps.open_acp();
        acpSteps.choose_general_tab();
        acpSteps.choose_registration_settings_option();
        registrationSettingsSteps.type_minimum_password_length("xyz");
        registrationSettingsSteps.send_registration_settings_form();

        // then
        registrationSettingsSteps.should_be_informed_about_incorrect_minimum_password_length_value();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.REGISTRATION, Release.VER_0_6_0})
    public void update_minimum_pass_length_to_empty_value_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        acpSteps.open_acp();
        acpSteps.choose_general_tab();
        acpSteps.choose_registration_settings_option();
        registrationSettingsSteps.type_minimum_password_length("");
        registrationSettingsSteps.send_registration_settings_form();

        // then
        registrationSettingsSteps.should_be_informed_about_incorrect_minimum_password_length_value();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.REGISTRATION, Release.VER_0_6_0})
    public void update_maximum_pass_length_to_negative_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        acpSteps.open_acp();
        acpSteps.choose_general_tab();
        acpSteps.choose_registration_settings_option();
        registrationSettingsSteps.type_maximum_password_length("-1");
        registrationSettingsSteps.send_registration_settings_form();

        // then
        registrationSettingsSteps.should_be_informed_about_too_small_password_length();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.REGISTRATION, Release.VER_0_6_0})
    public void update_maximum_pass_length_to_zero_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        acpSteps.open_acp();
        acpSteps.choose_general_tab();
        acpSteps.choose_registration_settings_option();
        registrationSettingsSteps.type_maximum_password_length("0");
        registrationSettingsSteps.send_registration_settings_form();

        // then
        registrationSettingsSteps.should_be_informed_about_too_small_password_length();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.REGISTRATION, Release.VER_0_6_0})
    public void update_maximum_pass_length_to_text_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        acpSteps.open_acp();
        acpSteps.choose_general_tab();
        acpSteps.choose_registration_settings_option();
        registrationSettingsSteps.type_maximum_password_length("xyz");
        registrationSettingsSteps.send_registration_settings_form();

        // then
        registrationSettingsSteps.should_be_informed_about_incorrect_maximum_password_length_value();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.REGISTRATION, Release.VER_0_6_0})
    public void update_maximum_pass_length_to_empty_value_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        acpSteps.open_acp();
        acpSteps.choose_general_tab();
        acpSteps.choose_registration_settings_option();
        registrationSettingsSteps.type_maximum_password_length("");
        registrationSettingsSteps.send_registration_settings_form();

        // then
        registrationSettingsSteps.should_be_informed_about_incorrect_maximum_password_length_value();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.REGISTRATION, Release.VER_0_6_0})
    public void update_minimum_pass_length_greater_than_maximum_pass_length_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        acpSteps.open_acp();
        acpSteps.choose_general_tab();
        acpSteps.choose_registration_settings_option();
        registrationSettingsSteps.type_minimum_password_length("5");
        registrationSettingsSteps.type_maximum_password_length("2");
        registrationSettingsSteps.send_registration_settings_form();

        // then
        registrationSettingsSteps.should_be_informed_that_minimum_cannot_be_greater_than_maximum_pass_length();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.REGISTRATION, Release.VER_0_6_0})
    public void changing_minimum_and_maximum_password_length_should_work() throws Exception {
        make_rollback_after_test_case(restore_default_registration_settings());

        // given
        signInSteps.sign_in_as_administrator_with_success();
        registrationSettingsSteps.set_new_password_lengths_with_success("3", "7");
        signInSteps.sign_out();
        registrationSteps.register_new_member_and_should_fail_due_to_passwordLength(
                "passX", "PassX", "x@pass.com", "ab", "ab"
        );
        registrationSteps.register_new_member(
                "passX", "PassX", "x@pass.com", "aba", "aba"
        );
        registrationSteps.register_new_member_and_should_fail_due_to_passwordLength(
                "passY", "PassY", "y@pass.com", "abababab", "abababab"
        );
        registrationSteps.register_new_member(
                "passY", "PassY", "y@pass.com", "abababa", "abababa"
        );
        signInSteps.sign_in_as_administrator_with_success();
        registrationSettingsSteps.set_new_password_lengths_with_success("2", "8");
        signInSteps.sign_out();
        registrationSteps.register_new_member(
                "passZ", "PassZ", "z@pass.com", "ab", "ab"
        );
        registrationSteps.register_new_member(
                "passT", "PassT", "t@pass.com", "abababab", "abababab"
        );
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.EDIT_PROFILE, Release.VER_0_6_0})
    public void minimum_and_maximum_password_length_policy_should_work_when_member_is_trying_to_change_password_in_ucp() throws Exception {
        make_rollback_after_test_case(restore_default_registration_settings());

        // given
        signInSteps.sign_in_as_administrator_with_success();
        registrationSettingsSteps.set_new_password_lengths_with_success("3", "7");
        signInSteps.sign_out();
        registrationSteps.register_new_member(
                "passXX", "PassXX", "xx@pass.com", "aba", "aba"
        );
        signInSteps.sign_in_with_credentials_with_success("passXX", "aba", "PassXX");
        editAccountSteps.change_password_with_fail_due_to_invalid_length("aba", "aa");
        editAccountSteps.change_password_with_fail_due_to_invalid_length("aba", "aaaaaaaa");
        signInSteps.sign_out();

        signInSteps.sign_in_as_administrator_with_success();
        registrationSettingsSteps.set_new_password_lengths_with_success("2", "8");
        signInSteps.sign_out();
        signInSteps.sign_in_with_credentials_with_success("passXX", "aba", "PassXX");
        editAccountSteps.change_password_with_success("aba", "aa");
        editAccountSteps.change_password_with_success("aa", "aaaaaaaa");

        // for rollback purposes
        signInSteps.sign_out();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.REGISTRATION, Release.VER_0_6_0})
    public void when_email_duplication_is_allowed_then_many_users_can_use_the_same_email() throws Exception {
        make_rollback_after_test_case(restore_default_registration_settings());

        // given
        String pass = "pass1";

        // when
        signInSteps.sign_in_as_administrator_with_success();
        registrationSettingsSteps.set_allow_for_email_duplication();
        signInSteps.sign_out();

        // then
        registrationSteps.register_new_member(
                "mailA", "MailA", "foo@acme.com", pass, pass
        );
        registrationSteps.register_new_member(
                "mailB", "MailB", "foo@acme.com", pass, pass
        );
        registrationSteps.register_new_member(
                "mailC", "MailC", "bar@acme.com", pass, pass
        );
        signInSteps.sign_in_with_credentials_with_success("mailC", pass, "MailC");
        editAccountSteps.change_email_with_success(pass, "foo@acme.com");

        // for rollback purposes
        signInSteps.sign_out();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.EDIT_PROFILE, Release.VER_0_6_0})
    public void when_email_duplication_is_disallowed_then_other_users_cannot_use_it_anymore() throws Exception {
        make_rollback_after_test_case(restore_default_registration_settings());

        // given
        String pass = "pass1";

        // when
        signInSteps.sign_in_as_administrator_with_success();
        registrationSettingsSteps.set_allow_for_email_duplication();
        signInSteps.sign_out();

        // then
        registrationSteps.register_new_member(
                "mailX", "MailX", "foo@acme.eu", pass, pass
        );
        registrationSteps.register_new_member(
                "mailY", "MailY", "foo@acme.eu", pass, pass
        );
        signInSteps.sign_in_as_administrator_with_success();
        registrationSettingsSteps.set_disallow_for_email_duplication();
        signInSteps.sign_out();
        registrationSteps.register_new_member_and_should_fail_due_to_busy_email(
                "mailZ", "MailZ", "foo@acme.eu", pass, pass
        );
        registrationSteps.register_new_member(
                "mailZ", "MailZ", "bar@acme.eu", pass, pass
        );
        signInSteps.sign_in_with_credentials_with_success("mailZ", pass, "MailZ");
        editAccountSteps.change_email_with_fail_due_to_used_by_another_member(pass, "foo@acme.eu");
        signInSteps.sign_out();
        signInSteps.sign_in_with_credentials_with_success("mailY", pass, "MailY");
        editAccountSteps.change_password_with_success(pass, pass + "@");
        editAccountSteps.change_email_with_success(pass + "@", "win@acme.eu");
        editAccountSteps.change_email_with_fail_due_to_used_by_another_member(pass + "@", "foo@acme.eu");

        // for rollback purposes
        signInSteps.sign_out();
    }

    RollbackAction restore_default_registration_settings() {
        return () -> {
            signInSteps.sign_in_as_administrator_with_success();
            registrationSettingsSteps.set_new_password_lengths_with_success("4", "16");
            registrationSettingsSteps.set_disallow_for_email_duplication();
        };
    }
}
