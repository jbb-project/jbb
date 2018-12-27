/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.rest.members;

import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.WithTagValuesOf;

import org.jbb.e2e.serenity.Tags.Interface;
import org.jbb.e2e.serenity.rest.EndToEndRestStories;
import org.jbb.e2e.serenity.rest.commons.TestMember;
import org.jbb.e2e.serenity.rest.commons.TestOAuthClient;
import org.jbb.e2e.serenity.rest.oauthclient.SetupOAuthSteps;
import org.jbb.lib.restful.domain.ErrorInfo;
import org.junit.Test;

import static org.jbb.e2e.serenity.Tags.Feature;
import static org.jbb.e2e.serenity.Tags.Release;
import static org.jbb.e2e.serenity.Tags.Type;
import static org.jbb.e2e.serenity.rest.commons.AuthRestSteps.ADMIN_DISPLAYED_NAME;
import static org.jbb.lib.commons.security.OAuthScope.MEMBER_READ;
import static org.jbb.lib.commons.security.OAuthScope.MEMBER_READ_CREATE;
import static org.jbb.lib.commons.security.OAuthScope.MEMBER_READ_DELETE;
import static org.jbb.lib.commons.security.OAuthScope.MEMBER_READ_WRITE;

public class GetMembersRestStories extends EndToEndRestStories {

    @Steps
    MemberResourceSteps memberResourceSteps;

    @Steps
    SetupMemberSteps setupMemberSteps;

