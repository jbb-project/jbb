/*
 * Copyright (C) 2018 the original author or authors.
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
import org.jbb.e2e.serenity.rest.members.MemberPublicDto;
import org.jbb.e2e.serenity.rest.members.MemberResourceSteps;
import org.jbb.e2e.serenity.rest.members.RegistrationRequestDto;
import org.jbb.lib.restful.domain.ErrorInfo;
import org.junit.Test;

import static net.serenitybdd.rest.SerenityRest.then;
import static org.assertj.core.api.Assertions.assertThat;

public class PutBoardSettingsRestStories extends EndToEndRestStories {

    @Steps
    BoardSettingsResourceSteps boardSettingsResourceSteps;

    @Steps
    MemberResourceSteps memberResourceSteps;


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
        register_and_mark_to_rollback("AccountTest");
        authRestSteps.include_basic_auth_header_for_every_request("AccountTest", "mysecretpass");

        // when
        boardSettingsResourceSteps.put_board_settings(validBoardSettings());

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.FORBIDDEN);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.BOARD_SETTINGS, Release.VER_0_11_0})
    public void administrator_can_put_board_settings_via_api() {
        // given
        authRestSteps.include_admin_basic_auth_header_for_every_request();

        BoardSettingsDto boardSettings = boardSettingsResourceSteps.get_board_settings().as(BoardSettingsDto.class);
        make_rollback_after_test_case(() -> boardSettingsResourceSteps.put_board_settings(boardSettings));

        // when
        BoardSettingsDto newBoardSettings = boardSettingsResourceSteps.put_board_settings(validBoardSettings()).as(BoardSettingsDto.class);

        // then
        boardSettingsResourceSteps.should_contains_board_settings();
        assertThat(newBoardSettings).isEqualTo(validBoardSettings());
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.BOARD_SETTINGS, Release.VER_0_11_0})
    public void administrator_cant_update_board_settings_with_null_board_name_via_api() {
        // given
        authRestSteps.include_admin_basic_auth_header_for_every_request();

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
        authRestSteps.include_admin_basic_auth_header_for_every_request();

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
        authRestSteps.include_admin_basic_auth_header_for_every_request();

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
        authRestSteps.include_admin_basic_auth_header_for_every_request();

        BoardSettingsDto boardSettings = boardSettingsResourceSteps.get_board_settings().as(BoardSettingsDto.class);
        make_rollback_after_test_case(() -> boardSettingsResourceSteps.put_board_settings(boardSettings));

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
        authRestSteps.include_admin_basic_auth_header_for_every_request();

        BoardSettingsDto newBoardSettings = validBoardSettings();
        newBoardSettings.setBoardName(RandomStringUtils.randomAlphabetic(61));

        // when
        boardSettingsResourceSteps.put_board_settings(newBoardSettings);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.INVALID_BOARD_SETTINGS);
        boardSettingsResourceSteps.should_contain_error_detail_about_invalid_length_of_board_name();
    }

    private void register_and_mark_to_rollback(String displayedName) {
        memberResourceSteps.register_member_with_success(register(displayedName));
        remove_when_rollback();
    }

    private void remove_when_rollback() {
        MemberPublicDto createdMember = then().extract().as(MemberPublicDto.class);

        make_rollback_after_test_case(
                memberResourceSteps.delete_testbed_member(createdMember.getId())
        );
    }

    private RegistrationRequestDto register(String displayedName) {
        return RegistrationRequestDto.builder()
                .username(displayedName)
                .displayedName(displayedName)
                .email(displayedName.toLowerCase() + "@gmail.com")
                .password("mysecretpass")
                .build();
    }

    private BoardSettingsDto validBoardSettings() {
        BoardSettingsDto dto = new BoardSettingsDto();
        dto.setBoardName("jBB Board");
        return dto;
    }


}
