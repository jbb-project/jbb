/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.qa.smoke;

import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.annotations.Steps;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@RunWith(SerenityRunner.class)
public class SmokeIT {
    @Managed(driver = "htmlunit", uniqueSession = true)
    WebDriver driver;

    @Steps
    AnonUserSteps anonUser;

    @Test
    public void should_see_home_page() throws Exception {
        // when
        anonUser.opens_home_page();

        // then
        anonUser.should_see_jbb_footer();
    }
}
