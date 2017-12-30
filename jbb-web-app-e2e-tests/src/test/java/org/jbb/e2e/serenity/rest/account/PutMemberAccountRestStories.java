/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.rest.account;

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

import static net.serenitybdd.rest.SerenityRest.then;

public class PutMemberAccountRestStories extends EndToEndRestStories {

    @Steps
    MemberAccountResourceSteps memberAccountResourceSteps;

    @Steps
    MemberResourceSteps memberResourceSteps;

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.ACCOUNTS, Release.VER_0_10_0})
    public void guest_cannot_put_any_account_data_via_api()
            throws Exception {
        // given
        register_and_mark_to_rollback("AccountTest");

        // when
        memberAccountResourceSteps
                .put_member_account(memberResourceSteps.get_created_member_id().toString(),
                        updateAccountDto("new@new.com", null));

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.UNAUTHORIZED);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.ACCOUNTS, Release.VER_0_10_0})
    public void regular_member_can_put_own_account_data_via_api_when_provide_correct_current_password()
            throws Exception {
        // given
        register_and_mark_to_rollback("AccountTest");
        authRestSteps.include_basic_auth_header_for_every_request("AccountTest", "mysecretpass");

        // when
        memberAccountResourceSteps
                .put_member_account(memberResourceSteps.get_created_member_id().toString(),
                        updateAccountDto(null, null));

        // then
        memberAccountResourceSteps.account_should_contains_email("accounttest@gmail.com");
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.ACCOUNTS, Release.VER_0_10_0})
    public void regular_member_cant_put_own_account_data_via_api_when_provide_incorrect_current_password()
            throws Exception {
        // given
        register_and_mark_to_rollback("AccountTest");
        authRestSteps.include_basic_auth_header_for_every_request("AccountTest", "mysecretpass");
        UpdateAccountDto updateAccountDto = updateAccountDto(null, null);
        updateAccountDto.setCurrentPassword("wrongpass");

        // when
        memberAccountResourceSteps
                .put_member_account(memberResourceSteps.get_created_member_id().toString(),
                        updateAccountDto);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.BAD_CREDENTIALS_WHEN_UPDATE_ACCOUNT);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.ACCOUNTS, Release.VER_0_10_0})
    public void regular_member_can_change_own_email_via_api_when_provide_correct_current_password()
            throws Exception {
        // given
        register_and_mark_to_rollback("AccountTest");
        authRestSteps.include_basic_auth_header_for_every_request("AccountTest", "mysecretpass");

        // when
        memberAccountResourceSteps
                .put_member_account(memberResourceSteps.get_created_member_id().toString(),
                        updateAccountDto("aa@bbb.com", null));

        // then
        memberAccountResourceSteps.account_should_contains_email("aa@bbb.com");
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.ACCOUNTS, Release.VER_0_10_0})
    public void regular_member_cant_change_own_email_via_api_to_invalid()
            throws Exception {
        // given
        register_and_mark_to_rollback("AccountTest");
        authRestSteps.include_basic_auth_header_for_every_request("AccountTest", "mysecretpass");

        // when
        memberAccountResourceSteps
                .put_member_account(memberResourceSteps.get_created_member_id().toString(),
                        updateAccountDto("aa(AT)bbb.com", null));

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.UPDATE_ACCOUNT_FAILED);
        memberAccountResourceSteps.should_contain_error_detail_about_invalid_email();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.ACCOUNTS, Release.VER_0_10_0})
    public void regular_member_cant_change_own_email_via_api_to_empty()
            throws Exception {
        // given
        register_and_mark_to_rollback("AccountTest");
        authRestSteps.include_basic_auth_header_for_every_request("AccountTest", "mysecretpass");

        // when
        memberAccountResourceSteps
                .put_member_account(memberResourceSteps.get_created_member_id().toString(),
                        updateAccountDto("", null));

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.UPDATE_ACCOUNT_FAILED);
        memberAccountResourceSteps.should_contain_error_detail_about_empty_email();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.ACCOUNTS, Release.VER_0_10_0})
    public void regular_member_cant_change_own_email_via_api_to_busy()
            throws Exception {
        // given
        register_and_mark_to_rollback("AccountTest");
        register_and_mark_to_rollback("AccountTestTwo");

        authRestSteps.include_basic_auth_header_for_every_request("AccountTestTwo", "mysecretpass");

        // when
        memberAccountResourceSteps
                .put_member_account(memberResourceSteps.get_created_member_id().toString(),
                        updateAccountDto("accounttest@gmail.com", null));

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.UPDATE_ACCOUNT_FAILED);
        memberAccountResourceSteps.should_contain_error_detail_about_busy_email();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.ACCOUNTS, Release.VER_0_10_0})
    public void regular_member_can_change_own_password_via_api_when_provide_correct_current_password()
            throws Exception {
        // given
        register_and_mark_to_rollback("AccountTest");
        String memberId = memberResourceSteps.get_created_member_id().toString();
        authRestSteps.include_basic_auth_header_for_every_request("AccountTest", "mysecretpass");

        // when
        memberAccountResourceSteps
                .put_member_account(memberId, updateAccountDto(null, "aaaa"));

        // then
        assertRestSteps.assert_response_status(HttpStatus.OK);

        // when
        authRestSteps.include_basic_auth_header_for_every_request("AccountTest", "aaaa");
        memberAccountResourceSteps.get_member_account(memberId);

        // then
        memberAccountResourceSteps.account_should_contains_email("accounttest@gmail.com");
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.ACCOUNTS, Release.VER_0_10_0})
    public void regular_member_cant_change_own_password_via_api_when_too_short()
            throws Exception {
        // given
        register_and_mark_to_rollback("AccountTest");
        authRestSteps.include_basic_auth_header_for_every_request("AccountTest", "mysecretpass");

        // when
        memberAccountResourceSteps
                .put_member_account(memberResourceSteps.get_created_member_id().toString(),
                        updateAccountDto(null, "a"));

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.UPDATE_ACCOUNT_FAILED);
        memberAccountResourceSteps.should_contain_error_detail_about_invalid_password_length();
    }


    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.ACCOUNTS, Release.VER_0_10_0})
    public void regular_member_cant_change_own_password_via_api_when_too_long()
            throws Exception {
        // given
        register_and_mark_to_rollback("AccountTest");
        authRestSteps.include_basic_auth_header_for_every_request("AccountTest", "mysecretpass");

        // when
        memberAccountResourceSteps
                .put_member_account(memberResourceSteps.get_created_member_id().toString(),
                        updateAccountDto(null, "aaaaaaadfvrgtrf4r3f4tf4rr4f4rfeedeeeee"));

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.UPDATE_ACCOUNT_FAILED);
        memberAccountResourceSteps.should_contain_error_detail_about_invalid_password_length();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.ACCOUNTS, Release.VER_0_10_0})
    public void regular_member_cant_put_not_own_account_data_via_api()
            throws Exception {
        // given
        register_and_mark_to_rollback("AccountTest");
        String firstMemberId = memberResourceSteps.get_created_member_id().toString();
        register_and_mark_to_rollback("AccountTestTwo");

        authRestSteps.include_basic_auth_header_for_every_request("AccountTestTwo", "mysecretpass");

        // when
        memberAccountResourceSteps
                .put_member_account(firstMemberId, updateAccountDto(null, null));

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.UPDATE_NOT_OWN_ACCOUNT);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.ACCOUNTS, Release.VER_0_10_0})
    public void administrator_can_put_not_own_account_data_via_api_without_password()
            throws Exception {
        // given
        register_and_mark_to_rollback("AccountTest");
        String regular_member_id = memberResourceSteps.get_created_member_id().toString();

        authRestSteps.include_admin_basic_auth_header_for_every_request();
        UpdateAccountDto updateAccountDto = updateAccountDto("aaaa@bbb.com", null);
        updateAccountDto.setCurrentPassword(null);

        // when
        memberAccountResourceSteps
                .put_member_account(regular_member_id, updateAccountDto);

        // then
        memberAccountResourceSteps.account_should_contains_email("aaaa@bbb.com");
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.ACCOUNTS, Release.VER_0_10_0})
    public void administrator_can_put_own_account_data_via_api_only_with_current_password()
            throws Exception {
        // given
        Long administrator_member_id = memberResourceSteps.get_administrator_member_id();
        authRestSteps.include_admin_basic_auth_header_for_every_request();
        UpdateAccountDto updateAccountDto = updateAccountDto(null, null);
        updateAccountDto.setCurrentPassword("administrator");

        // when
        memberAccountResourceSteps
                .put_member_account(administrator_member_id.toString(), updateAccountDto);

        // then
        assertRestSteps.assert_response_status(HttpStatus.OK);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.ACCOUNTS, Release.VER_0_10_0})
    public void administrator_cant_put_own_account_data_via_api_only_without_current_password()
            throws Exception {
        // given
        Long administrator_member_id = memberResourceSteps.get_administrator_member_id();
        authRestSteps.include_admin_basic_auth_header_for_every_request();
        UpdateAccountDto updateAccountDto = updateAccountDto(null, null);
        updateAccountDto.setCurrentPassword(null);

        // when
        memberAccountResourceSteps
                .put_member_account(administrator_member_id.toString(), updateAccountDto);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.BAD_CREDENTIALS_WHEN_UPDATE_ACCOUNT);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.ACCOUNTS, Release.VER_0_10_0})
    public void administrator_cant_put_own_account_data_via_api_only_with_invalid_current_password()
            throws Exception {
        // given
        Long administrator_member_id = memberResourceSteps.get_administrator_member_id();
        authRestSteps.include_admin_basic_auth_header_for_every_request();
        UpdateAccountDto updateAccountDto = updateAccountDto(null, null);
        updateAccountDto.setCurrentPassword("invalidpass");

        // when
        memberAccountResourceSteps
                .put_member_account(administrator_member_id.toString(), updateAccountDto);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.BAD_CREDENTIALS_WHEN_UPDATE_ACCOUNT);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.ACCOUNTS, Release.VER_0_10_0})
    public void put_account_for_not_existing_member_should_end_with_member_not_found_error()
            throws Exception {
        // when
        authRestSteps.include_admin_basic_auth_header_for_every_request();
        memberAccountResourceSteps.put_member_account("1", updateAccountDto(null, null));

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.MEMBER_NOT_FOUND);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.ACCOUNTS, Release.VER_0_10_0})
    public void should_return_type_mismatch_error_when_provide_text_member_id_when_put_account()
            throws Exception {
        // when
        authRestSteps.include_admin_basic_auth_header_for_every_request();
        memberAccountResourceSteps.put_member_account("aaa", updateAccountDto(null, null));

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

    private UpdateAccountDto updateAccountDto(String newEmail, String newPassword) {
        return UpdateAccountDto.builder()
                .currentPassword("mysecretpass")
                .email(newEmail)
                .newPassword(newPassword)
                .build();
    }

}
