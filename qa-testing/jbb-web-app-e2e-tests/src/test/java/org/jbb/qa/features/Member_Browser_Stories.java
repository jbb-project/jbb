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
import org.jbb.qa.steps.AnonUserMemberBrowserSteps;
import org.jbb.qa.steps.AnonUserRegistrationSteps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@RunWith(SerenityRunner.class)
public class Member_Browser_Stories {
    @Managed(uniqueSession = true)
    WebDriver driver;

    @Steps
    AnonUserMemberBrowserSteps anonUser;

    @Steps
    AnonUserRegistrationSteps anonRegistrationUser;

    @Test
    @WithTagValuesOf({Tags.Type.SMOKE, Tags.Feature.GENERAL, Tags.Release.VER_0_4_0})
    public void should_administrator_account_be_visible_after_installation() throws Exception {
        // when
        anonUser.opens_members_browser_page();

        // then
        anonUser.should_see_member_name("Administrator");
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.GENERAL, Tags.Release.VER_0_4_0})
    public void should_member_be_visible_in_browser_after_registration() throws Exception {
        // given
        anonRegistrationUser.register_new_member("juliet", "Juliette!", "foo@bar.com", "julliette1", "julliette1");

        // when
        anonUser.opens_members_browser_page();

        // then
        anonUser.should_see_member_name("Juliette!");
    }
}
