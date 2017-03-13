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

import org.jbb.qa.pages.AcpMemberBrowserPage;

public class AcpMemberBrowserSteps extends ScenarioSteps {
    AcpMemberBrowserPage acpMemberBrowserPage;

    @Step
    public void open_acp_member_browser_page() {
        acpMemberBrowserPage.open();
    }

    @Step
    public void type_username_to_search(String username) {
        acpMemberBrowserPage.typeUsername(username);
    }

    @Step
    public void send_member_search_form() {
        acpMemberBrowserPage.clickSearchButton();
    }

    @Step
    public void select_first_result() {
        acpMemberBrowserPage.clickSelect();
    }

    @Step
    public void click_delete_member_button() {
        acpMemberBrowserPage.clickDeleteMemberButton();
    }

    @Step
    public void should_not_found_any_results() {
        acpMemberBrowserPage.shouldSeeInfoAboutEmptyResult();
    }
}
