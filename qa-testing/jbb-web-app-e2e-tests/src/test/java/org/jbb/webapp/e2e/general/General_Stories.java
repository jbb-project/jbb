/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.webapp.e2e.general;

import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.WithTagValuesOf;

import org.jbb.webapp.e2e.Jbb_Base_Stories;
import org.jbb.webapp.e2e.Tags;
import org.jbb.webapp.e2e.commons.AcpSteps;
import org.jbb.webapp.e2e.commons.HomeSteps;
import org.jbb.webapp.e2e.commons.NotExistsPageSteps;
import org.jbb.webapp.e2e.registration.RegistrationSteps;
import org.jbb.webapp.e2e.signin.SignInSteps;
import org.junit.Test;

public class General_Stories extends Jbb_Base_Stories {
    private static boolean acpTestUserRegistered = false;

    @Steps
    HomeSteps homeSteps;
    @Steps
    NotExistsPageSteps notExistsPageSteps;
    @Steps
    AcpSteps acpSteps;
    @Steps
    UserInMonitoringSteps monitoringSteps;
    @Steps
    RegistrationSteps registrationSteps;
    @Steps
    SignInSteps signInSteps;

    @Test
    @WithTagValuesOf({Tags.Type.SMOKE, Tags.Feature.GENERAL, Tags.Release.VER_0_3_0})
    public void should_see_home_page() throws Exception {
        // when
        homeSteps.opens_home_page();

        // then
        homeSteps.should_see_jbb_footer();
    }

    @Test
    @WithTagValuesOf({Tags.Type.SMOKE, Tags.Feature.GENERAL, Tags.Release.VER_0_4_0})
    public void should_see_404_image_when_not_exists_page_opened() throws Exception {
        // when
        notExistsPageSteps.open_not_exists_page();

        // then
        notExistsPageSteps.should_contain_image_with_404_error_info();
    }

    @Test
    @WithTagValuesOf({Tags.Type.SMOKE, Tags.Feature.GENERAL, Tags.Release.VER_0_5_0})
    public void getting_any_ucp_page_by_anon_user_should_redirect_to_signin_page() throws Exception {
        // when
        homeSteps.open_main_ucp_page();

        // then
        homeSteps.should_see_sign_in_page();
    }

    @Test
    @WithTagValuesOf({Tags.Type.SMOKE, Tags.Feature.GENERAL, Tags.Release.VER_0_6_0})
    public void getting_any_acp_page_by_anon_user_should_redirect_to_signin_page() throws Exception {
        // when
        acpSteps.open_acp();

        // then
        acpSteps.should_see_sign_in_page();
    }

    @Test
    @WithTagValuesOf({Tags.Type.SMOKE, Tags.Feature.GENERAL, Tags.Release.VER_0_6_0})
    public void registered_non_admin_user_should_not_see_acp_link() throws Exception {
        // given
        assumeThatTestUserIsRegistered();
        signInSteps.sign_in_with_credentials_with_success("acpTest", "acp11", "ACP Test");

        // when
        homeSteps.opens_home_page();

        // then
        homeSteps.should_not_see_acp_link();
    }

    @Test
    @WithTagValuesOf({Tags.Type.SMOKE, Tags.Feature.GENERAL, Tags.Release.VER_0_6_0})
    public void getting_any_acp_page_by_registered_user_should_throw_forbidden_error() throws Exception {
        // given
        assumeThatTestUserIsRegistered();
        signInSteps.sign_in_with_credentials_with_success("acpTest", "acp11", "ACP Test");

        // when
        acpSteps.open_acp();

        // then
        acpSteps.should_see_403_error();
    }

    @Test
    @WithTagValuesOf({Tags.Type.SMOKE, Tags.Feature.GENERAL, Tags.Release.VER_0_6_0})
    public void administrator_should_see_acp_link_after_sign_in() throws Exception {
        // given
        signInSteps.sign_in_with_credentials_with_success("administrator", "administrator", "Administrator");

        // when
        homeSteps.opens_home_page();

        // then
        homeSteps.should_see_acp_link();
    }

    @Test
    @WithTagValuesOf({Tags.Type.SMOKE, Tags.Feature.GENERAL, Tags.Release.VER_0_6_0})
    public void getting_monitoring_page_by_anon_user_should_redirect_to_signin_page() throws Exception {
        // when
        monitoringSteps.open_monitoring_page();

        // then
        monitoringSteps.should_see_sign_in_page();
    }

    @Test
    @WithTagValuesOf({Tags.Type.SMOKE, Tags.Feature.GENERAL, Tags.Release.VER_0_6_0})
    public void getting_monitoring_page_by_not_administrator_user_should_throw_forbidden_error() throws Exception {
        // given
        assumeThatTestUserIsRegistered();
        signInSteps.sign_in_with_credentials_with_success("acpTest", "acp11", "ACP Test");

        // when
        monitoringSteps.open_monitoring_page();

        // then
        monitoringSteps.should_see_403_error();
    }

    @Test
    @WithTagValuesOf({Tags.Type.SMOKE, Tags.Feature.GENERAL, Tags.Release.VER_0_6_0})
    public void administrator_can_open_monitoring_though_acp() throws Exception {
        // given
        signInSteps.sign_in_with_credentials_with_success("administrator", "administrator", "Administrator");

        // when
        acpSteps.open_acp();
        acpSteps.choose_system_tab();
        acpSteps.choose_monitoring_option();

        // then
        acpSteps.should_see_monitoring_page();
    }

    private void assumeThatTestUserIsRegistered() {
        if (!acpTestUserRegistered) {
            registrationSteps.register_new_member("acpTest", "ACP Test", "acp@acp.com", "acp11", "acp11");
            acpTestUserRegistered = true;
        }
    }
}
