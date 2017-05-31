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
import org.jbb.e2e.serenity.signin.SignInSteps;
import org.junit.Test;


public class AcpSessionManagementStories extends JbbBaseSerenityStories {

    @Steps
    AcpSessionManagementSteps sessionManagementSteps;

    @Steps
    SignInSteps signInSteps;

    @Test
    @WithTagValuesOf({Tags.Type.SMOKE, Tags.Feature.SESSION_SETTINGS, Tags.Release.VER_0_8_0})
    public void update_session_maximum_inactive_interval_time_should_be_possible() {
        //given
        signInSteps.sign_in_as_administrator_with_success();

        //when
        sessionManagementSteps.open_session_management_page();
        sessionManagementSteps.type_maximum_inactive_interval("5000");
        sessionManagementSteps.save_session_settings_form();

        //then
        sessionManagementSteps.should_be_informed_about_saving_settings();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.SESSION_SETTINGS, Tags.Release.VER_0_8_0})
    public void editing_session_maximum_inactive_interval_time_with_empty_value_is_impossible() {
        //given
        signInSteps.sign_in_as_administrator_with_success();

        //when
        sessionManagementSteps.open_session_management_page();
        sessionManagementSteps.type_maximum_inactive_interval("");
        sessionManagementSteps.save_session_settings_form();

        //then
        sessionManagementSteps.should_be_informed_about_null_value();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.SESSION_SETTINGS, Tags.Release.VER_0_8_0})
    public void editing_session_maximum_inactive_interval_time_with_text_value_is_impossible() {

        //given
        signInSteps.sign_in_as_administrator_with_success();

        //when
        sessionManagementSteps.open_session_management_page();
        sessionManagementSteps.type_maximum_inactive_interval("abcs");
        sessionManagementSteps.save_session_settings_form();

        //then
        sessionManagementSteps.should_be_informed_about_invalid_value();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.SESSION_SETTINGS, Tags.Release.VER_0_8_0})
    public void editing_session_maximum_inactive_interval_time_with_negative_value_is_impossible() {

        //given
        signInSteps.sign_in_as_administrator_with_success();

        //when
        sessionManagementSteps.open_session_management_page();
        sessionManagementSteps.type_maximum_inactive_interval("-1");
        sessionManagementSteps.save_session_settings_form();

        //then
        sessionManagementSteps.should_be_informed_about_non_positive_value();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.SESSION_SETTINGS, Tags.Release.VER_0_8_0})
    public void editing_session_maximum_inactive_interval_time_with_zero_is_impossible() {

        //given
        signInSteps.sign_in_as_administrator_with_success();

        //when
        sessionManagementSteps.open_session_management_page();
        sessionManagementSteps.type_maximum_inactive_interval("0");
        sessionManagementSteps.save_session_settings_form();

        //then
        sessionManagementSteps.should_be_informed_about_non_positive_value();
    }

}
