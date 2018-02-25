/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.web.memberbrowser;

import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.WithTagValuesOf;

import org.jbb.e2e.serenity.web.EndToEndWebStories;
import org.jbb.e2e.serenity.web.registration.RegistrationSteps;
import org.junit.Test;

import static org.jbb.e2e.serenity.Tags.Feature;
import static org.jbb.e2e.serenity.Tags.Interface;
import static org.jbb.e2e.serenity.Tags.Release;
import static org.jbb.e2e.serenity.Tags.Type;

public class MemberBrowserStories extends EndToEndWebStories {

    @Steps
    MemberBrowserSteps memberBrowserSteps;
    @Steps
    RegistrationSteps registrationSteps;

    @Test
    @WithTagValuesOf({Interface.WEB, Type.SMOKE, Feature.GENERAL, Release.VER_0_4_0})
    public void should_administrator_account_be_visible_after_installation() throws Exception {
        // when
        memberBrowserSteps.open_members_browser_page();

        // then
        memberBrowserSteps.should_see_member_name("Administrator");
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.GENERAL, Release.VER_0_4_0})
    public void should_member_be_visible_in_browser_after_registration() throws Exception {
        // given
        registrationSteps.register_new_member("juliet", "Juliette!", "foo@bar.com", "julliette1", "julliette1");

        // when
        memberBrowserSteps.open_members_browser_page();

        // then
        memberBrowserSteps.should_see_member_name("Juliette!");
    }
}
