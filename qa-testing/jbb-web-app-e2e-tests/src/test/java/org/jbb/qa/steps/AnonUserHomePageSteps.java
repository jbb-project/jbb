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

import org.jbb.qa.pages.HomePage;

import static org.assertj.core.api.Assertions.assertThat;


public class AnonUserHomePageSteps {
    HomePage homePage;

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

}