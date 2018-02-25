/*
 * Copyright (C) 2017 the original author or authors.
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
import org.jbb.e2e.serenity.rest.members.MemberPublicDto;
import org.jbb.e2e.serenity.rest.members.MemberResourceSteps;
import org.jbb.e2e.serenity.rest.members.RegistrationRequestDto;
import org.jbb.lib.restful.domain.ErrorInfo;
import org.junit.Test;

import static net.serenitybdd.rest.SerenityRest.then;

public class PutMemberProfileRestStories extends EndToEndRestStories {

    @Steps
    MemberProfileResourceSteps memberProfileResourceSteps;

    @Steps
    MemberResourceSteps memberResourceSteps;

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.PROFILE, Release.VER_0_10_0})
    public void guest_cannot_put_any_profile_data_via_api()
            throws Exception {
        // given
        register_and_mark_to_rollback("AccountTest");

        // when
        memberProfileResourceSteps.put_member_profile(
                memberResourceSteps.get_created_member_id().toString(),
                updateProfileDto(null)
        );

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.UNAUTHORIZED);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.PROFILE, Release.VER_0_10_0})
    public void regular_member_can_put_own_profile_data_via_api()
            throws Exception {
        // given
        register_and_mark_to_rollback("AccountTest");
        authRestSteps.include_basic_auth_header_for_every_request("AccountTest", "mysecretpass");

        // when
        memberProfileResourceSteps.put_member_profile(
                memberResourceSteps.get_created_member_id().toString(),
                updateProfileDto("new displayed name")
        );

        // then
        memberProfileResourceSteps.profile_should_contains_displayed_name("new displayed name");
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.PROFILE, Release.VER_0_10_0})
    public void regular_member_cant_put_own_profile_data_via_api_when_displayed_name_is_empty()
            throws Exception {
        // given
        register_and_mark_to_rollback("AccountTest");
        authRestSteps.include_basic_auth_header_for_every_request("AccountTest", "mysecretpass");

        // when
        memberProfileResourceSteps.put_member_profile(
                memberResourceSteps.get_created_member_id().toString(),
                updateProfileDto("")
        );

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.UPDATE_PROFILE_FAILED);
        memberProfileResourceSteps.should_contain_error_detail_about_empty_displayed_name();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.PROFILE, Release.VER_0_10_0})
    public void regular_member_cant_put_own_profile_data_via_api_when_displayed_name_is_too_short()
            throws Exception {
        // given
        register_and_mark_to_rollback("AccountTest");
        authRestSteps.include_basic_auth_header_for_every_request("AccountTest", "mysecretpass");

        // when
        memberProfileResourceSteps.put_member_profile(
                memberResourceSteps.get_created_member_id().toString(),
                updateProfileDto("aa")
        );

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.UPDATE_PROFILE_FAILED);
        memberProfileResourceSteps
                .should_contain_error_detail_about_invalid_displayed_name_length();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.PROFILE, Release.VER_0_10_0})
    public void regular_member_cant_put_own_profile_data_via_api_when_displayed_name_is_too_long()
            throws Exception {
        // given
        register_and_mark_to_rollback("AccountTest");
        authRestSteps.include_basic_auth_header_for_every_request("AccountTest", "mysecretpass");

        // when
        memberProfileResourceSteps.put_member_profile(
                memberResourceSteps.get_created_member_id().toString(),
                updateProfileDto("abcabcabcdabcabcabcdabcabcabcdabcabcabcdabcabcabcdabcabcabcd12345")
        );

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.UPDATE_PROFILE_FAILED);
        memberProfileResourceSteps
                .should_contain_error_detail_about_invalid_displayed_name_length();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.PROFILE, Release.VER_0_10_0})
    public void regular_member_cant_put_own_profile_data_via_api_when_displayed_name_is_busy()
            throws Exception {
        // given
        register_and_mark_to_rollback("AccountTest");
        String firstMemberId = memberResourceSteps.get_created_member_id().toString();
        register_and_mark_to_rollback("AccountTestTwo");
        authRestSteps.include_basic_auth_header_for_every_request("AccountTest", "mysecretpass");

        // when
        memberProfileResourceSteps.put_member_profile(firstMemberId,
                updateProfileDto("AccountTestTwo")
        );

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.UPDATE_PROFILE_FAILED);
        memberProfileResourceSteps.should_contain_error_detail_about_busy_displayed_name();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.PROFILE, Release.VER_0_10_0})
    public void regular_member_cant_put_not_own_profile_data_via_api()
            throws Exception {
        // given
        register_and_mark_to_rollback("AccountTest");
        String firstMemberId = memberResourceSteps.get_created_member_id().toString();
        register_and_mark_to_rollback("AccountTestTwo");

        authRestSteps.include_basic_auth_header_for_every_request("AccountTestTwo", "mysecretpass");

        // when
        memberProfileResourceSteps.put_member_profile(firstMemberId,
                updateProfileDto(null)
        );

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.UPDATE_NOT_OWN_PROFILE);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.PROFILE, Release.VER_0_10_0})
    public void administrator_can_put_not_own_account_data_via_api()
            throws Exception {
        // given
        register_and_mark_to_rollback("AccountTest");
        String memberId = memberResourceSteps.get_created_member_id().toString();

        authRestSteps.include_admin_basic_auth_header_for_every_request();

        // when
        memberProfileResourceSteps.put_member_profile(memberId,
                updateProfileDto("new displayed name")
        );

        // then
        memberProfileResourceSteps.profile_should_contains_displayed_name("new displayed name");
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.PROFILE, Release.VER_0_10_0})
    public void put_profile_for_not_existing_member_should_end_with_member_not_found_error()
            throws Exception {
        // when
        authRestSteps.include_admin_basic_auth_header_for_every_request();
        memberProfileResourceSteps.put_member_profile("1",
                updateProfileDto("new displayed name"));

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.MEMBER_NOT_FOUND);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.PROFILE, Release.VER_0_10_0})
    public void should_return_type_mismatch_error_when_provide_text_member_id_when_put_profile()
            throws Exception {
        // when
        authRestSteps.include_admin_basic_auth_header_for_every_request();
        memberProfileResourceSteps.put_member_profile("aaa",
                updateProfileDto(null));
        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.TYPE_MISMATCH);
        memberResourceSteps.should_contain_error_detail_about_member_id_type_mismatch();
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

    private UpdateProfileDto updateProfileDto(String newDisplayedName) {
        return UpdateProfileDto.builder()
                .displayedName(newDisplayedName)
                .build();
    }

}
