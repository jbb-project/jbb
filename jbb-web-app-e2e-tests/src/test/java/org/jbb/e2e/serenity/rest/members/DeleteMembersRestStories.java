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

import org.jbb.e2e.serenity.Tags.Feature;
import org.jbb.e2e.serenity.Tags.Interface;
import org.jbb.e2e.serenity.Tags.Release;
import org.jbb.e2e.serenity.Tags.Type;
import org.jbb.e2e.serenity.rest.EndToEndRestStories;
import org.jbb.e2e.serenity.rest.commons.TestMember;
import org.jbb.e2e.serenity.rest.commons.TestOAuthClient;
import org.jbb.e2e.serenity.rest.oauthclient.SetupOAuthSteps;
import org.jbb.lib.restful.domain.ErrorInfo;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import static org.jbb.lib.commons.security.OAuthScope.MEMBER_READ_DELETE;
import static org.jbb.lib.commons.security.OAuthScope.MEMBER_READ_WRITE;

public class DeleteMembersRestStories extends EndToEndRestStories {

    @Steps
    MemberResourceSteps memberResourceSteps;

    @Steps
    SetupMemberSteps setupMemberSteps;

    @Steps
    SetupOAuthSteps setupOAuthSteps;

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.GENERAL, Release.VER_0_10_0})
    public void guest_cannot_delete_members_via_api() {
        // given
        TestMember member = setupMemberSteps.create_member();
        make_rollback_after_test_case(setupMemberSteps.delete_member(member));

        // when
        memberResourceSteps.delete_member(member.getMemberId());

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.UNAUTHORIZED);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.GENERAL, Release.VER_0_10_0})
    public void regular_members_cannot_delete_members_via_api() {
        // given
        TestMember firstMember = setupMemberSteps.create_member();
        TestMember secondMember = setupMemberSteps.create_member();
        make_rollback_after_test_case(setupMemberSteps.delete_member(firstMember));
        make_rollback_after_test_case(setupMemberSteps.delete_member(secondMember));

        authRestSteps.include_basic_auth_header_for_every_request(firstMember);

        // when
        memberResourceSteps.delete_member(secondMember.getMemberId());

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.FORBIDDEN);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.GENERAL, Release.VER_0_10_0})
    public void administrator_can_delete_members_via_api() {
        // given
        TestMember memberToRemove = setupMemberSteps.create_member();

        // when
        authRestSteps.include_admin_basic_auth_header_for_every_request();
        memberResourceSteps.delete_member(memberToRemove.getMemberId());

        // then
        assertRestSteps.assert_response_status(HttpStatus.NO_CONTENT);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.GENERAL, Release.VER_0_12_0})
    public void client_with_member_delete_scope_can_delete_members_via_api() {
        // given
        TestMember memberToRemove = setupMemberSteps.create_member();

        TestOAuthClient client = setupOAuthSteps.create_client_with_scope(MEMBER_READ_DELETE);
        make_rollback_after_test_case(setupOAuthSteps.delete_oauth_client(client));
        authRestSteps.authorize_every_request_with_oauth_client(client);

        // when
        memberResourceSteps.delete_member(memberToRemove.getMemberId());

        // then
        assertRestSteps.assert_response_status(HttpStatus.NO_CONTENT);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.GENERAL, Release.VER_0_12_0})
    public void client_with_member_write_scope_can_delete_members_via_api() {
        // given
        TestMember memberToRemove = setupMemberSteps.create_member();

        TestOAuthClient client = setupOAuthSteps.create_client_with_scope(MEMBER_READ_WRITE);
        make_rollback_after_test_case(setupOAuthSteps.delete_oauth_client(client));
        authRestSteps.authorize_every_request_with_oauth_client(client);

        // when
        memberResourceSteps.delete_member(memberToRemove.getMemberId());

        // then
        assertRestSteps.assert_response_status(HttpStatus.NO_CONTENT);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.GENERAL, Release.VER_0_12_0})
    public void client_without_member_delete_or_write_scope_cannot_delete_members_via_api() {
        // given
        TestMember memberToRemove = setupMemberSteps.create_member();
        make_rollback_after_test_case(setupMemberSteps.delete_member(memberToRemove));

        TestOAuthClient client = setupOAuthSteps.create_client_with_all_scopes_except(MEMBER_READ_DELETE, MEMBER_READ_WRITE);
        make_rollback_after_test_case(setupOAuthSteps.delete_oauth_client(client));
        authRestSteps.authorize_every_request_with_oauth_client(client);

        // when
        memberResourceSteps.delete_member(memberToRemove.getMemberId());

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.FORBIDDEN);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.GENERAL, Release.VER_0_10_0})
    public void delete_not_existing_member_should_end_with_member_not_found_error() {
        // when
        authRestSteps.include_admin_basic_auth_header_for_every_request();
        memberResourceSteps.delete_member(1L);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.MEMBER_NOT_FOUND);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.GENERAL, Release.VER_0_10_0})
    public void should_return_type_mismatch_error_when_provide_text_member_id_when_delete() {
        // when
        authRestSteps.include_admin_basic_auth_header_for_every_request();
        memberResourceSteps.delete_member("aaa");

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.TYPE_MISMATCH);
        memberResourceSteps.should_contain_error_detail_about_member_id_type_mismatch();
    }

}
