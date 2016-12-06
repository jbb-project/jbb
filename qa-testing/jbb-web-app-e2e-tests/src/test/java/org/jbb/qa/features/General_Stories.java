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
import org.jbb.qa.steps.AnonUserHomePageSteps;
import org.jbb.qa.steps.AnonUserNotExistsPageSteps;
import org.jbb.qa.steps.AnonUserRegistrationSteps;
import org.jbb.qa.steps.AnonUserSignInSteps;
import org.jbb.qa.steps.UserInAcpSteps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@RunWith(SerenityRunner.class)
public class General_Stories {
    private static boolean acpTestUserRegistered = false;

    @Managed(uniqueSession = true)
    WebDriver driver;

    @Steps
    AnonUserHomePageSteps anonUser;

    @Steps
    AnonUserNotExistsPageSteps anonUser_;

    @Steps
    UserInAcpSteps anonUser__;

    @Steps
    AnonUserRegistrationSteps anonRegistrationUser;
    @Steps
    AnonUserSignInSteps signInUser;

    @Test
    @WithTagValuesOf({Tags.Type.SMOKE, Tags.Feature.GENERAL, Tags.Release.VER_0_3_0})
    public void should_see_home_page() throws Exception {
        // when
        anonUser.opens_home_page();

        // then
        anonUser.should_see_jbb_footer();
    }

    @Test
    @WithTagValuesOf({Tags.Type.SMOKE, Tags.Feature.GENERAL, Tags.Release.VER_0_4_0})
    public void should_see_404_image_when_not_exists_page_opened() throws Exception {
        // when
        anonUser_.opens_not_exists_page();

        // then
        anonUser_.should_contain_image_with_404_error_info();
    }

    @Test
    @WithTagValuesOf({Tags.Type.SMOKE, Tags.Feature.GENERAL, Tags.Release.VER_0_5_0})
    public void getting_any_ucp_page_by_anon_user_should_redirect_to_signin_page() throws Exception {
        // when
        anonUser.open_main_ucp_page();

        // then
        anonUser.should_see_sign_in_page();
    }

    @Test
    @WithTagValuesOf({Tags.Type.SMOKE, Tags.Feature.GENERAL, Tags.Release.VER_0_6_0})
    public void getting_any_acp_page_by_anon_user_should_redirect_to_signin_page() throws Exception {
        // when
        anonUser__.open_main_acp_page();

        // then
        anonUser__.should_see_sign_in_page();
    }

    @Test
    @WithTagValuesOf({Tags.Type.SMOKE, Tags.Feature.GENERAL, Tags.Release.VER_0_6_0})
    public void registered_non_admin_user_should_not_see_acp_link() throws Exception {
        // given
        assumeThatTestUserIsRegistered();
        signInUser.sign_in_with_credentials_with_success("acpTest", "acp11", "ACP Test");

        // when
        anonUser.opens_home_page();

        // then
        anonUser.should_not_see_acp_link();
    }

    @Test
    @WithTagValuesOf({Tags.Type.SMOKE, Tags.Feature.GENERAL, Tags.Release.VER_0_6_0})
    public void getting_any_acp_page_by_registered_user_should_throw_forbidden_error() throws Exception {
        // given
        assumeThatTestUserIsRegistered();
        signInUser.sign_in_with_credentials_with_success("acpTest", "acp11", "ACP Test");

        // when
        anonUser__.open_main_acp_page();

        // then
        anonUser__.should_see_403_error();
    }

    @Test
    @WithTagValuesOf({Tags.Type.SMOKE, Tags.Feature.GENERAL, Tags.Release.VER_0_6_0})
    public void administrator_should_see_acp_link_after_sign_in() throws Exception {
        // given
        signInUser.sign_in_with_credentials_with_success("administrator", "administrator", "Administrator");

        // when
        anonUser.opens_home_page();

        // then
        anonUser.should_see_acp_link();
    }

    private void assumeThatTestUserIsRegistered() {
        if (!acpTestUserRegistered) {
            anonRegistrationUser.register_new_member("acpTest", "ACP Test", "acp@acp.com", "acp11", "acp11");
            acpTestUserRegistered = true;
        }
    }
}
