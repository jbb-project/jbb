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

public class GetMemberProfileRestStories extends EndToEndRestStories {

    @Steps
    MemberProfileResourceSteps memberProfileResourceSteps;

    @Steps
    MemberResourceSteps memberResourceSteps;

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.PROFILE, Release.VER_0_10_0})
    public void guest_cannot_get_any_profile_data_via_api()
        throws Exception {
        // given
        register_and_mark_to_rollback("AccountTest");

        // when
        memberProfileResourceSteps
            .get_member_profile(memberResourceSteps.get_created_member_id().toString());

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.UNAUTHORIZED);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.PROFILE, Release.VER_0_10_0})
    public void regular_member_can_get_own_profile_data_via_api()
        throws Exception {
        // given
        register_and_mark_to_rollback("AccountTest");
        authRestSteps.include_basic_auth_header_for_every_request("AccountTest", "mysecretpass");

        // when
        memberProfileResourceSteps
            .get_member_profile(memberResourceSteps.get_created_member_id().toString());

        // then
        memberProfileResourceSteps.profile_should_contains_displayed_name("AccountTest");
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.PROFILE, Release.VER_0_10_0})
    public void regular_member_cant_get_not_own_profile_data_via_api()
        throws Exception {
        // given
        register_and_mark_to_rollback("AccountTest");
        register_and_mark_to_rollback("AccountTestTwo");
        authRestSteps.include_basic_auth_header_for_every_request("AccountTest", "mysecretpass");

        // when
        memberProfileResourceSteps
            .get_member_profile(memberResourceSteps.get_created_member_id().toString());

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.GET_NOT_OWN_PROFILE);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.PROFILE, Release.VER_0_10_0})
    public void get_profile_for_not_existing_member_should_end_with_member_not_found_error()
        throws Exception {
        // when
        authRestSteps.include_admin_basic_auth_header_for_every_request();
        memberProfileResourceSteps.get_member_profile("1");

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.MEMBER_NOT_FOUND);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.PROFILE, Release.VER_0_10_0})
    public void should_return_type_mismatch_error_when_provide_text_member_id_when_get_profile()
        throws Exception {
        // when
        authRestSteps.include_admin_basic_auth_header_for_every_request();
        memberProfileResourceSteps.get_member_profile("aaa");

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.TYPE_MISMATCH);
        memberResourceSteps.should_contain_error_detail_about_member_id_type_mismatch();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.PROFILE, Release.VER_0_10_0})
    public void administrator_can_get_not_own_profile_data_via_api()
        throws Exception {
        // given
        register_and_mark_to_rollback("AccountTest");
        authRestSteps.include_admin_basic_auth_header_for_every_request();

        // when
        memberProfileResourceSteps
            .get_member_profile(memberResourceSteps.get_created_member_id().toString());

        // then
        memberProfileResourceSteps.profile_should_contains_displayed_name("AccountTest");
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


}
