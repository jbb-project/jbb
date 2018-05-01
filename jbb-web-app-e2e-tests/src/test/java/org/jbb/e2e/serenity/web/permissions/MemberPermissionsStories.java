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

import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.WithTagValuesOf;
import org.jbb.e2e.serenity.Tags.Feature;
import org.jbb.e2e.serenity.Tags.Interface;
import org.jbb.e2e.serenity.Tags.Release;
import org.jbb.e2e.serenity.Tags.Type;
import org.jbb.e2e.serenity.web.EndToEndWebStories;
import org.jbb.e2e.serenity.web.membermanagement.AcpMemberBrowserSteps;
import org.jbb.e2e.serenity.web.registration.RegistrationSteps;
import org.jbb.e2e.serenity.web.signin.SignInSteps;
import org.junit.Test;

public class MemberPermissionsStories extends EndToEndWebStories {

    @Steps
    AcpAdministratorPermissionsSteps acpAdministratorPermissionsSteps;
    @Steps
    AcpMemberPermissionsSteps acpMemberPermissionsSteps;
    @Steps
    RegistrationSteps registrationSteps;
    @Steps
    SignInSteps signInSteps;
    @Steps
    AcpMemberBrowserSteps acpMemberBrowserSteps;

    @Test
    @WithTagValuesOf({Interface.WEB, Type.SMOKE, Feature.PERMISSIONS_MANAGEMENT,
        Release.VER_0_10_0})
    public void admin_cannot_manage_administrator_permissions_when_he_has_not_permission()
        throws Exception {
        // given
        String testUsername = "adminperm-test";
        String testDisplayedName = "Admin permission user";
        make_rollback_after_test_case(delete_testbed_member(testUsername));

        // when
        registrationSteps
            .register_new_member(testUsername, testDisplayedName, "email@emailtest.com",
                "email4", "email4");

        signInSteps.sign_in_as_administrator_with_success();
        acpMemberBrowserSteps.add_administrator_role_for_member_with_username(testUsername);

        acpAdministratorPermissionsSteps.open_acp_administrator_permissions_page();
        acpAdministratorPermissionsSteps.type_member_displayed_name_to_search(testDisplayedName);
        acpAdministratorPermissionsSteps.click_get_permissions_for_member_button();
        acpAdministratorPermissionsSteps.choose_custom_permission_table();
        acpAdministratorPermissionsSteps
            .set_can_alter_member_permissions_permission(PermissionValue.NEVER);
        acpAdministratorPermissionsSteps.click_save_button();
        signInSteps.sign_out();

        signInSteps
            .sign_in_with_credentials_with_success(testUsername, "email4", testDisplayedName);
        acpMemberPermissionsSteps.open_acp_member_permissions_page();
        acpMemberPermissionsSteps.type_member_displayed_name_to_search(testDisplayedName);
        acpMemberPermissionsSteps.click_get_permissions_for_member_button();
        acpMemberPermissionsSteps.click_save_button();

        // then
        acpAdministratorPermissionsSteps.should_contains_info_about_access_denied();

        // for rollback
        signInSteps.sign_out();
        signInSteps.sign_in_as_administrator_with_success();
    }

    RollbackAction delete_testbed_member(String username) {
        return () -> {
            acpMemberBrowserSteps.remove_member_with_username(username);
        };
    }

}
