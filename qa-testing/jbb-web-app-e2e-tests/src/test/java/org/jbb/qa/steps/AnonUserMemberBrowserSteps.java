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

import org.jbb.qa.pages.MemberBrowserPage;

public class AnonUserMemberBrowserSteps extends ScenarioSteps {
    MemberBrowserPage memberBrowserPage;

    @Step
    public void opens_members_browser_page() {
        memberBrowserPage.open();
    }

    @Step
    public void should_see_member_name(String name) {
        memberBrowserPage.should_contain_member_name(name);
    }
}
