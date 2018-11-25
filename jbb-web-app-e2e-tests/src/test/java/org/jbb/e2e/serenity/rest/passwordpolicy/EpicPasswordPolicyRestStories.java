/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.rest.passwordpolicy;

import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.WithTagValuesOf;

import org.jbb.e2e.serenity.Tags.Feature;
import org.jbb.e2e.serenity.Tags.Interface;
import org.jbb.e2e.serenity.Tags.Release;
import org.jbb.e2e.serenity.Tags.Type;
import org.jbb.e2e.serenity.rest.EndToEndRestStories;
import org.jbb.e2e.serenity.rest.account.MemberAccountResourceSteps;
import org.jbb.e2e.serenity.rest.account.UpdateAccountDto;
import org.jbb.e2e.serenity.rest.commons.TestMember;
import org.jbb.e2e.serenity.rest.members.MemberPublicDto;
import org.jbb.e2e.serenity.rest.members.MemberResourceSteps;
import org.jbb.e2e.serenity.rest.members.RegistrationRequestDto;
import org.jbb.e2e.serenity.rest.members.SetupMemberSteps;
import org.jbb.lib.restful.domain.ErrorInfo;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import static net.serenitybdd.rest.SerenityRest.then;

public class EpicPasswordPolicyRestStories extends EndToEndRestStories {

    private static final String PASS_6_CHARS = "abcdef";
    private static final String PASS_7_CHARS = "abcdefg";
    private static final String PASS_8_CHARS = "abcdefg8";
    private static final String PASS_9_CHARS = "abcdefg89";
    private static final String PASS_10_CHARS = "abcdefg891";

    @Steps
    PasswordPolicyResourceSteps passwordPolicyResourceSteps;

    @Steps
    MemberAccountResourceSteps memberAccountResourceSteps;

    @Steps
    MemberResourceSteps memberResourceSteps;

    @Steps
    SetupMemberSteps setupMemberSteps;

    @Test
    @WithTagValuesOf({Interface.REST, Type.EPIC, Feature.PASSWORD_POLICY, Feature.ACCOUNTS, Release.VER_0_11_0})
    public void password_policy_should_work_when_member_is_changing_password_via_api() {
        // register testbed member
        TestMember member = setupMemberSteps.create_member();
        make_rollback_after_test_case(setupMemberSteps.delete_member(member));

        authRestSteps.include_admin_basic_auth_header_for_every_request();

        PasswordPolicyDto currentPasswordPolicy = passwordPolicyResourceSteps.get_password_policy()
                .as(PasswordPolicyDto.class);
        make_rollback_after_test_case(restore(currentPasswordPolicy));

        // set password policy between 8 and 9 characters
        passwordPolicyResourceSteps.put_password_policy(passwordPolicyBetween(8, 9));
        assertRestSteps.assert_response_status(HttpStatus.OK);

        authRestSteps.include_basic_auth_header_for_every_request(member.getUsername(), member.getPassword());

        // try to set 7 characters password (should fail)
        memberAccountResourceSteps.put_member_account(member.getMemberId(), UpdateAccountDto.builder()
                .currentPassword(member.getPassword())
                .newPassword(PASS_7_CHARS)
                .build());
        assertRestSteps.assert_response_error_info(ErrorInfo.UPDATE_ACCOUNT_FAILED);
        memberAccountResourceSteps.should_contain_error_detail_about_invalid_password_length(8, 9);

        // try to set 8 characters password (should pass)
        memberAccountResourceSteps.put_member_account(member.getMemberId(), UpdateAccountDto.builder()
                .currentPassword(member.getPassword())
                .newPassword(PASS_8_CHARS)
                .build());
        assertRestSteps.assert_response_status(HttpStatus.OK);

        // try to set 9 characters password (should pass)
        authRestSteps.include_basic_auth_header_for_every_request(member.getUsername(), PASS_8_CHARS);
        memberAccountResourceSteps.put_member_account(member.getMemberId(), UpdateAccountDto.builder()
                .currentPassword(PASS_8_CHARS)
                .newPassword(PASS_9_CHARS)
                .build());
        assertRestSteps.assert_response_status(HttpStatus.OK);

        // try to set 10 characters password (should fail)
        authRestSteps.include_basic_auth_header_for_every_request(member.getUsername(), PASS_9_CHARS);
        memberAccountResourceSteps.put_member_account(member.getMemberId(), UpdateAccountDto.builder()
                .currentPassword(PASS_9_CHARS)
                .newPassword(PASS_10_CHARS)
                .build());
        assertRestSteps.assert_response_error_info(ErrorInfo.UPDATE_ACCOUNT_FAILED);
        memberAccountResourceSteps.should_contain_error_detail_about_invalid_password_length(8, 9);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.EPIC, Feature.PASSWORD_POLICY, Feature.REGISTRATION, Release.VER_0_11_0})
    public void password_policy_should_work_during_registration_via_api() {
        authRestSteps.include_admin_basic_auth_header_for_every_request();

        PasswordPolicyDto passwordPolicy = passwordPolicyResourceSteps.get_password_policy().as(PasswordPolicyDto.class);
        make_rollback_after_test_case(restore(passwordPolicy));

        // set password policy between 7 and 8 characters
        passwordPolicyResourceSteps.put_password_policy(passwordPolicyBetween(7, 8));
        assertRestSteps.assert_response_status(HttpStatus.OK);

        // try to register with 6 characters password (should fail)
        memberResourceSteps.post_member(register("osp1", PASS_6_CHARS));
        assertRestSteps.assert_response_error_info(ErrorInfo.REGISTRATION_FAILED);
        memberResourceSteps.should_contain_error_detail_about_invalid_password_length(7, 8);

        // try to register with 7 characters password (should pass)
        memberResourceSteps.register_member_with_success(register("osp1", PASS_7_CHARS));
        remove_when_rollback();

        // try to register with 8 characters password (should pass)
        memberResourceSteps.register_member_with_success(register("osp2", PASS_8_CHARS));
        remove_when_rollback();

        // try to register with 9 characters password (should fail)
        memberResourceSteps.post_member(register("osp3", PASS_9_CHARS));
        assertRestSteps.assert_response_error_info(ErrorInfo.REGISTRATION_FAILED);
        memberResourceSteps.should_contain_error_detail_about_invalid_password_length(7, 8);
    }


    private PasswordPolicyDto passwordPolicyBetween(Integer minimumLength, Integer maximumLength) {
        return PasswordPolicyDto.builder()
                .minimumLength(minimumLength)
                .maximumLength(maximumLength)
                .build();
    }

    private void remove_when_rollback() {
        MemberPublicDto createdMember = then().extract().as(MemberPublicDto.class);
        make_rollback_after_test_case(setupMemberSteps.delete_member(createdMember.getId()));
    }

    private RegistrationRequestDto register(String displayedName, String pass) {
        return RegistrationRequestDto.builder()
                .username(displayedName)
                .displayedName(displayedName)
                .email(displayedName.toLowerCase() + "@gmail.com")
                .password(pass)
                .build();
    }

    private RollbackAction restore(PasswordPolicyDto passwordPolicyDto) {
        return () -> {
            authRestSteps.include_admin_basic_auth_header_for_every_request();
            passwordPolicyResourceSteps.put_password_policy(passwordPolicyDto);
            authRestSteps.remove_authorization_headers_from_request();
        };
    }

}
