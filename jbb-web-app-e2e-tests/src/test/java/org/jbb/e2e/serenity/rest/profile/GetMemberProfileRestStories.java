/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.rest.profile;

import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.WithTagValuesOf;

import org.jbb.e2e.serenity.Tags.Feature;
import org.jbb.e2e.serenity.Tags.Interface;
import org.jbb.e2e.serenity.Tags.Release;
import org.jbb.e2e.serenity.Tags.Type;
import org.jbb.e2e.serenity.rest.EndToEndRestStories;
import org.jbb.e2e.serenity.rest.commons.TestMember;
import org.jbb.e2e.serenity.rest.commons.TestOAuthClient;
import org.jbb.e2e.serenity.rest.members.MemberResourceSteps;
import org.jbb.e2e.serenity.rest.members.SetupMemberSteps;
import org.jbb.e2e.serenity.rest.oauthclient.SetupOAuthSteps;
import org.jbb.lib.restful.domain.ErrorInfo;
import org.junit.Test;

import static org.jbb.e2e.serenity.rest.commons.AuthRestSteps.ADMIN_USERNAME;
import static org.jbb.lib.commons.security.OAuthScope.MEMBER_PROFILE_READ;
import static org.jbb.lib.commons.security.OAuthScope.MEMBER_PROFILE_READ_WRITE;

public class GetMemberProfileRestStories extends EndToEndRestStories {

    @Steps
    MemberProfileResourceSteps memberProfileResourceSteps;

    @Steps
    MemberResourceSteps memberResourceSteps;

    @Steps
    SetupMemberSteps setupMemberSteps;

    @Steps
    SetupOAuthSteps setupOAuthSteps;

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.PROFILE, Release.VER_0_10_0})
    public void guest_cannot_get_any_profile_data_via_api() {
        // given
        TestMember member = setupMemberSteps.create_member();
        make_rollback_after_test_case(setupMemberSteps.delete_member(member));

        // when
        memberProfileResourceSteps.get_member_profile(member.getMemberId());

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.UNAUTHORIZED);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.PROFILE, Release.VER_0_10_0})
    public void regular_member_can_get_own_profile_data_via_api() {
        // given
        TestMember member = setupMemberSteps.create_member();
        make_rollback_after_test_case(setupMemberSteps.delete_member(member));
        authRestSteps.sign_in_for_every_request(member);

        // when
        memberProfileResourceSteps.get_member_profile(member.getMemberId());

        // then
        memberProfileResourceSteps.profile_should_contains_username(member.getUsername());
        memberProfileResourceSteps.profile_should_contains_displayed_name(member.getDisplayedName());
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.PROFILE, Release.VER_0_10_0})
    public void regular_member_cant_get_not_own_profile_data_via_api() {
        // given
        TestMember firstMember = setupMemberSteps.create_member();
        TestMember secondMember = setupMemberSteps.create_member();

        make_rollback_after_test_case(setupMemberSteps.delete_member(firstMember));
        make_rollback_after_test_case(setupMemberSteps.delete_member(secondMember));

        authRestSteps.sign_in_for_every_request(firstMember);

        // when
        memberProfileResourceSteps.get_member_profile(secondMember.getMemberId());

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.GET_NOT_OWN_PROFILE);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.PROFILE, Release.VER_0_10_0})
    public void get_profile_for_not_existing_member_should_end_with_member_not_found_error() {
        // when
        authRestSteps.sign_in_as_admin_for_every_request();
        memberProfileResourceSteps.get_member_profile(1L);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.MEMBER_NOT_FOUND);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.PROFILE, Release.VER_0_10_0})
    public void should_return_type_mismatch_error_when_provide_text_member_id_when_get_profile() {
        // when
        authRestSteps.sign_in_as_admin_for_every_request();
        memberProfileResourceSteps.get_member_profile("aaa");

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.TYPE_MISMATCH);
        memberResourceSteps.should_contain_error_detail_about_member_id_type_mismatch();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.PROFILE, Release.VER_0_10_0})
    public void administrator_can_get_not_own_profile_data_via_api() {
        // given
        TestMember member = setupMemberSteps.create_member();
        make_rollback_after_test_case(setupMemberSteps.delete_member(member));
        authRestSteps.sign_in_as_admin_for_every_request();

        // when
        memberProfileResourceSteps.get_member_profile(member.getMemberId());

        // then
        memberProfileResourceSteps.profile_should_contains_username(member.getUsername());
        memberProfileResourceSteps.profile_should_contains_displayed_name(member.getDisplayedName());
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.PROFILE, Release.VER_0_10_0})
    public void administrator_can_get_own_profile_data_via_api() {
        // given
        Long administratorId = memberResourceSteps.get_administrator_member_id();
        authRestSteps.sign_in_as_admin_for_every_request();

        // when
        memberProfileResourceSteps.get_member_profile(administratorId);

        // then
        memberProfileResourceSteps.profile_should_contains_username(ADMIN_USERNAME);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.PROFILE, Release.VER_0_12_0})
    public void client_with_profile_read_scope_can_get_profile_data_via_api() {
        // given
        TestMember member = setupMemberSteps.create_member();
        make_rollback_after_test_case(setupMemberSteps.delete_member(member));

        TestOAuthClient client = setupOAuthSteps.create_client_with_scope(MEMBER_PROFILE_READ);
        make_rollback_after_test_case(setupOAuthSteps.delete_oauth_client(client));
        authRestSteps.authorize_every_request_with_oauth_client(client);

        // when
        memberProfileResourceSteps.get_member_profile(member.getMemberId());

        // then
        memberProfileResourceSteps.profile_should_contains_username(member.getUsername());
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.PROFILE, Release.VER_0_12_0})
    public void client_with_profile_write_scope_can_get_profile_data_via_api() {
        // given
        TestMember member = setupMemberSteps.create_member();
        make_rollback_after_test_case(setupMemberSteps.delete_member(member));

        TestOAuthClient client = setupOAuthSteps.create_client_with_scope(MEMBER_PROFILE_READ_WRITE);
        make_rollback_after_test_case(setupOAuthSteps.delete_oauth_client(client));
        authRestSteps.authorize_every_request_with_oauth_client(client);

        // when
        memberProfileResourceSteps.get_member_profile(member.getMemberId());

        // then
        memberProfileResourceSteps.profile_should_contains_username(member.getUsername());
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.PROFILE, Release.VER_0_12_0})
    public void client_without_profile_scopes_cannot_get_profile_data_via_api() {
        // given
        TestMember member = setupMemberSteps.create_member();
        make_rollback_after_test_case(setupMemberSteps.delete_member(member));

        TestOAuthClient client = setupOAuthSteps.create_client_with_all_scopes_except(MEMBER_PROFILE_READ, MEMBER_PROFILE_READ_WRITE);
        make_rollback_after_test_case(setupOAuthSteps.delete_oauth_client(client));
        authRestSteps.authorize_every_request_with_oauth_client(client);

        // when
        memberProfileResourceSteps.get_member_profile(member.getMemberId());

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.FORBIDDEN);
    }

}
