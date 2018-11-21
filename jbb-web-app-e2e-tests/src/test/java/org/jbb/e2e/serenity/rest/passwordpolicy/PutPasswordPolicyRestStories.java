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
import org.jbb.e2e.serenity.rest.members.MemberPublicDto;
import org.jbb.e2e.serenity.rest.members.MemberResourceSteps;
import org.jbb.e2e.serenity.rest.members.RegistrationRequestDto;
import org.jbb.lib.restful.domain.ErrorInfo;
import org.junit.Test;

import static net.serenitybdd.rest.SerenityRest.then;
import static org.assertj.core.api.Assertions.assertThat;

public class PutPasswordPolicyRestStories extends EndToEndRestStories {

    @Steps
    PasswordPolicyResourceSteps passwordPolicyResourceSteps;

    @Steps
    MemberResourceSteps memberResourceSteps;


    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.PASSWORD_POLICY, Release.VER_0_11_0})
    public void guest_cannot_put_password_policy_via_api() {
        // when
        passwordPolicyResourceSteps.put_password_policy(validPasswordPolicy());

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.UNAUTHORIZED);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.PASSWORD_POLICY, Release.VER_0_11_0})
    public void member_cannot_put_password_policy_via_api() {
        // given
        register_and_mark_to_rollback("AccountTest");
        authRestSteps.include_basic_auth_header_for_every_request("AccountTest", "mysecretpass");

        // when
        passwordPolicyResourceSteps.put_password_policy(validPasswordPolicy());

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.FORBIDDEN);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.PASSWORD_POLICY, Release.VER_0_11_0})
    public void administrator_can_put_password_policy_via_api() {
        // given
        authRestSteps.include_admin_basic_auth_header_for_every_request();

        PasswordPolicyDto passwordPolicy = passwordPolicyResourceSteps.get_password_policy().as(PasswordPolicyDto.class);
        make_rollback_after_test_case(() -> passwordPolicyResourceSteps.put_password_policy(passwordPolicy));

        // when
        PasswordPolicyDto newPasswordPolicy = passwordPolicyResourceSteps.put_password_policy(validPasswordPolicy()).as(PasswordPolicyDto.class);

        // then
        passwordPolicyResourceSteps.should_contains_password_policy();
        assertThat(newPasswordPolicy).isEqualTo(validPasswordPolicy());
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.PASSWORD_POLICY, Release.VER_0_11_0})
    public void administrator_cant_update_password_policy_with_null_minimum_length_via_api() {
        // given
        authRestSteps.include_admin_basic_auth_header_for_every_request();

        PasswordPolicyDto passwordPolicy = validPasswordPolicy();
        passwordPolicy.setMinimumLength(null);

        // when
        passwordPolicyResourceSteps.put_password_policy(passwordPolicy);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.INVALID_PASSWORD_POLICY);
        passwordPolicyResourceSteps.should_contain_error_detail_about_null_minimum_length();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.PASSWORD_POLICY, Release.VER_0_11_0})
    public void administrator_cant_update_password_policy_with_negative_minimum_length_via_api() {
        // given
        authRestSteps.include_admin_basic_auth_header_for_every_request();

        PasswordPolicyDto passwordPolicy = validPasswordPolicy();
        passwordPolicy.setMinimumLength(-1);

        // when
        passwordPolicyResourceSteps.put_password_policy(passwordPolicy);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.INVALID_PASSWORD_POLICY);
        passwordPolicyResourceSteps.should_contain_error_detail_about_not_positive_minimum_length();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.PASSWORD_POLICY, Release.VER_0_11_0})
    public void administrator_cant_update_password_policy_with_zero_minimum_length_via_api() {
        // given
        authRestSteps.include_admin_basic_auth_header_for_every_request();

        PasswordPolicyDto passwordPolicy = validPasswordPolicy();
        passwordPolicy.setMinimumLength(0);

        // when
        passwordPolicyResourceSteps.put_password_policy(passwordPolicy);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.INVALID_PASSWORD_POLICY);
        passwordPolicyResourceSteps.should_contain_error_detail_about_not_positive_minimum_length();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.PASSWORD_POLICY, Release.VER_0_11_0})
    public void administrator_cant_update_password_policy_with_null_maximum_length_via_api() {
        // given
        authRestSteps.include_admin_basic_auth_header_for_every_request();

        PasswordPolicyDto passwordPolicy = validPasswordPolicy();
        passwordPolicy.setMaximumLength(null);

        // when
        passwordPolicyResourceSteps.put_password_policy(passwordPolicy);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.INVALID_PASSWORD_POLICY);
        passwordPolicyResourceSteps.should_contain_error_detail_about_null_maximum_length();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.PASSWORD_POLICY, Release.VER_0_11_0})
    public void administrator_cant_update_password_policy_with_negative_maximum_length_via_api() {
        // given
        authRestSteps.include_admin_basic_auth_header_for_every_request();

        PasswordPolicyDto passwordPolicy = validPasswordPolicy();
        passwordPolicy.setMaximumLength(-1);

        // when
        passwordPolicyResourceSteps.put_password_policy(passwordPolicy);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.INVALID_PASSWORD_POLICY);
        passwordPolicyResourceSteps.should_contain_error_detail_about_not_positive_maximum_length();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.PASSWORD_POLICY, Release.VER_0_11_0})
    public void administrator_cant_update_password_policy_with_zero_maximum_length_via_api() {
        // given
        authRestSteps.include_admin_basic_auth_header_for_every_request();

        PasswordPolicyDto passwordPolicy = validPasswordPolicy();
        passwordPolicy.setMaximumLength(0);

        // when
        passwordPolicyResourceSteps.put_password_policy(passwordPolicy);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.INVALID_PASSWORD_POLICY);
        passwordPolicyResourceSteps.should_contain_error_detail_about_not_positive_maximum_length();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.PASSWORD_POLICY, Release.VER_0_11_0})
    public void administrator_cant_update_password_policy_with_minimum_length_greater_than_maximum() {
        // given
        authRestSteps.include_admin_basic_auth_header_for_every_request();

        PasswordPolicyDto passwordPolicy = validPasswordPolicy();
        passwordPolicy.setMinimumLength(20);
        passwordPolicy.setMaximumLength(16);

        // when
        passwordPolicyResourceSteps.put_password_policy(passwordPolicy);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.INVALID_PASSWORD_POLICY);
        passwordPolicyResourceSteps.should_contain_error_detail_about_minimum_length_greater_than_maximum();
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

    private PasswordPolicyDto validPasswordPolicy() {
        PasswordPolicyDto dto = new PasswordPolicyDto();
        dto.setMinimumLength(4);
        dto.setMaximumLength(20);
        return dto;
    }


}
