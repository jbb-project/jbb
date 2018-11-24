/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.rest.board;

import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.WithTagValuesOf;

import org.jbb.e2e.serenity.Tags.Feature;
import org.jbb.e2e.serenity.Tags.Interface;
import org.jbb.e2e.serenity.Tags.Release;
import org.jbb.e2e.serenity.Tags.Type;
import org.jbb.e2e.serenity.rest.EndToEndRestStories;
import org.jbb.e2e.serenity.rest.commons.OAuthClient;
import org.jbb.e2e.serenity.rest.commons.TestMember;
import org.jbb.e2e.serenity.rest.members.SetupMemberSteps;
import org.jbb.e2e.serenity.rest.oauthclient.SetupOAuthSteps;
import org.jbb.lib.restful.domain.ErrorInfo;
import org.junit.Test;

import static org.jbb.lib.commons.security.OAuthScope.BOARD_READ;
import static org.jbb.lib.commons.security.OAuthScope.BOARD_READ_WRITE;

public class GetBoardRestStories extends EndToEndRestStories {

    @Steps
    BoardResourceSteps boardResourceSteps;

    @Steps
    SetupMemberSteps setupMemberSteps;

    @Steps
    SetupOAuthSteps setupOAuthSteps;

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.FORUM_MANAGEMENT, Release.VER_0_11_0})
    public void guest_can_get_board_structure_via_api() {
        // when
        boardResourceSteps.get_board();

        // then
        boardResourceSteps.should_contains_not_empty_board();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.FORUM_MANAGEMENT, Release.VER_0_11_0})
    public void member_can_get_board_structure_via_api() {
        // given
        TestMember member = setupMemberSteps.create_member();
        make_rollback_after_test_case(setupMemberSteps.delete_member(member));
        authRestSteps.include_basic_auth_header_for_every_request(member);

        // when
        boardResourceSteps.get_board();

        // then
        boardResourceSteps.should_contains_not_empty_board();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.FORUM_MANAGEMENT, Release.VER_0_11_0})
    public void administrator_can_get_board_structure_via_api() {
        // given
        authRestSteps.include_admin_basic_auth_header_for_every_request();

        // when
        boardResourceSteps.get_board();

        // then
        boardResourceSteps.should_contains_not_empty_board();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.FORUM_MANAGEMENT, Release.VER_0_12_0})
    public void client_with_board_read_scope_can_get_board_structure_via_api() {
        // given
        OAuthClient client = setupOAuthSteps.create_client_with_scope(BOARD_READ);
        make_rollback_after_test_case(setupOAuthSteps.delete_oauth_client(client));
        authRestSteps.authorize_every_request_with_oauth_client(client);

        // when
        boardResourceSteps.get_board();

        // then
        boardResourceSteps.should_contains_not_empty_board();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.FORUM_MANAGEMENT, Release.VER_0_12_0})
    public void client_with_board_write_scope_can_get_board_structure_via_api() {
        // given
        OAuthClient client = setupOAuthSteps.create_client_with_scope(BOARD_READ_WRITE);
        make_rollback_after_test_case(setupOAuthSteps.delete_oauth_client(client));
        authRestSteps.authorize_every_request_with_oauth_client(client);

        // when
        boardResourceSteps.get_board();

        // then
        boardResourceSteps.should_contains_not_empty_board();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.FORUM_MANAGEMENT, Release.VER_0_12_0})
    public void client_without_board_scopes_cannot_get_board_structure_via_api() {
        // given
        OAuthClient client = setupOAuthSteps.create_client_with_all_scopes_except(BOARD_READ, BOARD_READ_WRITE);
        make_rollback_after_test_case(setupOAuthSteps.delete_oauth_client(client));
        authRestSteps.authorize_every_request_with_oauth_client(client);

        // when
        boardResourceSteps.get_board();

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.FORBIDDEN);
    }

}
