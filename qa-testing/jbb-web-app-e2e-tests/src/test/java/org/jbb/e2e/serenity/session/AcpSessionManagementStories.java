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


public class AcpSessionManagementStories  extends JbbBaseSerenityStories {

    @Steps
    AcpSessionManagementSteps sessionManagementSteps;

    @Steps
    SignInSteps signInSteps;

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.SESSION_SETTINGS, Tags.Release.VER_0_8_0})
    public void save_correct_value_of_maximum_inactive_interval_time(){

        //given
        signInSteps.sign_in_as_administrator_with_success();

        //when
//        sessionManagementSteps.open_session_management_page();
//        sessionManagementSteps.provide_correct_value_to_text_field();
//        sessionManagementSteps.click_save_button();

        //then
//        sessionManagementSteps.user_should_save_alert_about_successful_operation();
    }
}
