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
import org.jbb.e2e.serenity.rest.commons.TestOAuthClient;
import org.jbb.e2e.serenity.rest.oauthclient.SetupOAuthSteps;
import org.jbb.lib.restful.domain.ErrorInfo;
import org.junit.Test;

import static net.serenitybdd.rest.SerenityRest.then;
import static org.jbb.lib.commons.security.OAuthScope.MEMBER_READ_CREATE;
import static org.jbb.lib.commons.security.OAuthScope.MEMBER_READ_WRITE;

public class PostMembersRestStories extends EndToEndRestStories {

    @Steps
    MemberResourceSteps memberResourceSteps;

    @Steps
    SetupMemberSteps setupMemberSteps;

    @Steps
    SetupOAuthSteps setupOAuthSteps;

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.REGISTRATION, Release.VER_0_10_0})
    public void register_member_via_api_should_be_possible() {
        // given
        RegistrationRequestDto registrationRequest = correctRegistrationRequest();

        // when
        memberResourceSteps.post_member(registrationRequest);
        MemberPublicDto createdMember = then().extract().as(MemberPublicDto.class);

        make_rollback_after_test_case(setupMemberSteps.delete_member(createdMember.getId()));

        // then
        memberResourceSteps.created_member_should_have_id();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.REGISTRATION, Release.VER_0_12_0})
    public void client_with_member_create_scope_can_create_member_via_api() {
        // given
        RegistrationRequestDto registrationRequest = correctRegistrationRequest();

        TestOAuthClient client = setupOAuthSteps.create_client_with_scope(MEMBER_READ_CREATE);
        make_rollback_after_test_case(setupOAuthSteps.delete_oauth_client(client));
        authRestSteps.authorize_every_request_with_oauth_client(client);

        // when
        memberResourceSteps.post_member(registrationRequest);
        MemberPublicDto createdMember = then().extract().as(MemberPublicDto.class);

        make_rollback_after_test_case(setupMemberSteps.delete_member(createdMember.getId()));

        // then
        memberResourceSteps.created_member_should_have_id();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.REGISTRATION, Release.VER_0_12_0})
    public void client_with_member_write_scope_can_create_member_via_api() {
        // given
        RegistrationRequestDto registrationRequest = correctRegistrationRequest();

        TestOAuthClient client = setupOAuthSteps.create_client_with_scope(MEMBER_READ_WRITE);
        make_rollback_after_test_case(setupOAuthSteps.delete_oauth_client(client));
        authRestSteps.authorize_every_request_with_oauth_client(client);

        // when
        memberResourceSteps.post_member(registrationRequest);
        MemberPublicDto createdMember = then().extract().as(MemberPublicDto.class);

        make_rollback_after_test_case(setupMemberSteps.delete_member(createdMember.getId()));

        // then
        memberResourceSteps.created_member_should_have_id();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.REGISTRATION, Release.VER_0_12_0})
    public void client_without_member_create_or_write_scope_cannot_create_member_via_api() {
        // given
        RegistrationRequestDto registrationRequest = correctRegistrationRequest();

        TestOAuthClient client = setupOAuthSteps.create_client_with_all_scopes_except(MEMBER_READ_CREATE, MEMBER_READ_WRITE);
        make_rollback_after_test_case(setupOAuthSteps.delete_oauth_client(client));
        authRestSteps.authorize_every_request_with_oauth_client(client);

        // when
        memberResourceSteps.post_member(registrationRequest);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.FORBIDDEN);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.REGISTRATION, Release.VER_0_10_0})
    public void register_member_via_api_without_displayed_name_is_impossible() {
        // given
        RegistrationRequestDto registrationRequest = correctRegistrationRequest();
        registrationRequest.setDisplayedName(null);

        // when
        memberResourceSteps.post_member(registrationRequest);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.REGISTRATION_FAILED);
        memberResourceSteps.should_contain_error_detail_about_empty_displayed_name();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.REGISTRATION, Release.VER_0_10_0})
    public void register_member_via_api_with_empty_displayed_name_is_impossible() {
        // given
        RegistrationRequestDto registrationRequest = correctRegistrationRequest();
        registrationRequest.setDisplayedName("");

        // when
        memberResourceSteps.post_member(registrationRequest);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.REGISTRATION_FAILED);
        memberResourceSteps.should_contain_error_detail_about_invalid_displayed_name_length();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.REGISTRATION, Release.VER_0_10_0})
    public void register_member_via_api_with_too_short_displayed_name_is_impossible() {
        // given
        RegistrationRequestDto registrationRequest = correctRegistrationRequest();
        registrationRequest.setDisplayedName("aa");

        // when
        memberResourceSteps.post_member(registrationRequest);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.REGISTRATION_FAILED);
        memberResourceSteps.should_contain_error_detail_about_invalid_displayed_name_length();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.REGISTRATION, Release.VER_0_10_0})
    public void register_member_via_api_with_too_long_displayed_name_is_impossible() {
        // given
        RegistrationRequestDto registrationRequest = correctRegistrationRequest();
        registrationRequest
                .setDisplayedName("abcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghij12345");

        // when
        memberResourceSteps.post_member(registrationRequest);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.REGISTRATION_FAILED);
        memberResourceSteps.should_contain_error_detail_about_invalid_displayed_name_length();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.REGISTRATION, Release.VER_0_10_0})
    public void register_member_via_api_with_busy_displayed_name_is_impossible() {
        // given
        RegistrationRequestDto correctRegistrationRequest = correctRegistrationRequest();
        correctRegistrationRequest.setUsername("restsuccess");
        correctRegistrationRequest.setDisplayedName("REST success");
        correctRegistrationRequest.setEmail("rest@jbb.com");
        memberResourceSteps.register_member_with_success(correctRegistrationRequest);

        MemberPublicDto createdMember = then().extract().as(MemberPublicDto.class);
        make_rollback_after_test_case(setupMemberSteps.delete_member(createdMember.getId()));

        // when
        RegistrationRequestDto registrationRequest = correctRegistrationRequest();
        registrationRequest.setUsername("restsuccesschanged");
        registrationRequest.setDisplayedName("REST success");
        registrationRequest.setEmail("rest-changed@jbb.com");
        memberResourceSteps.post_member(registrationRequest);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.REGISTRATION_FAILED);
        memberResourceSteps.should_contain_error_detail_about_busy_displayed_name();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.REGISTRATION, Release.VER_0_10_0})
    public void register_member_via_api_without_email_is_impossible() {
        // given
        RegistrationRequestDto registrationRequest = correctRegistrationRequest();
        registrationRequest.setEmail(null);

        // when
        memberResourceSteps.post_member(registrationRequest);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.REGISTRATION_FAILED);
        memberResourceSteps.should_contain_error_detail_about_empty_email();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.REGISTRATION, Release.VER_0_10_0})
    public void register_member_via_api_with_empty_email_is_impossible() {
        // given
        RegistrationRequestDto registrationRequest = correctRegistrationRequest();
        registrationRequest.setEmail("");

        // when
        memberResourceSteps.post_member(registrationRequest);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.REGISTRATION_FAILED);
        memberResourceSteps.should_contain_error_detail_about_empty_email();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.REGISTRATION, Release.VER_0_10_0})
    public void register_member_via_api_with_incorrect_email_is_impossible() {
        // given
        RegistrationRequestDto registrationRequest = correctRegistrationRequest();
        registrationRequest.setEmail("aaaa(AT)dsd.com");

        // when
        memberResourceSteps.post_member(registrationRequest);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.REGISTRATION_FAILED);
        memberResourceSteps.should_contain_error_detail_about_invalid_email();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.REGISTRATION, Release.VER_0_10_0})
    public void register_member_via_api_with_busy_email_is_impossible() {
        // given
        RegistrationRequestDto correctRegistrationRequest = correctRegistrationRequest();
        correctRegistrationRequest.setUsername("restsuccess");
        correctRegistrationRequest.setDisplayedName("REST success");
        correctRegistrationRequest.setEmail("rest@jbb.com");
        memberResourceSteps.register_member_with_success(correctRegistrationRequest);

        MemberPublicDto createdMember = then().extract().as(MemberPublicDto.class);
        make_rollback_after_test_case(setupMemberSteps.delete_member(createdMember.getId()));

        // when
        RegistrationRequestDto registrationRequest = correctRegistrationRequest();
        registrationRequest.setUsername("restsuccesschanged");
        registrationRequest.setDisplayedName("REST success changed");
        registrationRequest.setEmail("rest@jbb.com");
        memberResourceSteps.post_member(registrationRequest);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.REGISTRATION_FAILED);
        memberResourceSteps.should_contain_error_detail_about_busy_email();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.REGISTRATION, Release.VER_0_10_0})
    public void register_member_via_api_without_username_is_impossible() {
        // given
        RegistrationRequestDto registrationRequest = correctRegistrationRequest();
        registrationRequest.setUsername(null);

        // when
        memberResourceSteps.post_member(registrationRequest);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.REGISTRATION_FAILED);
        memberResourceSteps.should_contain_error_detail_about_empty_username();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.REGISTRATION, Release.VER_0_10_0})
    public void register_member_via_api_with_empty_username_is_impossible() {
        // given
        RegistrationRequestDto registrationRequest = correctRegistrationRequest();
        registrationRequest.setUsername("");

        // when
        memberResourceSteps.post_member(registrationRequest);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.REGISTRATION_FAILED);
        memberResourceSteps.should_contain_error_detail_about_empty_username();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.REGISTRATION, Release.VER_0_10_0})
    public void register_member_via_api_with_blank_username_is_impossible() {
        // given
        RegistrationRequestDto registrationRequest = correctRegistrationRequest();
        registrationRequest.setUsername("   ");

        // when
        memberResourceSteps.post_member(registrationRequest);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.REGISTRATION_FAILED);
        memberResourceSteps.should_contain_error_detail_about_white_characters_in_username();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.REGISTRATION, Release.VER_0_10_0})
    public void register_member_via_api_with_too_short_username_is_impossible() {
        // given
        RegistrationRequestDto registrationRequest = correctRegistrationRequest();
        registrationRequest.setUsername("aa");

        // when
        memberResourceSteps.post_member(registrationRequest);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.REGISTRATION_FAILED);
        memberResourceSteps.should_contain_error_detail_about_invalid_username_size();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.REGISTRATION, Release.VER_0_10_0})
    public void register_member_via_api_with_too_long_username_is_impossible() {
        // given
        RegistrationRequestDto registrationRequest = correctRegistrationRequest();
        registrationRequest.setUsername("abcabcabc1abcabcabc11");

        // when
        memberResourceSteps.post_member(registrationRequest);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.REGISTRATION_FAILED);
        memberResourceSteps.should_contain_error_detail_about_invalid_username_size();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.REGISTRATION, Release.VER_0_10_0})
    public void register_member_via_api_with_busy_username_is_impossible() {
        // given
        RegistrationRequestDto correctRegistrationRequest = correctRegistrationRequest();
        correctRegistrationRequest.setUsername("restsuccess");
        correctRegistrationRequest.setDisplayedName("REST success");
        correctRegistrationRequest.setEmail("rest@jbb.com");
        memberResourceSteps.register_member_with_success(correctRegistrationRequest);

        MemberPublicDto createdMember = then().extract().as(MemberPublicDto.class);
        make_rollback_after_test_case(setupMemberSteps.delete_member(createdMember.getId()));

        // when
        RegistrationRequestDto registrationRequest = correctRegistrationRequest();
        registrationRequest.setUsername("restsuccess");
        registrationRequest.setDisplayedName("REST success changed");
        registrationRequest.setEmail("rest22@jbb.com");
        memberResourceSteps.post_member(registrationRequest);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.REGISTRATION_FAILED);
        memberResourceSteps.should_contain_error_detail_about_busy_username();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.REGISTRATION, Release.VER_0_10_0})
    public void register_member_via_api_without_password_is_impossible() {
        // given
        RegistrationRequestDto registrationRequest = correctRegistrationRequest();
        registrationRequest.setPassword(null);

        // when
        memberResourceSteps.post_member(registrationRequest);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.REGISTRATION_FAILED);
        memberResourceSteps.should_contain_error_detail_about_invalid_password_length();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.REGISTRATION, Release.VER_0_10_0})
    public void register_member_via_api_with_empty_password_is_impossible() {
        // given
        RegistrationRequestDto registrationRequest = correctRegistrationRequest();
        registrationRequest.setPassword("");

        // when
        memberResourceSteps.post_member(registrationRequest);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.REGISTRATION_FAILED);
        memberResourceSteps.should_contain_error_detail_about_invalid_password_length();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.REGISTRATION, Release.VER_0_10_0})
    public void register_member_via_api_with_too_short_password_is_impossible() {
        // given
        RegistrationRequestDto registrationRequest = correctRegistrationRequest();
        registrationRequest.setPassword("aa");

        // when
        memberResourceSteps.post_member(registrationRequest);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.REGISTRATION_FAILED);
        memberResourceSteps.should_contain_error_detail_about_invalid_password_length();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.REGISTRATION, Release.VER_0_10_0})
    public void register_member_via_api_with_too_long_password_is_impossible() {
        // given
        RegistrationRequestDto registrationRequest = correctRegistrationRequest();
        registrationRequest.setPassword("frnjtiremof49fj4f94frmdr4iufnti9dr3o");

        // when
        memberResourceSteps.post_member(registrationRequest);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.REGISTRATION_FAILED);
        memberResourceSteps.should_contain_error_detail_about_invalid_password_length();
    }

    private RegistrationRequestDto correctRegistrationRequest() {
        return RegistrationRequestDto.builder()
                .username("testrest2")
                .displayedName("Test Rest2")
                .password("testrest")
                .email("test2@rest.com")
                .build();
    }

}
