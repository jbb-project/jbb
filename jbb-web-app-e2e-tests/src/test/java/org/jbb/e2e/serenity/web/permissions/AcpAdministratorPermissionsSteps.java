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

public class AcpAdministratorPermissionsSteps extends ScenarioSteps {

    AcpAdministratorPermissionsPage acpAdministratorPermissionsPage;

    @Step
    public void open_acp_administrator_permissions_page() {
        acpAdministratorPermissionsPage.open();
    }

    @Step
    public void type_member_displayed_name_to_search(String displayedName) {
        acpAdministratorPermissionsPage.typeMemberDisplayedName(displayedName);
    }

    @Step
    public void click_get_permissions_for_member_button() {
        acpAdministratorPermissionsPage.clickGetPermissionsForMember();
    }

    @Step
    public void choose_custom_permission_table() {
        acpAdministratorPermissionsPage.clickCustomPermissionTableCheckbox();
    }

    @Step
    public void click_save_button() {
        acpAdministratorPermissionsPage.clickSaveButton();
    }

    @Step
    public void should_contains_info_about_access_denied() {
        acpAdministratorPermissionsPage.containsAccessDeniedInfo();
    }

    @Step
    public void set_can_alter_administrator_permissions_permission(
        PermissionValue permissionValue) {
        acpAdministratorPermissionsPage
            .clickPermissionCheckbox("ADM_CAN_ALTER_ADMINISTRATOR_PERMISSIONS", permissionValue);
    }

    @Step
    public void set_can_alter_member_permissions_permission(PermissionValue permissionValue) {
        acpAdministratorPermissionsPage
            .clickPermissionCheckbox("ADM_CAN_ALTER_MEMBER_PERMISSIONS", permissionValue);
    }

    @Step
    public void set_can_manage_permission_roles_permission(PermissionValue permissionValue) {
        acpAdministratorPermissionsPage
            .clickPermissionCheckbox("ADM_CAN_MANAGE_PERMISSION_ROLES", permissionValue);
    }

    @Step
    public void set_can_manage_members_permission(PermissionValue permissionValue) {
        acpAdministratorPermissionsPage
            .clickPermissionCheckbox("ADM_CAN_MANAGE_MEMBERS", permissionValue);
    }

    @Step
    public void set_can_delete_members_permission(PermissionValue permissionValue) {
        acpAdministratorPermissionsPage
            .clickPermissionCheckbox("ADM_CAN_DELETE_MEMBERS", permissionValue);
    }

    @Step
    public void set_can_add_forums_permission(PermissionValue permissionValue) {
        acpAdministratorPermissionsPage
            .clickPermissionCheckbox("ADM_CAN_ADD_FORUMS", permissionValue);
    }

    @Step
    public void set_can_modify_forums_permission(PermissionValue permissionValue) {
        acpAdministratorPermissionsPage
            .clickPermissionCheckbox("ADM_CAN_MODIFY_FORUMS", permissionValue);
    }

}
