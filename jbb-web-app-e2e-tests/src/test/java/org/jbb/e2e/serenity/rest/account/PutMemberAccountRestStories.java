/*
 * Copyright (C) 2019 the original author or authors.
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
import org.jbb.e2e.serenity.rest.commons.AuthRestSteps;
import org.jbb.e2e.serenity.rest.commons.TestMember;
import org.jbb.e2e.serenity.rest.commons.TestOAuthClient;
import org.jbb.e2e.serenity.rest.members.MemberResourceSteps;
import org.jbb.e2e.serenity.rest.members.SetupMemberSteps;
import org.jbb.e2e.serenity.rest.oauthclient.SetupOAuthSteps;
import org.jbb.lib.restful.domain.ErrorInfo;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import static org.jbb.lib.commons.security.OAuthScope.MEMBER_ACCOUNT_READ_WRITE;

public class PutMemberAccountRestStories extends EndToEndRestStories {

    @Steps
    MemberAccountResourceSteps memberAccountResourceSteps;

    @Steps
    MemberResourceSteps memberResourceSteps;

    @Steps
    SetupMemberSteps setupMemberSteps;

    @Steps
    SetupOAuthSteps setupOAuthSteps;

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.ACCOUNTS, Release.VER_0_10_0})
    public void guest_cannot_put_any_account_data_via_api() {
        // given
        TestMember member = setupMemberSteps.create_member();
        make_rollback_after_test_case(setupMemberSteps.delete_member(member));

        // when
        memberAccountResourceSteps.put_member_account(member.getMemberId(),
                        updateAccountDto("new@new.com", null));

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.UNAUTHORIZED);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.ACCOUNTS, Release.VER_0_10_0})
    public void regular_member_can_put_own_account_data_via_api_when_provide_correct_current_password() {
        // given
        TestMember member = setupMemberSteps.create_member();
        make_rollback_after_test_case(setupMemberSteps.delete_member(member));
        authRestSteps.sign_in_for_every_request(member);

        // when
        memberAccountResourceSteps.put_member_account(member.getMemberId(),
                        updateAccountDto(null, null));

        // then
        memberAccountResourceSteps.account_should_contains_email(member.getEmail());
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.ACCOUNTS, Release.VER_0_10_0})
    public void regular_member_cant_put_own_account_data_via_api_when_provide_incorrect_current_password() {
        // given
        TestMember member = setupMemberSteps.create_member();
        make_rollback_after_test_case(setupMemberSteps.delete_member(member));
        authRestSteps.sign_in_for_every_request(member);

        UpdateAccountDto updateAccountDto = updateAccountDto(null, null);
        updateAccountDto.setCurrentPassword("wrongpass");

        // when
        memberAccountResourceSteps.put_member_account(member.getMemberId(), updateAccountDto);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.BAD_CREDENTIALS_WHEN_UPDATE_ACCOUNT);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.ACCOUNTS, Release.VER_0_10_0})
    public void regular_member_can_change_own_email_via_api_when_provide_correct_current_password() {
        // given
        TestMember member = setupMemberSteps.create_member();
        make_rollback_after_test_case(setupMemberSteps.delete_member(member));
        authRestSteps.sign_in_for_every_request(member);

        // when
        memberAccountResourceSteps.put_member_account(member.getMemberId(),
                        updateAccountDto("aa@bbb.com", null));

        // then
        memberAccountResourceSteps.account_should_contains_email("aa@bbb.com");
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.ACCOUNTS, Release.VER_0_10_0})
    public void regular_member_cant_change_own_email_via_api_to_invalid() {
        // given
        TestMember member = setupMemberSteps.create_member();
        make_rollback_after_test_case(setupMemberSteps.delete_member(member));
        authRestSteps.sign_in_for_every_request(member);

        // when
        memberAccountResourceSteps.put_member_account(member.getMemberId(),
                        updateAccountDto("aa(AT)bbb.com", null));

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.UPDATE_ACCOUNT_FAILED);
        memberAccountResourceSteps.should_contain_error_detail_about_invalid_email();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.ACCOUNTS, Release.VER_0_10_0})
    public void regular_member_cant_change_own_email_via_api_to_empty() {
        // given
        TestMember member = setupMemberSteps.create_member();
        make_rollback_after_test_case(setupMemberSteps.delete_member(member));
        authRestSteps.sign_in_for_every_request(member);

        // when
        memberAccountResourceSteps.put_member_account(member.getMemberId(),
                        updateAccountDto("", null));

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.UPDATE_ACCOUNT_FAILED);
        memberAccountResourceSteps.should_contain_error_detail_about_empty_email();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.ACCOUNTS, Release.VER_0_10_0})
    public void regular_member_cant_change_own_email_via_api_to_busy() {
        // given
        TestMember firstMember = setupMemberSteps.create_member();
        TestMember secondMember = setupMemberSteps.create_member();
        make_rollback_after_test_case(setupMemberSteps.delete_member(firstMember));
        make_rollback_after_test_case(setupMemberSteps.delete_member(secondMember));
        authRestSteps.sign_in_for_every_request(secondMember);

        // when
        memberAccountResourceSteps.put_member_account(secondMember.getMemberId(),
                updateAccountDto(firstMember.getEmail(), null));

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.UPDATE_ACCOUNT_FAILED);
        memberAccountResourceSteps.should_contain_error_detail_about_busy_email();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.ACCOUNTS, Release.VER_0_10_0})
    public void regular_member_can_change_own_password_via_api_when_provide_correct_current_password() {
        // given
        TestMember member = setupMemberSteps.create_member();
        make_rollback_after_test_case(setupMemberSteps.delete_member(member));
        authRestSteps.sign_in_for_every_request(member);

        String newPassword = "aaaa";

        // when
        memberAccountResourceSteps.put_member_account(member.getMemberId(),
                updateAccountDto(null, newPassword));

        // then
        assertRestSteps.assert_response_status(HttpStatus.OK);

        // when
        authRestSteps.sign_in_for_every_request(member.getUsername(), newPassword);
        memberAccountResourceSteps.get_member_account(member.getMemberId());

        // then
        memberAccountResourceSteps.account_should_contains_email(member.getEmail());
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.ACCOUNTS, Release.VER_0_10_0})
    public void regular_member_cant_change_own_password_via_api_when_too_short() {
        // given
        TestMember member = setupMemberSteps.create_member();
        make_rollback_after_test_case(setupMemberSteps.delete_member(member));
        authRestSteps.sign_in_for_every_request(member);

        // when
        memberAccountResourceSteps.put_member_account(member.getMemberId(),
                        updateAccountDto(null, "a"));

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.UPDATE_ACCOUNT_FAILED);
        memberAccountResourceSteps.should_contain_error_detail_about_invalid_password_length();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.ACCOUNTS, Release.VER_0_10_0})
    public void regular_member_cant_change_own_password_via_api_when_too_long() {
        // given
        TestMember member = setupMemberSteps.create_member();
        make_rollback_after_test_case(setupMemberSteps.delete_member(member));
        authRestSteps.sign_in_for_every_request(member);

        // when
        memberAccountResourceSteps.put_member_account(member.getMemberId(),
                        updateAccountDto(null, "aaaaaaadfvrgtrf4r3f4tf4rr4f4rfeedeeeee"));

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.UPDATE_ACCOUNT_FAILED);
        memberAccountResourceSteps.should_contain_error_detail_about_invalid_password_length();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.ACCOUNTS, Release.VER_0_10_0})
    public void regular_member_cant_put_not_own_account_data_via_api() {
        // given
        TestMember firstMember = setupMemberSteps.create_member();
        TestMember secondMember = setupMemberSteps.create_member();
        make_rollback_after_test_case(setupMemberSteps.delete_member(firstMember));
        make_rollback_after_test_case(setupMemberSteps.delete_member(secondMember));
        authRestSteps.sign_in_for_every_request(secondMember);

        // when
        memberAccountResourceSteps.put_member_account(firstMember.getMemberId(),
                updateAccountDto(null, null));

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.UPDATE_NOT_OWN_ACCOUNT);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.ACCOUNTS, Release.VER_0_10_0})
    public void administrator_can_put_not_own_account_data_via_api_without_password() {
        // given
        TestMember member = setupMemberSteps.create_member();
        make_rollback_after_test_case(setupMemberSteps.delete_member(member));

        authRestSteps.sign_in_as_admin_for_every_request();
        UpdateAccountDto updateAccountDto = updateAccountDto("aaaa@bbb.com", null);
        updateAccountDto.setCurrentPassword(null);

        // when
        memberAccountResourceSteps.put_member_account(member.getMemberId(), updateAccountDto);

        // then
        memberAccountResourceSteps.account_should_contains_email("aaaa@bbb.com");
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.ACCOUNTS, Release.VER_0_10_0})
    public void administrator_can_put_own_account_data_via_api_only_with_current_password() {
        // given
        Long administratorId = memberResourceSteps.get_administrator_member_id();
        authRestSteps.sign_in_as_admin_for_every_request();

        UpdateAccountDto updateAccountDto = updateAccountDto(null, null);
        updateAccountDto.setCurrentPassword(AuthRestSteps.ADMIN_PASSWORD);

        // when
        memberAccountResourceSteps.put_member_account(administratorId, updateAccountDto);

        // then
        assertRestSteps.assert_response_status(HttpStatus.OK);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.ACCOUNTS, Release.VER_0_10_0})
    public void administrator_cant_put_own_account_data_via_api_only_without_current_password() {
        // given
        Long administratorId = memberResourceSteps.get_administrator_member_id();
        authRestSteps.sign_in_as_admin_for_every_request();

        UpdateAccountDto updateAccountDto = updateAccountDto(null, null);
        updateAccountDto.setCurrentPassword(null);

        // when
        memberAccountResourceSteps.put_member_account(administratorId, updateAccountDto);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.BAD_CREDENTIALS_WHEN_UPDATE_ACCOUNT);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.ACCOUNTS, Release.VER_0_10_0})
    public void administrator_cant_put_own_account_data_via_api_only_with_invalid_current_password() {
        // given
        Long administratorId = memberResourceSteps.get_administrator_member_id();
        authRestSteps.sign_in_as_admin_for_every_request();

        UpdateAccountDto updateAccountDto = updateAccountDto(null, null);
        updateAccountDto.setCurrentPassword("invalidpass");

        // when
        memberAccountResourceSteps.put_member_account(administratorId, updateAccountDto);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.BAD_CREDENTIALS_WHEN_UPDATE_ACCOUNT);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.ACCOUNTS, Release.VER_0_10_0})
    public void put_account_for_not_existing_member_should_end_with_member_not_found_error() {
        // when
        authRestSteps.sign_in_as_admin_for_every_request();
        memberAccountResourceSteps.put_member_account(1L, updateAccountDto(null, null));

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.MEMBER_NOT_FOUND);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.ACCOUNTS, Release.VER_0_10_0})
    public void should_return_type_mismatch_error_when_provide_text_member_id_when_put_account() {
        // when
        authRestSteps.sign_in_as_admin_for_every_request();
        memberAccountResourceSteps.put_member_account("aaa", updateAccountDto(null, null));

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.TYPE_MISMATCH);
        memberResourceSteps.should_contain_error_detail_about_member_id_type_mismatch();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.ACCOUNTS, Release.VER_0_12_0})
    public void client_with_account_write_scope_can_update_any_account_via_api() {
        // given
        TestMember member = setupMemberSteps.create_member();
        make_rollback_after_test_case(setupMemberSteps.delete_member(member));

        TestOAuthClient client = setupOAuthSteps.create_client_with_scope(MEMBER_ACCOUNT_READ_WRITE);
        make_rollback_after_test_case(setupOAuthSteps.delete_oauth_client(client));
        authRestSteps.authorize_every_request_with_oauth_client(client);

        // when
        memberAccountResourceSteps.put_member_account(member.getMemberId(),
                updateAccountDto("aa@bbb.com", null));

        // then
        memberAccountResourceSteps.account_should_contains_email("aa@bbb.com");
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.ACCOUNTS, Release.VER_0_12_0})
    public void client_without_account_write_scope_cant_update_accounts_via_api() {
        // given
        TestMember member = setupMemberSteps.create_member();
        make_rollback_after_test_case(setupMemberSteps.delete_member(member));

        TestOAuthClient client = setupOAuthSteps.create_client_with_all_scopes_except(MEMBER_ACCOUNT_READ_WRITE);
        make_rollback_after_test_case(setupOAuthSteps.delete_oauth_client(client));
        authRestSteps.authorize_every_request_with_oauth_client(client);

        // when
        memberAccountResourceSteps.put_member_account(member.getMemberId(),
                updateAccountDto("aa@bbb.com", null));

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.FORBIDDEN);
    }

    private UpdateAccountDto updateAccountDto(String newEmail, String newPassword) {
        return UpdateAccountDto.builder()
                .currentPassword(SetupMemberSteps.DEFAULT_TEST_PASSWORD)
                .email(newEmail)
                .newPassword(newPassword)
                .build();
    }

}
