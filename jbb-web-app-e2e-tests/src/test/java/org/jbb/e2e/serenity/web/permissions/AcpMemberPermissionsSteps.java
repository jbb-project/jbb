/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.web.permissions;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;

public class AcpMemberPermissionsSteps extends ScenarioSteps {

    AcpMemberPermissionsPage acpMemberPermissionsPage;

    @Step
    public void open_acp_member_permissions_page() {
        acpMemberPermissionsPage.open();
    }

    @Step
    public void type_member_displayed_name_to_search(String displayedName) {
        acpMemberPermissionsPage.typeMemberDisplayedName(displayedName);
    }

    @Step
    public void click_get_permissions_for_member_button() {
        acpMemberPermissionsPage.clickGetPermissionsForMember();
    }

    @Step
    public void choose_custom_permission_table() {
        acpMemberPermissionsPage.clickCustomPermissionTableCheckbox();
    }

    @Step
    public void click_save_button() {
        acpMemberPermissionsPage.clickSaveButton();
    }

    @Step
    public void should_contains_info_about_access_denied() {
        acpMemberPermissionsPage.containsAccessDeniedInfo();
    }

    @Step
    public void set_can_change_email_permission(PermissionValue permissionValue) {
        acpMemberPermissionsPage.clickPermissionCheckbox("MBR_CAN_CHANGE_EMAIL", permissionValue);
    }

    @Step
    public void set_can_change_displayed_name_permission(PermissionValue permissionValue) {
        acpMemberPermissionsPage
            .clickPermissionCheckbox("MBR_CAN_CHANGE_DISPLAYED_NAME", permissionValue);
    }

    @Step
    public void set_can_view_faq_permission(PermissionValue permissionValue) {
        acpMemberPermissionsPage.clickPermissionCheckbox("MBR_CAN_VIEW_FAQ", permissionValue);
    }

}
