/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.web.faq;

import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.WithTagValuesOf;
import org.jbb.e2e.serenity.Tags.Feature;
import org.jbb.e2e.serenity.Tags.Interface;
import org.jbb.e2e.serenity.Tags.Release;
import org.jbb.e2e.serenity.Tags.Type;
import org.jbb.e2e.serenity.web.EndToEndWebStories;
import org.jbb.e2e.serenity.web.membermanagement.AcpMemberBrowserSteps;
import org.jbb.e2e.serenity.web.permissions.AcpMemberPermissionsSteps;
import org.jbb.e2e.serenity.web.permissions.PermissionValue;
import org.jbb.e2e.serenity.web.registration.RegistrationSteps;
import org.jbb.e2e.serenity.web.signin.SignInSteps;
import org.junit.Test;

public class FaqStories extends EndToEndWebStories {

    @Steps
    FaqSteps faqSteps;
    @Steps
    RegistrationSteps registrationSteps;
    @Steps
    SignInSteps signInSteps;
    @Steps
    AcpMemberPermissionsSteps acpMemberPermissionsSteps;
    @Steps
    AcpMemberBrowserSteps acpMemberBrowserSteps;

    @Test
    @WithTagValuesOf({Interface.WEB, Type.SMOKE, Feature.FAQ_MANAGEMENT, Release.VER_0_10_0})
    public void member_cant_view_faq_when_he_has_not_permission() throws Exception {
        // given
        String testUsername = "faqpermission-test";
        String testDisplayedName = "FAQ permission user";
        make_rollback_after_test_case(delete_testbed_member(testUsername));

        // when
        registrationSteps
            .register_new_member(testUsername, testDisplayedName, "email@emailtest.com",
                "email4", "email4");

        signInSteps.sign_in_as_administrator_with_success();
        acpMemberPermissionsSteps.open_acp_member_permissions_page();
        acpMemberPermissionsSteps.type_member_displayed_name_to_search(testDisplayedName);
        acpMemberPermissionsSteps.click_get_permissions_for_member_button();
        acpMemberPermissionsSteps.choose_custom_permission_table();
        acpMemberPermissionsSteps.set_can_view_faq_permission(PermissionValue.NEVER);
        acpMemberPermissionsSteps.click_save_button();
        signInSteps.sign_out();

        signInSteps
            .sign_in_with_credentials_with_success(testUsername, "email4", testDisplayedName);
        faqSteps.open_faq_page();

        // then
        faqSteps.should_contains_info_about_access_denied();

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
