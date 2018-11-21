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
import org.jbb.e2e.serenity.rest.members.MemberPublicDto;
import org.jbb.e2e.serenity.rest.members.MemberResourceSteps;
import org.jbb.e2e.serenity.rest.members.RegistrationRequestDto;
import org.jbb.lib.restful.domain.ErrorInfo;
import org.junit.Test;

import static net.serenitybdd.rest.SerenityRest.then;

public class GetAdministratorPrivilegeRestStories extends EndToEndRestStories {

    @Steps
    AdministratorPrivilegeResourceSteps administratorPrivilegeResourceSteps;

    @Steps
    MemberResourceSteps memberResourceSteps;

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.SECURITY, Release.VER_0_11_0})
    public void guest_can_get_administrator_privileges_for_members_via_api() {
        // given
        register_and_mark_to_rollback("AccountTest");
        String memberId = memberResourceSteps.get_created_member_id().toString();

        // when
        administratorPrivilegeResourceSteps.get_administrator_privileges(memberId);

        // then
        administratorPrivilegeResourceSteps.member_should_not_be_an_administrator();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.SECURITY, Release.VER_0_11_0})
    public void member_can_get_administrator_privileges_for_members_via_api() {
        // given
        register_and_mark_to_rollback("AccountTest");
        String memberId = memberResourceSteps.get_created_member_id().toString();
        authRestSteps.include_basic_auth_header_for_every_request("AccountTest", "mysecretpass");

        // when
        administratorPrivilegeResourceSteps.get_administrator_privileges(memberId);

        // then
        administratorPrivilegeResourceSteps.member_should_not_be_an_administrator();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.BOARD_SETTINGS, Release.VER_0_11_0})
    public void administrator_can_get_administrator_privileges_for_members_via_api() {
        // given
        register_and_mark_to_rollback("AccountTest");
        String memberId = memberResourceSteps.get_created_member_id().toString();
        authRestSteps.include_admin_basic_auth_header_for_every_request();

        // when
        administratorPrivilegeResourceSteps.get_administrator_privileges(memberId);

        // then
        administratorPrivilegeResourceSteps.member_should_not_be_an_administrator();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.SECURITY, Release.VER_0_11_0})
    public void get_administrator_privileges_for_not_existing_member_should_end_with_member_not_found_error() {
        // when
        administratorPrivilegeResourceSteps.get_administrator_privileges("1");

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.MEMBER_NOT_FOUND);
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
