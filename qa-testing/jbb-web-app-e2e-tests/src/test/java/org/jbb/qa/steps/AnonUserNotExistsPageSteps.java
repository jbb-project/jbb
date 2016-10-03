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

import org.jbb.qa.pages.NotExistsPage;

public class AnonUserNotExistsPageSteps extends ScenarioSteps {
    NotExistsPage notExistsPage;

    @Step
    public void opens_not_exists_page() {
        notExistsPage.open();
    }

    @Step
    public void should_contain_image_with_404_error_info() {
        notExistsPage.should_contain_image_with_404_error_info();
    }
}
