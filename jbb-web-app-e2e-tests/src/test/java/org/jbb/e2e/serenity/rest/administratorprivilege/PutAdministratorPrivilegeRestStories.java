/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.rest.administratorprivilege;

import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.WithTagValuesOf;

import org.jbb.e2e.serenity.Tags.Feature;
import org.jbb.e2e.serenity.Tags.Interface;
import org.jbb.e2e.serenity.Tags.Release;
import org.jbb.e2e.serenity.Tags.Type;
import org.jbb.e2e.serenity.rest.EndToEndRestStories;
import org.jbb.e2e.serenity.rest.commons.OAuthClient;
import org.jbb.e2e.serenity.rest.commons.TestMember;
import org.jbb.e2e.serenity.rest.members.SetupMemberSteps;
import org.jbb.e2e.serenity.rest.oauthclient.SetupOAuthSteps;
import org.jbb.lib.restful.domain.ErrorInfo;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import static org.jbb.lib.commons.security.OAuthScope.ADMINISTRATOR_PRIVILEGE_READ_WRITE;

public class PutAdministratorPrivilegeRestStories extends EndToEndRestStories {

    @Steps
    AdministratorPrivilegeResourceSteps administratorPrivilegeResourceSteps;

    @Steps
    SetupMemberSteps setupMemberSteps;

    @Steps
    SetupOAuthSteps setupOAuthSteps;

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.SECURITY, Release.VER_0_11_0})
    public void guest_cannot_put_administrator_privileges_via_api() {
        // given
        TestMember member = setupMemberSteps.create_member();
        make_rollback_after_test_case(setupMemberSteps.delete_member(member));

        // when
        administratorPrivilegeResourceSteps.put_administrator_privileges(member.getMemberId(),
                new UpdatePrivilegesDto(true));

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.UNAUTHORIZED);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.SECURITY, Release.VER_0_11_0})
    public void member_cannot_put_administrator_privileges_via_api() {
        // given
        TestMember member = setupMemberSteps.create_member();
        make_rollback_after_test_case(setupMemberSteps.delete_member(member));
        authRestSteps.include_basic_auth_header_for_every_request(member);

        // when
        administratorPrivilegeResourceSteps.put_administrator_privileges(member.getMemberId(),
                new UpdatePrivilegesDto(true));

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.FORBIDDEN);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.SECURITY, Release.VER_0_11_0})
    public void administrator_can_put_administrator_privileges_via_api() {
        // given
        TestMember member = setupMemberSteps.create_member();
        make_rollback_after_test_case(setupMemberSteps.delete_member(member));

        authRestSteps.include_admin_basic_auth_header_for_every_request();

        // when
        administratorPrivilegeResourceSteps.get_administrator_privileges(member.getMemberId()).as(PrivilegesDto.class);

        // then
        administratorPrivilegeResourceSteps.member_should_not_be_an_administrator(member.getMemberId());

        // when
        administratorPrivilegeResourceSteps.put_administrator_privileges(member.getMemberId(), new UpdatePrivilegesDto(true));
        assertRestSteps.assert_response_status(HttpStatus.OK);

        administratorPrivilegeResourceSteps.get_administrator_privileges(member.getMemberId()).as(PrivilegesDto.class);

        // then
        administratorPrivilegeResourceSteps.member_should_be_an_administrator(member.getMemberId());
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.SECURITY, Release.VER_0_11_0})
    public void administrator_cant_update_administrator_privileges_with_null_privilege_via_api() {
        // given
        TestMember member = setupMemberSteps.create_member();
        make_rollback_after_test_case(setupMemberSteps.delete_member(member));

        authRestSteps.include_admin_basic_auth_header_for_every_request();

        // when
        administratorPrivilegeResourceSteps.put_administrator_privileges(member.getMemberId(), new UpdatePrivilegesDto(null));

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.VALIDATION_ERROR);
        administratorPrivilegeResourceSteps.should_contain_error_detail_about_null_administrator_privilege();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.SECURITY, Release.VER_0_11_0})
    public void administrator_cant_update_administrator_privileges_for_not_existing_member_via_api() {
        // given
        authRestSteps.include_admin_basic_auth_header_for_every_request();

        // when
        administratorPrivilegeResourceSteps.put_administrator_privileges(1L, new UpdatePrivilegesDto(true));

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.MEMBER_NOT_FOUND);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.SECURITY, Release.VER_0_12_0})
    public void client_with_admin_privileges_write_scope_can_put_administrator_privileges_via_api() {
        // given
        TestMember member = setupMemberSteps.create_member();
        make_rollback_after_test_case(setupMemberSteps.delete_member(member));

        OAuthClient client = setupOAuthSteps.create_client_with_scope(ADMINISTRATOR_PRIVILEGE_READ_WRITE);
        make_rollback_after_test_case(setupOAuthSteps.delete_oauth_client(client));
        authRestSteps.authorize_every_request_with_oauth_client(client);

        // when
        administratorPrivilegeResourceSteps.put_administrator_privileges(member.getMemberId(),
                new UpdatePrivilegesDto(true));

        // then
        assertRestSteps.assert_response_status(HttpStatus.OK);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.SECURITY, Release.VER_0_12_0})
    public void client_without_admin_privileges_write_scope_cant_put_administrator_privileges_via_api() {
        // given
        TestMember member = setupMemberSteps.create_member();
        make_rollback_after_test_case(setupMemberSteps.delete_member(member));

        OAuthClient client = setupOAuthSteps.create_client_with_all_scopes_except(ADMINISTRATOR_PRIVILEGE_READ_WRITE);
        make_rollback_after_test_case(setupOAuthSteps.delete_oauth_client(client));
        authRestSteps.authorize_every_request_with_oauth_client(client);

        // when
        administratorPrivilegeResourceSteps.put_administrator_privileges(member.getMemberId(),
                new UpdatePrivilegesDto(true));

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.FORBIDDEN);
    }

}
