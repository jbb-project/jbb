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
import org.junit.Test;

import static net.serenitybdd.rest.SerenityRest.then;

public class GetPasswordPolicyRestStories extends EndToEndRestStories {

    @Steps
    PasswordPolicyResourceSteps passwordPolicyResourceSteps;

    @Steps
    MemberResourceSteps memberResourceSteps;

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.PASSWORD_POLICY, Release.VER_0_11_0})
    public void guest_can_get_password_policy_via_api() {
        // when
        passwordPolicyResourceSteps.get_password_policy();

        // then
        passwordPolicyResourceSteps.should_contains_password_policy();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.PASSWORD_POLICY, Release.VER_0_11_0})
    public void member_can_get_password_policy_via_api() {
        // given
        register_and_mark_to_rollback("AccountTest");
        authRestSteps.include_basic_auth_header_for_every_request("AccountTest", "mysecretpass");

        // when
        passwordPolicyResourceSteps.get_password_policy();

        // then
        passwordPolicyResourceSteps.should_contains_password_policy();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.PASSWORD_POLICY, Release.VER_0_11_0})
    public void administrator_can_get_password_policy_via_api() {
        // given
        authRestSteps.include_admin_basic_auth_header_for_every_request();

        // when
        passwordPolicyResourceSteps.get_password_policy();

        // then
        passwordPolicyResourceSteps.should_contains_password_policy();
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
