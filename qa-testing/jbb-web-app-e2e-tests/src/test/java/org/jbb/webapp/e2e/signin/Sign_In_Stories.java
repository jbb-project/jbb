/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.webapp.e2e.signin;

import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.WithTagValuesOf;

import org.jbb.webapp.e2e.Jbb_Base_Stories;
import org.jbb.webapp.e2e.Tags;
import org.jbb.webapp.e2e.commons.HomePageSteps;
import org.jbb.webapp.e2e.registration.RegistrationSteps;
import org.junit.Test;

public class Sign_In_Stories extends Jbb_Base_Stories {

    @Steps
    RegistrationSteps anonUser;

    @Steps
    SignInSteps signInUser;

    @Steps
    HomePageSteps anonUserAtHomePage;

    @Test
    @WithTagValuesOf({Tags.Type.SMOKE, Tags.Feature.AUTHENTICATION, Tags.Release.VER_0_4_0})
    public void sign_in_link_at_home_page_is_working() throws Exception {
        // when
        anonUserAtHomePage.opens_home_page();
        anonUserAtHomePage.click_sign_in_link();

        // then
        anonUserAtHomePage.should_move_to_sign_in_page();
    }

    @Test
    @WithTagValuesOf({Tags.Type.SMOKE, Tags.Feature.AUTHENTICATION, Tags.Release.VER_0_4_0})
    public void should_sign_in_when_correct_credencials_passed() throws Exception {
        // given
        anonUser.register_new_member("thomas", "Thomas", "thomas@thomas.com", "thomas1", "thomas1");

        // when
        signInUser.opens_sign_in_page();
        signInUser.type_username("thomas");
        signInUser.type_password("thomas1");
        signInUser.send_form();

        // then
        signInUser.should_move_to_home_page();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.AUTHENTICATION, Tags.Release.VER_0_4_0})
    public void username_should_be_visible_after_sign_in() throws Exception {
        // given
        anonUser.register_new_member("thomas2", "Thomas2", "thomas2@thomas.com", "thomas1", "thomas1");

        // when
        signInUser.opens_sign_in_page();
        signInUser.type_username("thomas2");
        signInUser.type_password("thomas1");
        signInUser.send_form();

        // then
        signInUser.should_see_own_displayed_name_in_navbar("Thomas2");
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.AUTHENTICATION, Tags.Release.VER_0_4_0})
    public void should_not_sign_in_when_bad_credencials_passed() throws Exception {
        // given
        anonUser.register_new_member("thomas3", "Thomas3", "thomas3@thomas.com", "thomas1", "thomas1");

        // when
        signInUser.opens_sign_in_page();
        signInUser.type_username("thomas3");
        signInUser.type_password("thomasINCORRECT_pass1");
        signInUser.send_form();

        // then
        signInUser.should_be_informed_about_invalid_credencials();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.AUTHENTICATION, Tags.Release.VER_0_4_0})
    public void should_signin_with_credencials_for_first_autocreated_administrator_member() throws Exception {
        // when
        signInUser.opens_sign_in_page();
        signInUser.type_username("administrator");
        signInUser.type_password("administrator");
        signInUser.send_form();

        // then
        signInUser.should_see_own_displayed_name_in_navbar("Administrator");
    }
}
