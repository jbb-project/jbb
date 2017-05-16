/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.webapp.e2e.membermanagement;

import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.WithTagValuesOf;

import org.jbb.webapp.e2e.Jbb_Base_Stories;
import org.jbb.webapp.e2e.Tags;
import org.jbb.webapp.e2e.memberbrowser.MemberBrowserSteps;
import org.jbb.webapp.e2e.registration.RegistrationSteps;
import org.jbb.webapp.e2e.signin.SignInSteps;
import org.junit.Test;

public class Member_Deleting_Stories extends Jbb_Base_Stories {

    @Steps
    RegistrationSteps registrationSteps;

    @Steps
    MemberBrowserSteps memberBrowserSteps;

    @Steps
    SignInSteps signInSteps;

    @Steps
    AcpMemberBrowserSteps acpMemberBrowserSteps;

    @Test
    @WithTagValuesOf({Tags.Type.SMOKE, Tags.Feature.REGISTRATION, Tags.Release.VER_0_7_0})
    public void remove_member_by_administrator() throws Exception {
        registrationSteps.register_new_member("toDelete", "ToDelete",
                "to@delete.com", "toDelete", "toDelete");
        memberBrowserSteps.opens_members_browser_page();
        memberBrowserSteps.should_see_member_name("ToDelete");
        signInSteps.sign_in_with_credentials_with_success("administrator", "administrator", "Administrator");
        acpMemberBrowserSteps.open_acp_member_browser_page();
        acpMemberBrowserSteps.type_username_to_search("toDelete");
        acpMemberBrowserSteps.send_member_search_form();
        acpMemberBrowserSteps.select_first_result();
        acpMemberBrowserSteps.click_delete_member_button();
        acpMemberBrowserSteps.open_acp_member_browser_page();
        acpMemberBrowserSteps.type_username_to_search("toDelete");
        acpMemberBrowserSteps.send_member_search_form();
        acpMemberBrowserSteps.should_not_found_any_results();
        memberBrowserSteps.opens_members_browser_page();
        memberBrowserSteps.should_not_see_member_name("ToDelete");

    }
}
