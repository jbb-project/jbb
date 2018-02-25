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

import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.WithTagValuesOf;

import org.jbb.e2e.serenity.Tags.Interface;
import org.jbb.e2e.serenity.rest.EndToEndRestStories;
import org.jbb.lib.restful.domain.ErrorInfo;
import org.junit.Test;

import static net.serenitybdd.rest.SerenityRest.then;
import static org.jbb.e2e.serenity.Tags.Feature;
import static org.jbb.e2e.serenity.Tags.Release;
import static org.jbb.e2e.serenity.Tags.Type;

public class GetMembersRestStories extends EndToEndRestStories {

    @Steps
    MemberResourceSteps memberResourceSteps;

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.GENERAL, Release.VER_0_10_0})
    public void should_administrator_account_be_visible_in_api_after_installation()
            throws Exception {
        // when
        memberResourceSteps.get_with_displayed_name("Administrator");

        // then
        memberResourceSteps.should_return_at_least_one_member();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.GENERAL, Release.VER_0_10_0})
    public void should_return_all_members_with_given_phrase_insensitive_in_displayed_name()
            throws Exception {
        // given
        register_and_mark_to_rollback("Adrian");
        register_and_mark_to_rollback("Hanna");
        register_and_mark_to_rollback("Damian");
        register_and_mark_to_rollback("Bartek");
        register_and_mark_to_rollback("Anna");

        // when
        memberResourceSteps.get_with_displayed_name("aN");

        // then
        memberResourceSteps
                .should_return_members_with_displayed_names("Adrian", "Hanna", "Damian", "Anna");
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.GENERAL, Release.VER_0_10_0})
    public void should_return_bind_error_when_text_page_number_provided_for_member_search()
            throws Exception {
        // when
        memberResourceSteps.get_member_page_with_page_number("aaa");

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.BIND_ERROR);
        memberResourceSteps.should_contain_error_detail_about_invalid_page_param();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.GENERAL, Release.VER_0_10_0})
    public void should_return_bind_error_when_nagative_page_number_provided_for_member_search()
            throws Exception {
        // when
        memberResourceSteps.get_member_page_with_page_number("-1");

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.BIND_ERROR);
        memberResourceSteps.should_contain_error_detail_about_negative_page_param();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.GENERAL, Release.VER_0_10_0})
    public void should_return_bind_error_when_text_page_size_number_provided_for_member_search()
            throws Exception {
        // when
        memberResourceSteps.get_member_page_with_page_size("aaa");

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.BIND_ERROR);
        memberResourceSteps.should_contain_error_detail_about_invalid_page_size_param();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.GENERAL, Release.VER_0_10_0})
    public void should_return_bind_error_when_negative_page_size_number_provided_for_member_search()
            throws Exception {
        // when
        memberResourceSteps.get_member_page_with_page_size("-1");

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.BIND_ERROR);
        memberResourceSteps.should_contain_error_detail_about_too_small_page_size_param();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.GENERAL, Release.VER_0_10_0})
    public void should_return_bind_error_when_zero_page_size_number_provided_for_member_search()
            throws Exception {
        // when
        memberResourceSteps.get_member_page_with_page_size("0");

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.BIND_ERROR);
        memberResourceSteps.should_contain_error_detail_about_too_small_page_size_param();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.GENERAL, Release.VER_0_10_0})
    public void should_return_bind_error_when_101_page_size_number_provided_for_member_search()
            throws Exception {
        // when
        memberResourceSteps.get_member_page_with_page_size("101");

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.BIND_ERROR);
        memberResourceSteps.should_contain_error_detail_about_too_large_page_size_param();
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
