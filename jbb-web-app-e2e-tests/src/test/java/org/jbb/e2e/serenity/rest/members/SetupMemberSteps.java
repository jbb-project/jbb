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

import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.steps.ScenarioSteps;

import org.apache.commons.lang3.RandomStringUtils;
import org.jbb.e2e.serenity.rest.commons.AuthRestSteps;
import org.jbb.e2e.serenity.rest.commons.TestMember;
import org.jbb.e2e.serenity.web.EndToEndWebStories;

public class SetupMemberSteps extends ScenarioSteps {
    public static final String DEFAULT_TEST_PASSWORD = "mysecretpass";

    @Steps
    AuthRestSteps authRestSteps;

    @Steps
    MemberResourceSteps memberResourceSteps;

    @Step
    public TestMember create_member() {
        authRestSteps.remove_authorization_headers_from_request();
        String displayedName = "TestMember-" + RandomStringUtils.randomAlphabetic(6);
        RegistrationRequestDto registerRequest = register(displayedName);
        MemberPublicDto member = memberResourceSteps.register_member_with_success(registerRequest)
                .as(MemberPublicDto.class);
        return new TestMember(member.getId(), registerRequest.getUsername(), registerRequest.getPassword());
    }

    private RegistrationRequestDto register(String displayedName) {
        return RegistrationRequestDto.builder()
                .username(displayedName)
                .displayedName(displayedName)
                .email(displayedName.toLowerCase() + "@gmail.com")
                .password(DEFAULT_TEST_PASSWORD)
                .build();
    }

    public EndToEndWebStories.RollbackAction delete_member(TestMember member) {
        return () -> {
            authRestSteps.include_admin_basic_auth_header_for_every_request();
            memberResourceSteps.delete_member(member.getMemberId().toString());
            authRestSteps.remove_authorization_headers_from_request();
        };
    }

}
