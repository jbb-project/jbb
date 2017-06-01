/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.stacktrace;

import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.DefaultUrl;

import static org.assertj.core.api.Assertions.assertThat;

@DefaultUrl(ErrorPage.URL)
public class ErrorPage extends PageObject {
    public static final String URL = "/err";

    public void should_contains_stacetrace() {
        shouldContainText("Stacktrace for developers");
    }

    public void should_not_contain_stacetrace() {
        assertThat(containsText("Stacktrace for developers")).isFalse();
    }


}
