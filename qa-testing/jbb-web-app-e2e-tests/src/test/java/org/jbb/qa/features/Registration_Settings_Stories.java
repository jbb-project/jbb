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
import org.jbb.qa.steps.AnonUserSignInSteps;
import org.jbb.qa.steps.RegistrationSettingsSteps;
import org.jbb.qa.steps.UserInAcpSteps;
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


    private void signInAsAdministrator() {
        signInUser.sign_in_with_credentials_with_success("administrator", "administrator", "Administrator");
    }
}
