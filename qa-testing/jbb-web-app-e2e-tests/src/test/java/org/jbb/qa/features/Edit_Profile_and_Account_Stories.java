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
import org.jbb.qa.steps.EditProfileSteps;
import org.jbb.qa.steps.UserInUcpSteps;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import static org.apache.commons.lang3.StringUtils.isNoneBlank;

@RunWith(SerenityRunner.class)
public class Edit_Profile_and_Account_Stories {
    private static String leonPassword;
    private static String leonDisplayedName;
    private static String leonEmail;
    @Managed(uniqueSession = true)
    WebDriver driver;
    @Steps
    AnonUserRegistrationSteps anonRegistrationUser;
    @Steps
    AnonUserSignInSteps signInUser;
    @Steps
    UserInUcpSteps ucpUser;
    @Steps
    EditProfileSteps editProfileUser;
    @Steps
    EditAccountSteps editAccountUser;

    @Before
    public void setUp() throws Exception {
        // assume
        registerTestUserIfNeeded();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.EDIT_PROFILE, Tags.Release.VER_0_5_0})
    public void update_profile_with_displayed_name_shorter_than_3_characters_is_impossible() throws Exception {
        // when
        signInUser.sign_in_with_credentials_with_success("leon", leonPassword, leonDisplayedName);

        ucpUser.opens_ucp();
        ucpUser.choose_profile_tab();
        ucpUser.choose_edit_profile_option();

        editProfileUser.type_displayed_name("aa");
        editProfileUser.send_edit_profile_form();

        // then
        editProfileUser.should_be_informed_about_incorrect_display_name_length();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.EDIT_PROFILE, Tags.Release.VER_0_5_0})
    public void update_profile_with_displayed_name_longer_than_64_characters_is_impossible() throws Exception {
        // when
        signInUser.sign_in_with_credentials_with_success("leon", leonPassword, leonDisplayedName);

        ucpUser.opens_ucp();
        ucpUser.choose_profile_tab();
        ucpUser.choose_edit_profile_option();

        editProfileUser.type_displayed_name("abcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghij12345");
        editProfileUser.send_edit_profile_form();

        // then
        editProfileUser.should_be_informed_about_incorrect_display_name_length();
    }

    @Test
    @WithTagValuesOf({Tags.Type.SMOKE, Tags.Feature.EDIT_PROFILE, Tags.Release.VER_0_5_0})
    public void update_profile_with_displayed_name_updates_link_text_to_ucp() throws Exception {
        // given
        String newDisplayedName = "new Leon";

        // when
        signInUser.sign_in_with_credentials_with_success("leon", leonPassword, leonDisplayedName);

        ucpUser.opens_ucp();
        ucpUser.choose_profile_tab();
        ucpUser.choose_edit_profile_option();

        editProfileUser.type_displayed_name(newDisplayedName);
        editProfileUser.send_edit_profile_form();

        // then
        editProfileUser.should_be_informed_about_saving_settings();
        editProfileUser.current_displayed_name_should_be_visible_as_link_to_ucp(newDisplayedName);
        leonDisplayedName = newDisplayedName;
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.EDIT_PROFILE, Tags.Release.VER_0_5_0})
    public void update_account_with_incorrect_email_is_impossible() throws Exception {
        // when
        signInUser.sign_in_with_credentials_with_success("leon", leonPassword, leonDisplayedName);

        ucpUser.opens_ucp();
        ucpUser.choose_profile_tab();
        ucpUser.choose_edit_account_settings_option();

        editAccountUser.type_current_password(leonPassword);

        editAccountUser.type_email("no(AT)email.com");
        editAccountUser.send_edit_account_form();

        // then
        editAccountUser.should_be_informed_about_incorrect_email();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.EDIT_PROFILE, Tags.Release.VER_0_5_0})
    public void by_default_update_account_with_password_with_less_than_4_characters_is_impossible() throws Exception {
        // when
        signInUser.sign_in_with_credentials_with_success("leon", leonPassword, leonDisplayedName);

        ucpUser.opens_ucp();
        ucpUser.choose_profile_tab();
        ucpUser.choose_edit_account_settings_option();

        editAccountUser.type_current_password(leonPassword);

        editAccountUser.type_new_password("abc");
        editAccountUser.type_new_password_again("abc");
        editAccountUser.send_edit_account_form();

        // then
        editAccountUser.should_be_informed_about_incorrect_password_length();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.EDIT_PROFILE, Tags.Release.VER_0_5_0})
    public void by_default_update_account_with_password_with_more_than_16_characters_is_impossible() throws Exception {
        // when
        signInUser.sign_in_with_credentials_with_success("leon", leonPassword, leonDisplayedName);

        ucpUser.opens_ucp();
        ucpUser.choose_profile_tab();
        ucpUser.choose_edit_account_settings_option();

        editAccountUser.type_current_password(leonPassword);

        editAccountUser.type_new_password("abcdef1234567890X");
        editAccountUser.type_new_password_again("abcdef1234567890X");
        editAccountUser.send_edit_account_form();

        // then
        editAccountUser.should_be_informed_about_incorrect_password_length();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.EDIT_PROFILE, Tags.Release.VER_0_5_0})
    public void update_account_should_failed_when_user_passed_different_new_passwords() throws Exception {
        // when
        signInUser.sign_in_with_credentials_with_success("leon", leonPassword, leonDisplayedName);

        ucpUser.opens_ucp();
        ucpUser.choose_profile_tab();
        ucpUser.choose_edit_account_settings_option();

        editAccountUser.type_current_password(leonPassword);

        editAccountUser.type_new_password("blebleble");
        editAccountUser.type_new_password_again("blablabla");
        editAccountUser.send_edit_account_form();

        // then
        editAccountUser.should_be_informed_about_not_matching_passwords();
    }

    @Test
    @WithTagValuesOf({Tags.Type.SMOKE, Tags.Feature.EDIT_PROFILE, Tags.Release.VER_0_5_0})
    public void update_account_should_failed_when_user_passed_wrong_current_password() throws Exception {
        // when
        signInUser.sign_in_with_credentials_with_success("leon", leonPassword, leonDisplayedName);

        ucpUser.opens_ucp();
        ucpUser.choose_profile_tab();
        ucpUser.choose_edit_account_settings_option();

        editAccountUser.type_current_password(leonPassword.substring(2));

        editAccountUser.type_email("new@leon.com");
        editAccountUser.type_new_password("newPasswordOk");
        editAccountUser.type_new_password_again("newPasswordOk");
        editAccountUser.send_edit_account_form();

        // then
        editAccountUser.should_be_informed_about_not_matching_current_password();
    }

    @Test
    @WithTagValuesOf({Tags.Type.SMOKE, Tags.Feature.EDIT_PROFILE, Tags.Release.VER_0_5_0})
    public void updating_email_and_password_is_possible() throws Exception {
        // given
        String newEmail = "new@leon.com";
        String newPassword = "newPasswordOk";

        // when
        signInUser.sign_in_with_credentials_with_success("leon", leonPassword, leonDisplayedName);

        ucpUser.opens_ucp();
        ucpUser.choose_profile_tab();
        ucpUser.choose_edit_account_settings_option();

        editAccountUser.type_current_password(leonPassword);

        editAccountUser.type_email(newEmail);
        editAccountUser.type_new_password(newPassword);
        editAccountUser.type_new_password_again(newPassword);
        editAccountUser.send_edit_account_form();

        // then
        editAccountUser.should_be_informed_about_saving_settings();
        leonEmail = newEmail;
        leonPassword = newPassword;

        signInUser.sign_out();

        signInUser.sign_in_with_credentials_with_success("leon", leonPassword, leonDisplayedName);

//        editAccountUser.email_should_be_visible_in_edit_account_form(leonEmail); TODO
    }

    private void registerTestUserIfNeeded() {
        if (!isNoneBlank(leonPassword, leonDisplayedName, leonEmail)) {
            leonPassword = "defaultPass";
            leonDisplayedName = "Leon";
            leonEmail = "default@leon.com";

            anonRegistrationUser.register_new_member("leon", leonDisplayedName, leonEmail,
                    leonPassword, leonPassword);
        }
    }
}
