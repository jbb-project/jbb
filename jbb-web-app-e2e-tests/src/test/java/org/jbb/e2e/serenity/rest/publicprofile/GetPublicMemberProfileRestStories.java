/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.rest.publicprofile;

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

import static org.jbb.lib.commons.security.OAuthScope.MEMBER_READ;
import static org.jbb.lib.commons.security.OAuthScope.MEMBER_READ_CREATE;
import static org.jbb.lib.commons.security.OAuthScope.MEMBER_READ_DELETE;
import static org.jbb.lib.commons.security.OAuthScope.MEMBER_READ_WRITE;

public class GetPublicMemberProfileRestStories extends EndToEndRestStories {

    @Steps
    PublicMemberResourceSteps publicMemberResourceSteps;

    @Steps
    SetupMemberSteps setupMemberSteps;

    @Steps
    SetupOAuthSteps setupOAuthSteps;

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.PROFILE, Release.VER_0_10_0})
    public void regular_member_can_put_own_profile_data_via_api() {
        // given
        TestMember member = setupMemberSteps.create_member();
        make_rollback_after_test_case(setupMemberSteps.delete_member(member));

        // when
        publicMemberResourceSteps.get_public_profile(member.getMemberId());

        // then
        publicMemberResourceSteps.should_contain_displayed_name(member.getDisplayedName());
        publicMemberResourceSteps.should_contain_join_date_time();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.PROFILE, Release.VER_0_12_0})
    public void client_with_member_read_scope_can_get_public_profile_data() {
        // given
        TestMember member = setupMemberSteps.create_member();
        make_rollback_after_test_case(setupMemberSteps.delete_member(member));

        TestOAuthClient client = setupOAuthSteps.create_client_with_scope(MEMBER_READ);
        make_rollback_after_test_case(setupOAuthSteps.delete_oauth_client(client));
        authRestSteps.authorize_every_request_with_oauth_client(client);

        // when
        publicMemberResourceSteps.get_public_profile(member.getMemberId());

        // then
        publicMemberResourceSteps.should_contain_valid_public_profile();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.PROFILE, Release.VER_0_12_0})
    public void client_with_member_create_scope_can_get_public_profile_data() {
        // given
        TestMember member = setupMemberSteps.create_member();
        make_rollback_after_test_case(setupMemberSteps.delete_member(member));

        TestOAuthClient client = setupOAuthSteps.create_client_with_scope(MEMBER_READ_CREATE);
        make_rollback_after_test_case(setupOAuthSteps.delete_oauth_client(client));
        authRestSteps.authorize_every_request_with_oauth_client(client);

        // when
        publicMemberResourceSteps.get_public_profile(member.getMemberId());

        // then
        publicMemberResourceSteps.should_contain_valid_public_profile();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.PROFILE, Release.VER_0_12_0})
    public void client_with_member_delete_scope_can_get_public_profile_data() {
        // given
        TestMember member = setupMemberSteps.create_member();
        make_rollback_after_test_case(setupMemberSteps.delete_member(member));

        TestOAuthClient client = setupOAuthSteps.create_client_with_scope(MEMBER_READ_DELETE);
        make_rollback_after_test_case(setupOAuthSteps.delete_oauth_client(client));
        authRestSteps.authorize_every_request_with_oauth_client(client);

        // when
        publicMemberResourceSteps.get_public_profile(member.getMemberId());

        // then
        publicMemberResourceSteps.should_contain_valid_public_profile();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.PROFILE, Release.VER_0_12_0})
    public void client_with_member_write_scope_can_get_public_profile_data() {
        // given
        TestMember member = setupMemberSteps.create_member();
        make_rollback_after_test_case(setupMemberSteps.delete_member(member));

        TestOAuthClient client = setupOAuthSteps.create_client_with_scope(MEMBER_READ_WRITE);
        make_rollback_after_test_case(setupOAuthSteps.delete_oauth_client(client));
        authRestSteps.authorize_every_request_with_oauth_client(client);

        // when
        publicMemberResourceSteps.get_public_profile(member.getMemberId());

        // then
        publicMemberResourceSteps.should_contain_valid_public_profile();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.PROFILE, Release.VER_0_12_0})
    public void client_without_member_scopes_cannot_get_public_profile_data() {
        // given
        TestMember member = setupMemberSteps.create_member();
        make_rollback_after_test_case(setupMemberSteps.delete_member(member));

        TestOAuthClient client = setupOAuthSteps.create_client_with_all_scopes_except(
                MEMBER_READ, MEMBER_READ_CREATE, MEMBER_READ_DELETE, MEMBER_READ_WRITE);
        make_rollback_after_test_case(setupOAuthSteps.delete_oauth_client(client));
        authRestSteps.authorize_every_request_with_oauth_client(client);

        // when
        publicMemberResourceSteps.get_public_profile(member.getMemberId());

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.FORBIDDEN);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.PROFILE, Release.VER_0_10_0})
    public void get_public_profile_for_not_existing_member_should_end_with_member_not_found_error() {
        // when
        publicMemberResourceSteps.get_public_profile(1L);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.MEMBER_NOT_FOUND);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.PROFILE, Release.VER_0_10_0})
    public void should_return_type_mismatch_error_when_provide_text_member_id_when_get_public_profile() {
        // when
        publicMemberResourceSteps.get_public_profile("aaaa");

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.TYPE_MISMATCH);
        publicMemberResourceSteps.should_contain_error_detail_about_member_id_type_mismatch();
    }

}
