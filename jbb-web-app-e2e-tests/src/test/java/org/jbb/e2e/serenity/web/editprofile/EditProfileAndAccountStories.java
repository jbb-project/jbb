/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.web.editprofile;

import static org.apache.commons.lang3.StringUtils.isNoneBlank;
import static org.jbb.e2e.serenity.Tags.Feature;
import static org.jbb.e2e.serenity.Tags.Interface;
import static org.jbb.e2e.serenity.Tags.Release;
import static org.jbb.e2e.serenity.Tags.Type;

import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.WithTagValuesOf;
import org.jbb.e2e.serenity.web.EndToEndWebStories;
import org.jbb.e2e.serenity.web.commons.UcpSteps;
import org.jbb.e2e.serenity.web.membermanagement.AcpMemberBrowserSteps;
import org.jbb.e2e.serenity.web.permissions.AcpMemberPermissionsSteps;
import org.jbb.e2e.serenity.web.permissions.PermissionValue;
import org.jbb.e2e.serenity.web.registration.RegistrationSteps;
import org.jbb.e2e.serenity.web.signin.SignInSteps;
import org.junit.Before;
import org.junit.Test;

public class EditProfileAndAccountStories extends EndToEndWebStories {
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
    @Steps
    AcpMemberPermissionsSteps acpMemberPermissionsSteps;
    @Steps
    AcpMemberBrowserSteps acpMemberBrowserSteps;

    @Before
    public void setUp() throws Exception {
        // assume
        register_test_user_if_needed();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.PROFILE, Release.VER_0_5_0})
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
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.PROFILE, Release.VER_0_5_0})
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
    @WithTagValuesOf({Interface.WEB, Type.SMOKE, Feature.PROFILE, Release.VER_0_5_0})
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
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.ACCOUNTS, Release.VER_0_5_0})
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
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.ACCOUNTS, Release.VER_0_5_0})
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
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.ACCOUNTS, Release.VER_0_5_0})
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
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.ACCOUNTS, Release.VER_0_5_0})
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
    @WithTagValuesOf({Interface.WEB, Type.SMOKE, Feature.ACCOUNTS, Release.VER_0_5_0})
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
    @WithTagValuesOf({Interface.WEB, Type.SMOKE, Feature.ACCOUNTS, Release.VER_0_5_0})
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

    @Test
    @WithTagValuesOf({Interface.WEB, Type.SMOKE, Feature.ACCOUNTS, Release.VER_0_10_0})
    public void member_cant_update_email_when_he_has_not_permission() throws Exception {
        // given
        String testUsername = "emailpermission-test";
        String testDisplayedName = "Email permission user";
        make_rollback_after_test_case(delete_testbed_member(testUsername));

        // when
        registrationSteps
            .register_new_member(testUsername, testDisplayedName, "email@emailtest.com",
                "email4", "email4");

        signInSteps.sign_in_as_administrator_with_success();
        acpMemberPermissionsSteps.open_acp_member_permissions_page();
        acpMemberPermissionsSteps.type_member_displayed_name_to_search(testDisplayedName);
        acpMemberPermissionsSteps.click_get_permissions_for_member_button();
        acpMemberPermissionsSteps.choose_custom_permission_table();
        acpMemberPermissionsSteps.set_can_change_email_permission(PermissionValue.NEVER);
        acpMemberPermissionsSteps.click_save_button();
        signInSteps.sign_out();

        signInSteps
            .sign_in_with_credentials_with_success(testUsername, "email4", testDisplayedName);
        ucpSteps.open_ucp();
        ucpSteps.choose_profile_tab();
        ucpSteps.choose_edit_account_settings_option();

        // then
        editAccountSteps.email_field_should_be_read_only();

        // for rollback
        signInSteps.sign_out();
        signInSteps.sign_in_as_administrator_with_success();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.SMOKE, Feature.PROFILE, Release.VER_0_10_0})
    public void member_cant_update_displayed_name_when_he_has_not_permission() throws Exception {
        // given
        String testUsername = "dispnameperm-test";
        String testDisplayedName = "Displayed Name permission user";
        make_rollback_after_test_case(delete_testbed_member(testUsername));

        // when
        registrationSteps
            .register_new_member(testUsername, testDisplayedName, "email@emailtest.com",
                "email4", "email4");

        signInSteps.sign_in_as_administrator_with_success();
        acpMemberPermissionsSteps.open_acp_member_permissions_page();
        acpMemberPermissionsSteps.type_member_displayed_name_to_search(testDisplayedName);
        acpMemberPermissionsSteps.click_get_permissions_for_member_button();
        acpMemberPermissionsSteps.choose_custom_permission_table();
        acpMemberPermissionsSteps.set_can_change_displayed_name_permission(PermissionValue.NEVER);
        acpMemberPermissionsSteps.click_save_button();
        signInSteps.sign_out();

        signInSteps
            .sign_in_with_credentials_with_success(testUsername, "email4", testDisplayedName);
        ucpSteps.open_ucp();
        ucpSteps.choose_profile_tab();
        ucpSteps.choose_edit_profile_option();

        // then
        editProfileSteps.displayed_name_field_should_be_read_only();

        // for rollback
        signInSteps.sign_out();
        signInSteps.sign_in_as_administrator_with_success();
    }

    RollbackAction delete_testbed_member(String username) {
        return () -> {
            acpMemberBrowserSteps.remove_member_with_username(username);
        };
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
