/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.rest.members;

import static net.serenitybdd.rest.SerenityRest.then;
import static org.assertj.core.api.Assertions.assertThat;
import static org.jbb.e2e.serenity.Tags.Feature;
import static org.jbb.e2e.serenity.Tags.Release;
import static org.jbb.e2e.serenity.Tags.Type;

import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.WithTagValuesOf;
import org.jbb.e2e.serenity.Tags.Interface;
import org.jbb.e2e.serenity.rest.EndToEndRestStories;
import org.jbb.e2e.serenity.rest.commons.PageDto;
import org.jbb.lib.restful.domain.ErrorInfo;
import org.junit.Test;

public class MembersRestStories extends EndToEndRestStories {

    @Steps
    MemberResourceSteps memberResourceSteps;

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.REGISTRATION, Release.VER_0_10_0})
    public void should_administrator_account_be_visible_in_api_after_installation()
        throws Exception {
        // when
        PageDto<MemberPublicDto> pageResult = memberResourceSteps
            .get_with_displayed_name("Administrator");

        // then
        assertThat(pageResult.getContent()).isNotEmpty();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.REGISTRATION, Release.VER_0_10_0})
    public void invalid_page() throws Exception {
        // when
        memberResourceSteps.get_member_page_with_page_number("aaa");

        // then
        assertRestSteps.assert_error_info(ErrorInfo.BIND_ERROR);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.REGISTRATION, Release.VER_0_10_0})
    public void create_member_via_api_should_be_possible() throws Exception {
        // given
        RegistrationRequestDto registrationRequest = RegistrationRequestDto.builder()
            .username("testrest2")
            .displayedName("Test Rest2")
            .password("testrest")
            .email("test2@rest.com")
            .build();

        // when
        memberResourceSteps.post_member(registrationRequest);
        MemberPublicDto createdMember = then().extract().as(MemberPublicDto.class);

        make_rollback_after_test_case(
            memberResourceSteps.delete_testbed_member(createdMember.getId())
        );

        // then
        memberResourceSteps.created_member_should_have_id();
    }


}
