/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.rest.account;

import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.WithTagValuesOf;

import org.jbb.e2e.serenity.Tags.Feature;
import org.jbb.e2e.serenity.Tags.Interface;
import org.jbb.e2e.serenity.Tags.Release;
import org.jbb.e2e.serenity.Tags.Type;
import org.jbb.e2e.serenity.rest.EndToEndRestStories;
import org.jbb.e2e.serenity.rest.commons.TestMember;
import org.jbb.e2e.serenity.rest.commons.TestOAuthClient;
import org.jbb.e2e.serenity.rest.members.SetupMemberSteps;
import org.jbb.e2e.serenity.rest.oauthclient.SetupOAuthSteps;
import org.jbb.lib.restful.domain.ErrorInfo;
import org.junit.Test;

import static org.jbb.lib.commons.security.OAuthScope.MEMBER_ACCOUNT_READ;
import static org.jbb.lib.commons.security.OAuthScope.MEMBER_ACCOUNT_READ_WRITE;

public class GetMemberAccountRestStories extends EndToEndRestStories {

    @Steps
    MemberAccountResourceSteps memberAccountResourceSteps;

    @Steps
    SetupMemberSteps setupMemberSteps;

    @Steps
    SetupOAuthSteps setupOAuthSteps;

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.ACCOUNTS, Release.VER_0_10_0})
    public void guest_cannot_get_any_account_data_via_api() {
        // given
        TestMember member = setupMemberSteps.create_member();
        make_rollback_after_test_case(setupMemberSteps.delete_member(member));

        // when
        memberAccountResourceSteps.get_member_account(member.getMemberId());

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.UNAUTHORIZED);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.ACCOUNTS, Release.VER_0_10_0})
    public void regular_member_can_get_own_account_data_via_api() {
        // given
        TestMember member = setupMemberSteps.create_member();
        make_rollback_after_test_case(setupMemberSteps.delete_member(member));
        authRestSteps.include_basic_auth_header_for_every_request(member);

        // when
        memberAccountResourceSteps.get_member_account(member.getMemberId());

        // then
        memberAccountResourceSteps.account_should_contains_email(member.getEmail());
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.ACCOUNTS, Release.VER_0_10_0})
    public void regular_member_cant_get_not_own_account_data_via_api() {
        // given
        TestMember firstMember = setupMemberSteps.create_member();
        TestMember secondMember = setupMemberSteps.create_member();
        make_rollback_after_test_case(setupMemberSteps.delete_member(firstMember));
        make_rollback_after_test_case(setupMemberSteps.delete_member(secondMember));
        authRestSteps.include_basic_auth_header_for_every_request(firstMember);

        // when
        memberAccountResourceSteps.get_member_account(secondMember.getMemberId());

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.GET_NOT_OWN_ACCOUNT);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.ACCOUNTS, Release.VER_0_10_0})
    public void get_account_for_not_existing_member_should_end_with_member_not_found_error() {
        // when
        authRestSteps.include_admin_basic_auth_header_for_every_request();
        memberAccountResourceSteps.get_member_account(1L);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.MEMBER_NOT_FOUND);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.ACCOUNTS, Release.VER_0_10_0})
    public void should_return_type_mismatch_error_when_provide_text_member_id_when_get_account() {
        // when
        authRestSteps.include_admin_basic_auth_header_for_every_request();
        memberAccountResourceSteps.get_member_account("aaa");

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.TYPE_MISMATCH);
        memberAccountResourceSteps.should_contain_error_detail_about_member_id_type_mismatch();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.ACCOUNTS, Release.VER_0_10_0})
    public void administrator_can_get_not_own_account_data_via_api() {
        // given
        TestMember member = setupMemberSteps.create_member();
        make_rollback_after_test_case(setupMemberSteps.delete_member(member));

        authRestSteps.include_admin_basic_auth_header_for_every_request();

        // when
        memberAccountResourceSteps.get_member_account(member.getMemberId());

        // then
        memberAccountResourceSteps.account_should_contains_email(member.getEmail());
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.ACCOUNTS, Release.VER_0_12_0})
    public void client_with_account_read_scope_can_get_account_data_via_api() {
        // given
        TestMember member = setupMemberSteps.create_member();
        make_rollback_after_test_case(setupMemberSteps.delete_member(member));

        TestOAuthClient client = setupOAuthSteps.create_client_with_scope(MEMBER_ACCOUNT_READ);
        make_rollback_after_test_case(setupOAuthSteps.delete_oauth_client(client));
        authRestSteps.authorize_every_request_with_oauth_client(client);

        // when
        memberAccountResourceSteps.get_member_account(member.getMemberId());

        // then
        memberAccountResourceSteps.account_should_contains_email(member.getEmail());
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.ACCOUNTS, Release.VER_0_12_0})
    public void client_with_account_write_scope_can_get_account_data_via_api() {
        // given
        TestMember member = setupMemberSteps.create_member();
        make_rollback_after_test_case(setupMemberSteps.delete_member(member));

        TestOAuthClient client = setupOAuthSteps.create_client_with_scope(MEMBER_ACCOUNT_READ_WRITE);
        make_rollback_after_test_case(setupOAuthSteps.delete_oauth_client(client));
        authRestSteps.authorize_every_request_with_oauth_client(client);

        // when
        memberAccountResourceSteps.get_member_account(member.getMemberId());

        // then
        memberAccountResourceSteps.account_should_contains_email(member.getEmail());
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.ACCOUNTS, Release.VER_0_12_0})
    public void client_without_account_scopes_cant_get_account_data_via_api() {
        // given
        TestMember member = setupMemberSteps.create_member();
        make_rollback_after_test_case(setupMemberSteps.delete_member(member));

        TestOAuthClient client = setupOAuthSteps.create_client_with_all_scopes_except(MEMBER_ACCOUNT_READ, MEMBER_ACCOUNT_READ_WRITE);
        make_rollback_after_test_case(setupOAuthSteps.delete_oauth_client(client));
        authRestSteps.authorize_every_request_with_oauth_client(client);

        // when
        memberAccountResourceSteps.get_member_account(member.getMemberId());

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.FORBIDDEN);
    }

}
