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
import org.jbb.e2e.serenity.memberbrowser.MemberBrowserSteps;
import org.jbb.e2e.serenity.membermanagement.AcpMemberBrowserSteps;
import org.jbb.e2e.serenity.registration.RegistrationSteps;
import org.jbb.e2e.serenity.signin.SignInSteps;
import org.junit.Test;


public class AcpSessionManagementStories  extends JbbBaseSerenityStories {

    @Steps
    AcpSessionManagementSteps sessionManagementSteps;

    @Steps
    SignInSteps signInSteps;

    @Steps
    RegistrationSteps registrationSteps;

    @Steps
    MemberBrowserSteps memberBrowserSteps;

    @Steps
    AcpMemberBrowserSteps acpMemberBrowserSteps;

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.SESSION_SETTINGS, Tags.Release.VER_0_8_0})
    public void try_save_correct_value_of_maximum_inactive_interval_time(){

        //given
        signInSteps.sign_in_as_administrator_with_success();

        //when
        sessionManagementSteps.open_session_management_page();
        sessionManagementSteps.type_maximum_inactive_interval("5000");
        sessionManagementSteps.click_save_button();

        //then
        sessionManagementSteps.user_should_save_alert_with_message("Settings saved correctly");
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.SESSION_SETTINGS, Tags.Release.VER_0_8_0})
    public void try_save_empty_value_of_maximum_inactive_interval_time(){

        //given
        signInSteps.sign_in_as_administrator_with_success();

        //when
        sessionManagementSteps.open_session_management_page();
        sessionManagementSteps.type_maximum_inactive_interval("");
        sessionManagementSteps.click_save_button();

        //then
        sessionManagementSteps.user_should_save_alert_with_message("may not be null");
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.SESSION_SETTINGS, Tags.Release.VER_0_8_0})
    public void try_save_characters_as_value_of_maximum_inactive_interval_time(){

        //given
        signInSteps.sign_in_as_administrator_with_success();

        //when
        sessionManagementSteps.open_session_management_page();
        sessionManagementSteps.type_maximum_inactive_interval("abcs");
        sessionManagementSteps.click_save_button();

        //then
        sessionManagementSteps.user_should_save_alert_with_message("Invalid value");
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.SESSION_SETTINGS, Tags.Release.VER_0_8_0})
    public void new_signed_in_user_should_be_display_on_session_board(){

        make_rollback_after_test_case(remove_created_users());
        //given
        registrationSteps.register_new_member("username2","displayName2","email2@gmail.com","password2","password2");
        signInSteps.sign_in_as_administrator_with_success();

        //when
        sessionManagementSteps.open_session_management_page();

        //then
        sessionManagementSteps.user__with_specify_username_should_be_display_on_session_board("username");

    }

    RollbackAction remove_created_users() {
        return () -> {
            memberBrowserSteps.open_members_browser_page();
            memberBrowserSteps.should_see_member_name("username2");
            signInSteps.sign_in_as_administrator_with_success();
            acpMemberBrowserSteps.open_acp_member_browser_page();
            acpMemberBrowserSteps.type_username_to_search("username2");
            acpMemberBrowserSteps.send_member_search_form();
            acpMemberBrowserSteps.select_first_result();
            acpMemberBrowserSteps.click_delete_member_button();
            acpMemberBrowserSteps.open_acp_member_browser_page();
            acpMemberBrowserSteps.type_username_to_search("username2");
            acpMemberBrowserSteps.send_member_search_form();
            acpMemberBrowserSteps.should_not_found_any_results();
            memberBrowserSteps.open_members_browser_page();
            memberBrowserSteps.should_not_see_member_name("username2");
        };
    }


}
