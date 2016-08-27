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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@RunWith(SerenityRunner.class)
public class Sign_In_Tests {
    @Steps
    static AnonUserRegistrationSteps anonUser;
    @Managed(uniqueSession = true)
    WebDriver driver;
    @Steps
    AnonUserSignInSteps signInUser;

    @Test
    @WithTagValuesOf({Tags.Type.SMOKE, Tags.Feature.AUTHENTICATION, Tags.From.RELEASE_0_4_0})
    public void should_signin_when_correct_credencials_passed() throws Exception {
        // given
        anonUser.register_new_member("thomas", "Thomas", "thomas@thomas.com", "thomas1", "thomas1");

        // when
        signInUser.opens_sign_in_page();
        signInUser.type_login("thomas");
        signInUser.type_password("thomas1");
        signInUser.send_form();

        // then
        // ...
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.AUTHENTICATION, Tags.From.RELEASE_0_4_0})
    public void should_not_signin_when_bad_credencials_passed() throws Exception {
        // given
        anonUser.register_new_member("thomas2", "Thomas2", "thomas2@thomas.com", "thomas1", "thomas1");

        // when
        signInUser.opens_sign_in_page();
        signInUser.type_login("thomas2");
        signInUser.type_password("thomasINCORRECT_pass1");
        signInUser.send_form();

        // then
        // ...
    }
}
