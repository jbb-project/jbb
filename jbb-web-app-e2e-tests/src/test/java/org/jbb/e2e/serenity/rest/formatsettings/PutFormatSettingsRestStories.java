/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.rest.formatsettings;

import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.WithTagValuesOf;

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

public class PutFormatSettingsRestStories extends EndToEndRestStories {

    @Steps
    FormatSettingsResourceSteps formatSettingsResourceSteps;

    @Steps
    MemberResourceSteps memberResourceSteps;

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.BOARD_SETTINGS, Release.VER_0_11_0})
    public void guest_cannot_put_format_settings_via_api() {
        // when
        formatSettingsResourceSteps.put_format_settings(validFormatSettings());

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.UNAUTHORIZED);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.BOARD_SETTINGS, Release.VER_0_11_0})
    public void member_cannot_put_format_settings_via_api() {
        // given
        register_and_mark_to_rollback("AccountTest");
        authRestSteps.include_basic_auth_header_for_every_request("AccountTest", "mysecretpass");

        // when
        formatSettingsResourceSteps.put_format_settings(validFormatSettings());

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.FORBIDDEN);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.BOARD_SETTINGS, Release.VER_0_11_0})
    public void administrator_can_put_format_settings_via_api() {
        // given
        authRestSteps.include_admin_basic_auth_header_for_every_request();

        FormatSettingsDto formatSettings = formatSettingsResourceSteps.get_format_settings().as(FormatSettingsDto.class);
        make_rollback_after_test_case(() -> formatSettingsResourceSteps.put_format_settings(formatSettings));

        // when
        FormatSettingsDto newFormatSettings = formatSettingsResourceSteps.put_format_settings(validFormatSettings()).as(FormatSettingsDto.class);

        // then
        formatSettingsResourceSteps.should_contains_format_settings();
        assertThat(newFormatSettings).isEqualTo(validFormatSettings());
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.BOARD_SETTINGS, Release.VER_0_11_0})
    public void administrator_cant_update_format_settings_with_null_date_format_via_api() {
        // given
        authRestSteps.include_admin_basic_auth_header_for_every_request();

        FormatSettingsDto newFormatSettings = validFormatSettings();
        newFormatSettings.setDateFormat(null);

        // when
        formatSettingsResourceSteps.put_format_settings(newFormatSettings);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.INVALID_FORMAT_SETTINGS);
        formatSettingsResourceSteps.should_contain_error_detail_about_empty_date_format();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.BOARD_SETTINGS, Release.VER_0_11_0})
    public void administrator_cant_update_format_settings_with_invalid_date_format_via_api() {
        // given
        authRestSteps.include_admin_basic_auth_header_for_every_request();

        FormatSettingsDto newFormatSettings = validFormatSettings();
        newFormatSettings.setDateFormat("DLdlo HH:RR lor");

        // when
        formatSettingsResourceSteps.put_format_settings(newFormatSettings);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.INVALID_FORMAT_SETTINGS);
        formatSettingsResourceSteps.should_contain_error_detail_about_invalid_date_format();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.BOARD_SETTINGS, Release.VER_0_11_0})
    public void administrator_cant_update_format_settings_with_null_duration_format_via_api() {
        // given
        authRestSteps.include_admin_basic_auth_header_for_every_request();

        FormatSettingsDto newFormatSettings = validFormatSettings();
        newFormatSettings.setDurationFormat(null);

        // when
        formatSettingsResourceSteps.put_format_settings(newFormatSettings);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.INVALID_FORMAT_SETTINGS);
        formatSettingsResourceSteps.should_contain_error_detail_about_empty_duration_format();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.BOARD_SETTINGS, Release.VER_0_11_0})
    public void administrator_cant_update_format_settings_with_invalid_duration_format_via_api() {
        // given
        authRestSteps.include_admin_basic_auth_header_for_every_request();

        FormatSettingsDto newFormatSettings = validFormatSettings();
        newFormatSettings.setDurationFormat("DLdlo HH:RR lor");

        // when
        formatSettingsResourceSteps.put_format_settings(newFormatSettings);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.INVALID_FORMAT_SETTINGS);
        formatSettingsResourceSteps.should_contain_error_detail_about_invalid_duration_format();
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

    private FormatSettingsDto validFormatSettings() {
        FormatSettingsDto dto = new FormatSettingsDto();
        dto.setDateFormat("dd/MM/yyyy HH:mm:ss");
        dto.setDurationFormat("HH:mm:ss");
        return dto;
    }

}
