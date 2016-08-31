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
import org.jbb.qa.steps.AnonUserRegistrationSteps;
import org.jbb.qa.steps.AnonUserSignInSteps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@RunWith(SerenityRunner.class)
public class Sign_In_Stories {
    @Steps
    AnonUserRegistrationSteps anonUser;

    @Managed(uniqueSession = true)
    WebDriver driver;

    @Steps
    AnonUserSignInSteps signInUser;

    @Steps
    AnonUserHomePageSteps anonUserAtHomePage;


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
        signInUser.type_login("thomas");
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
        signInUser.type_login("thomas2");
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
        signInUser.type_login("thomas3");
        signInUser.type_password("thomasINCORRECT_pass1");
        signInUser.send_form();

        // then
        signInUser.should_be_informed_about_invalid_credencials();
    }
}
