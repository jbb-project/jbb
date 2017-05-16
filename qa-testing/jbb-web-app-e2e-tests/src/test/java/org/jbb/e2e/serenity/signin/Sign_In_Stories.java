/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.signin;

import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.WithTagValuesOf;

import org.jbb.e2e.serenity.Jbb_Base_Serenity_Stories;
import org.jbb.e2e.serenity.Tags;
import org.jbb.e2e.serenity.commons.HomeSteps;
import org.jbb.e2e.serenity.registration.RegistrationSteps;
import org.junit.Test;

public class Sign_In_Stories extends Jbb_Base_Serenity_Stories {

    @Steps
    RegistrationSteps registrationSteps;
    @Steps
    SignInSteps signInSteps;
    @Steps
    HomeSteps homeSteps;

    @Test
    @WithTagValuesOf({Tags.Type.SMOKE, Tags.Feature.AUTHENTICATION, Tags.Release.VER_0_4_0})
    public void sign_in_link_at_home_page_is_working() throws Exception {
        // when
        homeSteps.opens_home_page();
        homeSteps.click_sign_in_link();

        // then
        homeSteps.should_move_to_sign_in_page();
    }

    @Test
    @WithTagValuesOf({Tags.Type.SMOKE, Tags.Feature.AUTHENTICATION, Tags.Release.VER_0_4_0})
    public void should_sign_in_when_correct_credencials_passed() throws Exception {
        // given
        registrationSteps.register_new_member("thomas", "Thomas", "thomas@thomas.com", "thomas1", "thomas1");

        // when
        signInSteps.opens_sign_in_page();
        signInSteps.type_username("thomas");
        signInSteps.type_password("thomas1");
        signInSteps.send_form();

        // then
        signInSteps.should_move_to_home_page();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.AUTHENTICATION, Tags.Release.VER_0_4_0})
    public void username_should_be_visible_after_sign_in() throws Exception {
        // given
        registrationSteps.register_new_member("thomas2", "Thomas2", "thomas2@thomas.com", "thomas1", "thomas1");

        // when
        signInSteps.opens_sign_in_page();
        signInSteps.type_username("thomas2");
        signInSteps.type_password("thomas1");
        signInSteps.send_form();

        // then
        signInSteps.should_see_own_displayed_name_in_navbar("Thomas2");
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.AUTHENTICATION, Tags.Release.VER_0_4_0})
    public void should_not_sign_in_when_bad_credencials_passed() throws Exception {
        // given
        registrationSteps.register_new_member("thomas3", "Thomas3", "thomas3@thomas.com", "thomas1", "thomas1");

        // when
        signInSteps.opens_sign_in_page();
        signInSteps.type_username("thomas3");
        signInSteps.type_password("thomasINCORRECT_pass1");
        signInSteps.send_form();

        // then
        signInSteps.should_be_informed_about_invalid_credencials();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.AUTHENTICATION, Tags.Release.VER_0_4_0})
    public void should_signin_with_credencials_for_first_autocreated_administrator_member() throws Exception {
        // when
        signInSteps.opens_sign_in_page();
        signInSteps.type_username("administrator");
        signInSteps.type_password("administrator");
        signInSteps.send_form();

        // then
        signInSteps.should_see_own_displayed_name_in_navbar("Administrator");
    }
}
