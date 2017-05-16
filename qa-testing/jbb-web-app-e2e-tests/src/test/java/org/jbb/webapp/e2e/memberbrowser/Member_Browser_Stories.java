/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.webapp.e2e.memberbrowser;

import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.WithTagValuesOf;

import org.jbb.webapp.e2e.Jbb_Base_Stories;
import org.jbb.webapp.e2e.Tags;
import org.jbb.webapp.e2e.registration.RegistrationSteps;
import org.junit.Test;

public class Member_Browser_Stories extends Jbb_Base_Stories {

    @Steps
    MemberBrowserSteps memberBrowserSteps;
    @Steps
    RegistrationSteps registrationSteps;

    @Test
    @WithTagValuesOf({Tags.Type.SMOKE, Tags.Feature.GENERAL, Tags.Release.VER_0_4_0})
    public void should_administrator_account_be_visible_after_installation() throws Exception {
        // when
        memberBrowserSteps.open_members_browser_page();

        // then
        memberBrowserSteps.should_see_member_name("Administrator");
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.GENERAL, Tags.Release.VER_0_4_0})
    public void should_member_be_visible_in_browser_after_registration() throws Exception {
        // given
        registrationSteps.register_new_member("juliet", "Juliette!", "foo@bar.com", "julliette1", "julliette1");

        // when
        memberBrowserSteps.open_members_browser_page();

        // then
        memberBrowserSteps.should_see_member_name("Juliette!");
    }
}
