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

import org.jbb.qa.steps.AnonUserRegistrationSteps;
import org.jbb.qa.steps.AnonUserSignInSteps;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SerenityRunner.class)
public class Sign_In_Tests {
    @Managed(uniqueSession = true)
    WebDriver driver;

    @Steps
    AnonUserRegistrationSteps anonUser;

    @Steps
    AnonUserSignInSteps signInUser;

    @Before
    public void setUp() throws Exception {
        // given
        anonUser.register_new_member("thomas", "Thomas", "thomas@thomas.com", "thomas1", "thomas1");
    }

    @Test
    @Ignore //Test in progress...
    public void foo() throws Exception {
        // when
        signInUser.opens_sign_in_page();
        signInUser.type_login("thomas");
        signInUser.type_password("thomas1");
        signInUser.send_form();

        // then
        assertThat(driver.getCurrentUrl().toString()).isEqualTo("/");
    }
}
