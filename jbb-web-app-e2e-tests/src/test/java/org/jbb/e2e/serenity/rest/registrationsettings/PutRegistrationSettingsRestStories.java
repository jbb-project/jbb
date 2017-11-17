/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.rest.registrationsettings;

import static net.serenitybdd.rest.SerenityRest.then;

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
import org.springframework.http.HttpStatus;

public class PutRegistrationSettingsRestStories extends EndToEndRestStories {

    @Steps
    RegistrationSettingsResourceSteps registrationSettingsResourceSteps;

    @Steps
    MemberResourceSteps memberResourceSteps;

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.REGISTRATION, Release.VER_0_10_0})
    public void put_registration_settings_without_email_duplication_allowed_value_is_impossible()
        throws Exception {
        // given
        authRestSteps.include_admin_basic_auth_header_for_every_request();

        RegistrationSettingsDto registrationSettings = RegistrationSettingsDto.builder()
            .emailDuplicationAllowed(null)
            .build();

        // when
        registrationSettingsResourceSteps.put_registration_settings(registrationSettings);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.VALIDATION_ERROR);
        registrationSettingsResourceSteps
            .should_contain_error_detail_about_null_email_duplication_allowed();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.REGISTRATION, Release.VER_0_10_0})
    public void put_registration_settings_with_invalid_email_duplication_allowed_value_is_impossible()
        throws Exception {
        // given
        authRestSteps.include_admin_basic_auth_header_for_every_request();

        RegistrationSettingsDto registrationSettings = RegistrationSettingsDto.builder()
            .emailDuplicationAllowed("tru")
            .build();

        // when
        registrationSettingsResourceSteps.put_registration_settings(registrationSettings);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.MESSAGE_NOT_READABLE);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.REGISTRATION, Release.VER_0_10_0})
    public void when_email_duplication_is_allowed_via_api_then_many_members_can_use_the_same_email()
        throws Exception {
        // given
        rollback_to_disallow_email_duplication();
        authRestSteps.include_admin_basic_auth_header_for_every_request();

        RegistrationSettingsDto registrationSettings = RegistrationSettingsDto.builder()
            .emailDuplicationAllowed(true)
            .build();

        // when
        registrationSettingsResourceSteps.put_registration_settings(registrationSettings);

        // then
        registrationSettingsResourceSteps.email_duplication_should_be_enabled();
        authRestSteps.remove_basic_auth_header_from_request();
        register_and_mark_to_rollback("testbedUser1");
        register_and_mark_to_rollback("testbedUser2");
        assertRestSteps.assert_response_status(HttpStatus.CREATED);
    }


    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.REGISTRATION, Release.VER_0_10_0})
    public void when_email_duplication_is_disallowed_via_api_then_many_members_cannot_use_the_same_email()
        throws Exception {
        // given
        rollback_to_disallow_email_duplication();
        authRestSteps.include_admin_basic_auth_header_for_every_request();

        RegistrationSettingsDto registrationSettings = RegistrationSettingsDto.builder()
            .emailDuplicationAllowed(false)
            .build();

        // when
        registrationSettingsResourceSteps.put_registration_settings(registrationSettings);

        // then
        registrationSettingsResourceSteps.email_duplication_should_be_disabled();
        authRestSteps.remove_basic_auth_header_from_request();
        register_and_mark_to_rollback("testbedUser3");
        memberResourceSteps.post_member(register("testbedUser4"));
        memberResourceSteps.should_contain_error_detail_about_busy_email();
    }

    private void rollback_to_disallow_email_duplication() {
        authRestSteps.include_admin_basic_auth_header_for_every_request();

        make_rollback_after_test_case((RollbackAction) () -> {
            authRestSteps.include_admin_basic_auth_header_for_every_request();
            registrationSettingsResourceSteps.put_registration_settings(
                RegistrationSettingsDto.builder()
                    .emailDuplicationAllowed(false)
                    .build()
            );
        });
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
            .email("thesame@mail.com")
            .password("mysecretpass")
            .build();
    }


}
