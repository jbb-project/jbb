/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.qa.steps;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;

import org.jbb.qa.Utils;
import org.jbb.qa.pages.HomePage;
import org.jbb.qa.pages.SignInPage;
import org.jbb.qa.pages.UcpPage;

import static org.assertj.core.api.Assertions.assertThat;


public class AnonUserHomePageSteps extends ScenarioSteps {
    HomePage homePage;
    UcpPage ucpPage;

    @Step
    public void opens_home_page() {
        homePage.open();
    }

    @Step
    public void should_see_jbb_footer() {
        assertThat(homePage.footer_content()).contains("jBB v.");
    }

    @Step
    public void should_see_registration_link() {
        homePage.has_registration_link();
    }

    @Step
    public void click_registration_link() {
        homePage.click_on_registration_link();
    }

    @Step
    public void click_sign_in_link() {
        homePage.click_on_sign_in_link();
    }

    @Step
    public void should_move_to_sign_in_page() {
        assertThat(Utils.currentUrl()).endsWith(SignInPage.URL);
    }

    @Step
    public void open_main_ucp_page() {
        ucpPage.open();
    }

    public void should_see_sign_in_page() {
        assertThat(Utils.currentUrl()).contains("signin");
    }
}
