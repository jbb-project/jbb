/*
 * Copyright (C) 2017 the original author or authors.
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

import org.jbb.qa.pages.ErrorPage;

public class StacktraceVisibilitySteps extends ScenarioSteps {
    ErrorPage errorPage;

    @Step
    public void open_error_page() {
        errorPage.open();
    }

    @Step
    public void should_contain_stacktrace() {
        errorPage.should_contains_stacetrace();
    }

    @Step
    public void should_not_contain_stacktrace() {
        errorPage.should_not_contain_stacetrace();
    }
}
