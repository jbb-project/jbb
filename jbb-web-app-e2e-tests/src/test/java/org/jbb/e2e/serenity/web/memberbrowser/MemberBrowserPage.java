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

import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.DefaultUrl;

import static org.assertj.core.api.Assertions.assertThat;

@DefaultUrl(MemberBrowserPage.URL)
public class MemberBrowserPage extends PageObject {
    public static final String URL = "/members";

    public void should_contain_member_name(String name) {
        shouldContainText(name);
    }

    public void should_not_contain_member_name(String displayedName) {
        assertThat(containsText(displayedName)).isFalse();
    }
}
