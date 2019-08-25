/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.rest.boardsettings;

import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.WithTagValuesOf;

import org.apache.commons.lang3.RandomStringUtils;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.jbb.lib.commons.security.OAuthScope.BOARD_SETTINGS_READ_WRITE;

public class PutBoardSettingsRestStories extends EndToEndRestStories {

    @Steps
    BoardSettingsResourceSteps boardSettingsResourceSteps;

    @Steps
    SetupMemberSteps setupMemberSteps;

    @Steps
    SetupOAuthSteps setupOAuthSteps;

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.BOARD_SETTINGS, Release.VER_0_11_0})
    public void guest_cannot_put_board_settings_via_api() {
        // when
        boardSettingsResourceSteps.put_board_settings(validBoardSettings());

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.UNAUTHORIZED);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.BOARD_SETTINGS, Release.VER_0_11_0})
    public void member_cannot_put_board_settings_via_api() {
        // given
        TestMember member = setupMemberSteps.create_member();
        make_rollback_after_test_case(setupMemberSteps.delete_member(member));
        authRestSteps.sign_in_for_every_request(member);

        // when
        boardSettingsResourceSteps.put_board_settings(validBoardSettings());

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.FORBIDDEN);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.BOARD_SETTINGS, Release.VER_0_11_0})
    public void administrator_can_put_board_settings_via_api() {
        // given
        authRestSteps.sign_in_as_admin_for_every_request();

        BoardSettingsDto boardSettings = boardSettingsResourceSteps.get_board_settings().as(BoardSettingsDto.class);
        make_rollback_after_test_case(restore(boardSettings));

        // when
        BoardSettingsDto newBoardSettings = boardSettingsResourceSteps.put_board_settings(validBoardSettings()).as(BoardSettingsDto.class);

        // then
        boardSettingsResourceSteps.should_contains_board_settings();
        assertThat(newBoardSettings).isEqualTo(validBoardSettings());
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.BOARD_SETTINGS, Release.VER_0_12_0})
    public void client_with_board_settings_write_scope_can_put_board_settings_via_api() {
        // given
        BoardSettingsDto boardSettings = boardSettingsResourceSteps.get_board_settings().as(BoardSettingsDto.class);
        make_rollback_after_test_case(restore(boardSettings));
        TestOAuthClient client = setupOAuthSteps.create_client_with_scope(BOARD_SETTINGS_READ_WRITE);
        make_rollback_after_test_case(setupOAuthSteps.delete_oauth_client(client));
        authRestSteps.authorize_every_request_with_oauth_client(client);

        // when
        boardSettingsResourceSteps.put_board_settings(validBoardSettings()).as(BoardSettingsDto.class);

        // then
        boardSettingsResourceSteps.should_contains_board_settings();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.BOARD_SETTINGS, Release.VER_0_12_0})
    public void client_without_board_settings_write_scope_cannot_put_board_settings_via_api() {
        // given
        TestOAuthClient client = setupOAuthSteps.create_client_with_all_scopes_except(BOARD_SETTINGS_READ_WRITE);
        make_rollback_after_test_case(setupOAuthSteps.delete_oauth_client(client));
        authRestSteps.authorize_every_request_with_oauth_client(client);

        // when
        boardSettingsResourceSteps.put_board_settings(validBoardSettings());

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.FORBIDDEN);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.BOARD_SETTINGS, Release.VER_0_11_0})
    public void administrator_cant_update_board_settings_with_null_board_name_via_api() {
        // given
        authRestSteps.sign_in_as_admin_for_every_request();

        BoardSettingsDto newBoardSettings = validBoardSettings();
        newBoardSettings.setBoardName(null);

        // when
        boardSettingsResourceSteps.put_board_settings(newBoardSettings);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.INVALID_BOARD_SETTINGS);
        boardSettingsResourceSteps.should_contain_error_detail_about_blank_board_name();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.BOARD_SETTINGS, Release.VER_0_11_0})
    public void administrator_cant_update_board_settings_with_empty_board_name_via_api() {
        // given
        authRestSteps.sign_in_as_admin_for_every_request();

        BoardSettingsDto newBoardSettings = validBoardSettings();
        newBoardSettings.setBoardName("");

        // when
        boardSettingsResourceSteps.put_board_settings(newBoardSettings);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.INVALID_BOARD_SETTINGS);
        boardSettingsResourceSteps.should_contain_error_detail_about_blank_board_name();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.BOARD_SETTINGS, Release.VER_0_11_0})
    public void administrator_cant_update_board_settings_with_blank_board_name_via_api() {
        // given
        authRestSteps.sign_in_as_admin_for_every_request();

        BoardSettingsDto newBoardSettings = validBoardSettings();
        newBoardSettings.setBoardName("     ");

        // when
        boardSettingsResourceSteps.put_board_settings(newBoardSettings);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.INVALID_BOARD_SETTINGS);
        boardSettingsResourceSteps.should_contain_error_detail_about_blank_board_name();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.BOARD_SETTINGS, Release.VER_0_11_0})
    public void administrator_can_put_board_settings_with_60_characters_board_name_via_api() {
        // given
        authRestSteps.sign_in_as_admin_for_every_request();

        BoardSettingsDto boardSettings = boardSettingsResourceSteps.get_board_settings().as(BoardSettingsDto.class);
        make_rollback_after_test_case(restore(boardSettings));

        BoardSettingsDto targetBoardSettings = validBoardSettings();
        targetBoardSettings.setBoardName(RandomStringUtils.randomAlphabetic(60));

        // when
        BoardSettingsDto newBoardSettings = boardSettingsResourceSteps.put_board_settings(targetBoardSettings).as(BoardSettingsDto.class);

        // then
        boardSettingsResourceSteps.should_contains_board_settings();
        assertThat(newBoardSettings).isEqualTo(targetBoardSettings);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.BOARD_SETTINGS, Release.VER_0_11_0})
    public void administrator_cant_update_board_settings_with_61_characters_board_name_via_api() {
        // given
        authRestSteps.sign_in_as_admin_for_every_request();

        BoardSettingsDto newBoardSettings = validBoardSettings();
        newBoardSettings.setBoardName(RandomStringUtils.randomAlphabetic(61));

        // when
        boardSettingsResourceSteps.put_board_settings(newBoardSettings);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.INVALID_BOARD_SETTINGS);
        boardSettingsResourceSteps.should_contain_error_detail_about_invalid_length_of_board_name();
    }

    private BoardSettingsDto validBoardSettings() {
        return BoardSettingsDto.builder()
                .boardName("jBB Board")
                .build();
    }

    private RollbackAction restore(BoardSettingsDto boardSettingsDto) {
        return () -> {
            authRestSteps.sign_in_as_admin_for_every_request();
            boardSettingsResourceSteps.put_board_settings(boardSettingsDto);
            authRestSteps.remove_authorization_headers_from_request();
        };
    }


}