    @Steps
    SetupOAuthSteps setupOAuthSteps;

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.GENERAL, Release.VER_0_10_0})
    public void should_administrator_account_be_visible_in_api_after_installation() {
        // when
        memberResourceSteps.get_with_displayed_name(ADMIN_DISPLAYED_NAME);

        // then
        memberResourceSteps.should_return_at_least_one_member();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.GENERAL, Release.VER_0_10_0})
    public void should_return_all_members_with_given_phrase_insensitive_in_displayed_name() {
        // given
        create_removable_member_with_displayed_name("Adrian");
        create_removable_member_with_displayed_name("Hanna");
        create_removable_member_with_displayed_name("Damian");
        create_removable_member_with_displayed_name("Bartek");
        create_removable_member_with_displayed_name("Anna");

        // when
        memberResourceSteps.get_with_displayed_name("aN");

        // then
        memberResourceSteps
                .should_return_members_with_displayed_names("Adrian", "Hanna", "Damian", "Anna");
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.GENERAL, Release.VER_0_12_0})
    public void client_with_member_read_scope_can_get_members_via_api() {
        // given
        TestMember member = setupMemberSteps.create_member();
        make_rollback_after_test_case(setupMemberSteps.delete_member(member));

        TestOAuthClient client = setupOAuthSteps.create_client_with_scope(MEMBER_READ);
        make_rollback_after_test_case(setupOAuthSteps.delete_oauth_client(client));
        authRestSteps.authorize_every_request_with_oauth_client(client);

        // when
        memberResourceSteps.get_with_displayed_name(member.getDisplayedName());

        // then
        memberResourceSteps.should_return_at_least_one_member();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.GENERAL, Release.VER_0_12_0})
    public void client_with_member_create_scope_can_get_members_via_api() {
        // given
        TestMember member = setupMemberSteps.create_member();
        make_rollback_after_test_case(setupMemberSteps.delete_member(member));

        TestOAuthClient client = setupOAuthSteps.create_client_with_scope(MEMBER_READ_CREATE);
        make_rollback_after_test_case(setupOAuthSteps.delete_oauth_client(client));
        authRestSteps.authorize_every_request_with_oauth_client(client);

        // when
        memberResourceSteps.get_with_displayed_name(member.getDisplayedName());

        // then
        memberResourceSteps.should_return_at_least_one_member();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.GENERAL, Release.VER_0_12_0})
    public void client_with_member_delete_scope_can_get_members_via_api() {
        // given
        TestMember member = setupMemberSteps.create_member();
        make_rollback_after_test_case(setupMemberSteps.delete_member(member));

        TestOAuthClient client = setupOAuthSteps.create_client_with_scope(MEMBER_READ_DELETE);
        make_rollback_after_test_case(setupOAuthSteps.delete_oauth_client(client));
        authRestSteps.authorize_every_request_with_oauth_client(client);

        // when
        memberResourceSteps.get_with_displayed_name(member.getDisplayedName());

        // then
        memberResourceSteps.should_return_at_least_one_member();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.GENERAL, Release.VER_0_12_0})
    public void client_with_member_write_scope_can_get_members_via_api() {
        // given
        TestMember member = setupMemberSteps.create_member();
        make_rollback_after_test_case(setupMemberSteps.delete_member(member));

        TestOAuthClient client = setupOAuthSteps.create_client_with_scope(MEMBER_READ_WRITE);
        make_rollback_after_test_case(setupOAuthSteps.delete_oauth_client(client));
        authRestSteps.authorize_every_request_with_oauth_client(client);

        // when
        memberResourceSteps.get_with_displayed_name(member.getDisplayedName());

        // then
        memberResourceSteps.should_return_at_least_one_member();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.GENERAL, Release.VER_0_12_0})
    public void client_without_member_scopes_cannot_get_members_via_api() {
        // given
        TestMember member = setupMemberSteps.create_member();
        make_rollback_after_test_case(setupMemberSteps.delete_member(member));

        TestOAuthClient client = setupOAuthSteps.create_client_with_all_scopes_except(
                MEMBER_READ, MEMBER_READ_CREATE, MEMBER_READ_DELETE, MEMBER_READ_WRITE);
        make_rollback_after_test_case(setupOAuthSteps.delete_oauth_client(client));
        authRestSteps.authorize_every_request_with_oauth_client(client);

        // when
        memberResourceSteps.get_with_displayed_name(member.getDisplayedName());

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.FORBIDDEN);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.GENERAL, Release.VER_0_10_0})
    public void should_return_bind_error_when_text_page_number_provided_for_member_search() {
        // when
        memberResourceSteps.get_member_page_with_page_number("aaa");

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.BIND_ERROR);
        memberResourceSteps.should_contain_error_detail_about_invalid_page_param();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.GENERAL, Release.VER_0_10_0})
    public void should_return_bind_error_when_nagative_page_number_provided_for_member_search() {
        // when
        memberResourceSteps.get_member_page_with_page_number("-1");

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.BIND_ERROR);
        memberResourceSteps.should_contain_error_detail_about_negative_page_param();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.GENERAL, Release.VER_0_10_0})
    public void should_return_bind_error_when_text_page_size_number_provided_for_member_search() {
        // when
        memberResourceSteps.get_member_page_with_page_size("aaa");

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.BIND_ERROR);
        memberResourceSteps.should_contain_error_detail_about_invalid_page_size_param();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.GENERAL, Release.VER_0_10_0})
    public void should_return_bind_error_when_negative_page_size_number_provided_for_member_search() {
        // when
        memberResourceSteps.get_member_page_with_page_size("-1");

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.BIND_ERROR);
        memberResourceSteps.should_contain_error_detail_about_too_small_page_size_param();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.GENERAL, Release.VER_0_10_0})
    public void should_return_bind_error_when_zero_page_size_number_provided_for_member_search() {
        // when
        memberResourceSteps.get_member_page_with_page_size("0");

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.BIND_ERROR);
        memberResourceSteps.should_contain_error_detail_about_too_small_page_size_param();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.GENERAL, Release.VER_0_10_0})
    public void should_return_bind_error_when_101_page_size_number_provided_for_member_search() {
        // when
        memberResourceSteps.get_member_page_with_page_size("101");

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.BIND_ERROR);
        memberResourceSteps.should_contain_error_detail_about_too_large_page_size_param();
    }

    private void create_removable_member_with_displayed_name(String displayedName) {
        TestMember adrianMember = setupMemberSteps.create_member_with_displayed_name(displayedName);
        make_rollback_after_test_case(setupMemberSteps.delete_member(adrianMember));
    }

}
