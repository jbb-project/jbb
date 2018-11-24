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
import org.jbb.e2e.serenity.rest.commons.TestMember;
import org.jbb.e2e.serenity.rest.commons.TestOAuthClient;
import org.jbb.e2e.serenity.rest.members.SetupMemberSteps;
import org.jbb.e2e.serenity.rest.oauthclient.SetupOAuthSteps;
import org.jbb.lib.restful.domain.ErrorInfo;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jbb.lib.commons.security.OAuthScope.PASSWORD_POLICY_READ_WRITE;

public class PutPasswordPolicyRestStories extends EndToEndRestStories {

    @Steps
    PasswordPolicyResourceSteps passwordPolicyResourceSteps;

    @Steps
    SetupMemberSteps setupMemberSteps;

    @Steps
    SetupOAuthSteps setupOAuthSteps;

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
        TestMember member = setupMemberSteps.create_member();
        make_rollback_after_test_case(setupMemberSteps.delete_member(member));
        authRestSteps.include_basic_auth_header_for_every_request(member);

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
        make_rollback_after_test_case(restore(passwordPolicy));

        // when
        PasswordPolicyDto newPasswordPolicy = passwordPolicyResourceSteps.put_password_policy(validPasswordPolicy()).as(PasswordPolicyDto.class);

        // then
        passwordPolicyResourceSteps.should_contains_password_policy();
        assertThat(newPasswordPolicy).isEqualTo(validPasswordPolicy());
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.PASSWORD_POLICY, Release.VER_0_12_0})
    public void client_with_password_policy_write_scope_can_put_password_policy_via_api() {
        // given
        TestOAuthClient client = setupOAuthSteps.create_client_with_scope(PASSWORD_POLICY_READ_WRITE);
        make_rollback_after_test_case(setupOAuthSteps.delete_oauth_client(client));
        authRestSteps.authorize_every_request_with_oauth_client(client);

        PasswordPolicyDto passwordPolicy = passwordPolicyResourceSteps.get_password_policy().as(PasswordPolicyDto.class);
        make_rollback_after_test_case(restore(passwordPolicy));

        // when
        passwordPolicyResourceSteps.put_password_policy(validPasswordPolicy());

        // then
        passwordPolicyResourceSteps.should_contains_password_policy();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.PASSWORD_POLICY, Release.VER_0_12_0})
    public void client_without_password_policy_write_scope_cannot_put_password_policy_via_api() {
        // given
        TestOAuthClient client = setupOAuthSteps.create_client_with_all_scopes_except(PASSWORD_POLICY_READ_WRITE);
        make_rollback_after_test_case(setupOAuthSteps.delete_oauth_client(client));
        authRestSteps.authorize_every_request_with_oauth_client(client);

        // when
        passwordPolicyResourceSteps.put_password_policy(validPasswordPolicy());

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.FORBIDDEN);
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

    private PasswordPolicyDto validPasswordPolicy() {
        return PasswordPolicyDto.builder()
                .minimumLength(4)
                .maximumLength(20)
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
