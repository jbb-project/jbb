/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.editprofile;

import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.WithTagValuesOf;

import org.jbb.e2e.serenity.JbbBaseSerenityStories;
import org.jbb.e2e.serenity.Tags;
import org.jbb.e2e.serenity.commons.UcpSteps;
import org.jbb.e2e.serenity.registration.RegistrationSteps;
import org.jbb.e2e.serenity.signin.SignInSteps;
import org.junit.Before;
import org.junit.Test;

import static org.apache.commons.lang3.StringUtils.isNoneBlank;

public class EditProfileAndAccountStories extends JbbBaseSerenityStories {
    private static String leonPassword;
    private static String leonDisplayedName;
    private static String leonEmail;

    @Steps
    RegistrationSteps registrationSteps;
    @Steps
    SignInSteps signInSteps;
    @Steps
    UcpSteps ucpSteps;
    @Steps
    EditProfileSteps editProfileSteps;
    @Steps
    EditAccountSteps editAccountSteps;

    @Before
    public void setUp() throws Exception {
        // assume
        register_test_user_if_needed();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.EDIT_PROFILE, Tags.Release.VER_0_5_0})
    public void update_profile_with_displayed_name_shorter_than_3_characters_is_impossible() throws Exception {
        // when
        signInSteps.sign_in_with_credentials_with_success("leon", leonPassword, leonDisplayedName);

        ucpSteps.open_ucp();
        ucpSteps.choose_profile_tab();
        ucpSteps.choose_edit_profile_option();

        editProfileSteps.type_displayed_name("aa");
        editProfileSteps.send_edit_profile_form();

        // then
        editProfileSteps.should_be_informed_about_incorrect_display_name_length();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.EDIT_PROFILE, Tags.Release.VER_0_5_0})
    public void update_profile_with_displayed_name_longer_than_64_characters_is_impossible() throws Exception {
        // when
        signInSteps.sign_in_with_credentials_with_success("leon", leonPassword, leonDisplayedName);

        ucpSteps.open_ucp();
        ucpSteps.choose_profile_tab();
        ucpSteps.choose_edit_profile_option();

        editProfileSteps.type_displayed_name("abcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghij12345");
        editProfileSteps.send_edit_profile_form();

        // then
        editProfileSteps.should_be_informed_about_incorrect_display_name_length();
    }

    @Test
    @WithTagValuesOf({Tags.Type.SMOKE, Tags.Feature.EDIT_PROFILE, Tags.Release.VER_0_5_0})
    public void update_profile_with_displayed_name_updates_link_text_to_ucp() throws Exception {
        // given
        String newDisplayedName = "new Leon";

        // when
        signInSteps.sign_in_with_credentials_with_success("leon", leonPassword, leonDisplayedName);

        ucpSteps.open_ucp();
        ucpSteps.choose_profile_tab();
        ucpSteps.choose_edit_profile_option();

        editProfileSteps.type_displayed_name(newDisplayedName);
        editProfileSteps.send_edit_profile_form();

        // then
        editProfileSteps.should_be_informed_about_saving_settings();
        editProfileSteps.current_displayed_name_should_be_visible_as_link_to_ucp(newDisplayedName);
        leonDisplayedName = newDisplayedName;
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.EDIT_PROFILE, Tags.Release.VER_0_5_0})
    public void update_account_with_incorrect_email_is_impossible() throws Exception {
        // when
        signInSteps.sign_in_with_credentials_with_success("leon", leonPassword, leonDisplayedName);

        ucpSteps.open_ucp();
        ucpSteps.choose_profile_tab();
        ucpSteps.choose_edit_account_settings_option();

        editAccountSteps.type_current_password(leonPassword);

        editAccountSteps.type_email("no(AT)email.com");
        editAccountSteps.send_edit_account_form();

        // then
        editAccountSteps.should_be_informed_about_incorrect_email();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.EDIT_PROFILE, Tags.Release.VER_0_5_0})
    public void by_default_update_account_with_password_with_less_than_4_characters_is_impossible() throws Exception {
        // when
        signInSteps.sign_in_with_credentials_with_success("leon", leonPassword, leonDisplayedName);

        ucpSteps.open_ucp();
        ucpSteps.choose_profile_tab();
        ucpSteps.choose_edit_account_settings_option();

        editAccountSteps.type_current_password(leonPassword);

        editAccountSteps.type_new_password("abc");
        editAccountSteps.type_new_password_again("abc");
        editAccountSteps.send_edit_account_form();

        // then
        editAccountSteps.should_be_informed_about_incorrect_password_length();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.EDIT_PROFILE, Tags.Release.VER_0_5_0})
    public void by_default_update_account_with_password_with_more_than_16_characters_is_impossible() throws Exception {
        // when
        signInSteps.sign_in_with_credentials_with_success("leon", leonPassword, leonDisplayedName);

        ucpSteps.open_ucp();
        ucpSteps.choose_profile_tab();
        ucpSteps.choose_edit_account_settings_option();

        editAccountSteps.type_current_password(leonPassword);

        editAccountSteps.type_new_password("abcdef1234567890X");
        editAccountSteps.type_new_password_again("abcdef1234567890X");
        editAccountSteps.send_edit_account_form();

        // then
        editAccountSteps.should_be_informed_about_incorrect_password_length();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.EDIT_PROFILE, Tags.Release.VER_0_5_0})
    public void update_account_should_failed_when_user_passed_different_new_passwords() throws Exception {
        // when
        signInSteps.sign_in_with_credentials_with_success("leon", leonPassword, leonDisplayedName);

        ucpSteps.open_ucp();
        ucpSteps.choose_profile_tab();
        ucpSteps.choose_edit_account_settings_option();

        editAccountSteps.type_current_password(leonPassword);

        editAccountSteps.type_new_password("blebleble");
        editAccountSteps.type_new_password_again("blablabla");
        editAccountSteps.send_edit_account_form();

        // then
        editAccountSteps.should_be_informed_about_not_matching_passwords();
    }

    @Test
    @WithTagValuesOf({Tags.Type.SMOKE, Tags.Feature.EDIT_PROFILE, Tags.Release.VER_0_5_0})
    public void update_account_should_failed_when_user_passed_wrong_current_password() throws Exception {
        // when
        signInSteps.sign_in_with_credentials_with_success("leon", leonPassword, leonDisplayedName);

        ucpSteps.open_ucp();
        ucpSteps.choose_profile_tab();
        ucpSteps.choose_edit_account_settings_option();

        editAccountSteps.type_current_password(leonPassword.substring(2));

        editAccountSteps.type_email("new@leon.com");
        editAccountSteps.type_new_password("newPasswordOk");
        editAccountSteps.type_new_password_again("newPasswordOk");
        editAccountSteps.send_edit_account_form();

        // then
        editAccountSteps.should_be_informed_about_not_matching_current_password();
    }

    @Test
    @WithTagValuesOf({Tags.Type.SMOKE, Tags.Feature.EDIT_PROFILE, Tags.Release.VER_0_5_0})
    public void updating_email_and_password_is_possible() throws Exception {
        // given
        String newEmail = "new@leon.com";
        String newPassword = "newPasswordOk";

        // when
        signInSteps.sign_in_with_credentials_with_success("leon", leonPassword, leonDisplayedName);

        ucpSteps.open_ucp();
        ucpSteps.choose_profile_tab();
        ucpSteps.choose_edit_account_settings_option();

        editAccountSteps.type_current_password(leonPassword);

        editAccountSteps.type_email(newEmail);
        editAccountSteps.type_new_password(newPassword);
        editAccountSteps.type_new_password_again(newPassword);
        editAccountSteps.send_edit_account_form();

        // then
        editAccountSteps.should_be_informed_about_saving_settings();
        leonEmail = newEmail;
        leonPassword = newPassword;

        signInSteps.sign_out();

        signInSteps.sign_in_with_credentials_with_success("leon", leonPassword, leonDisplayedName);

        editAccountSteps.email_should_be_visible_in_edit_account_form(leonEmail);
    }

    private void register_test_user_if_needed() {
        if (!isNoneBlank(leonPassword, leonDisplayedName, leonEmail)) {
            leonPassword = "defaultPass";
            leonDisplayedName = "Leon";
            leonEmail = "default@leon.com";

            registrationSteps.register_new_member("leon", leonDisplayedName, leonEmail,
                    leonPassword, leonPassword);
        }
    }
}
