/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.web.membermanagement;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;

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
    public void click_save_button() {
        acpMemberBrowserPage.clickSave();
    }

    @Step
    public void should_not_found_any_results() {
        acpMemberBrowserPage.shouldSeeInfoAboutEmptyResult();
    }

    @Step
    public void click_remove_lock_button() {
        acpMemberBrowserPage.clickRemoveLockButton();
    }

    @Step
    public void should_contain_lock_expiration_date(String date) {
        acpMemberBrowserPage.shouldSeeInfoAboutLockExpirationTimeDate(date);
    }

    @Step
    public void remove_member_with_username(String username) {
        open_acp_member_browser_page();
        type_username_to_search(username);
        send_member_search_form();
        select_first_result();
        click_delete_member_button();
        open_acp_member_browser_page();
        type_username_to_search(username);
        send_member_search_form();
        should_not_found_any_results();
    }

    @Step
    public void add_administrator_role_for_member_with_username(String username) {
        open_acp_member_browser_page();
        type_username_to_search(username);
        send_member_search_form();
        select_first_result();
        set_admin_role_checkbox();
        click_save_button();
    }

    @Step
    public void set_admin_role_checkbox() {
        acpMemberBrowserPage.clickAdminRoleCheckbox();
    }
}
