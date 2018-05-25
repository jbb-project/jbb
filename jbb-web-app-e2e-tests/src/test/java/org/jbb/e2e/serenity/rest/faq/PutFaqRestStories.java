/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.rest.faq;

import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.WithTagValuesOf;

import org.assertj.core.util.Lists;
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

public class PutFaqRestStories extends EndToEndRestStories {

    @Steps
    FaqResourceSteps faqResourceSteps;

    @Steps
    MemberResourceSteps memberResourceSteps;

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.FAQ_MANAGEMENT, Release.VER_0_11_0})
    public void guest_cannot_put_faq_via_api() {
        // when
        faqResourceSteps.put_faq(validFaq());

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.UNAUTHORIZED);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.FAQ_MANAGEMENT, Release.VER_0_11_0})
    public void member_cannot_put_faq_via_api() {
        // given
        register_and_mark_to_rollback("AccountTest");
        authRestSteps.include_basic_auth_header_for_every_request("AccountTest", "mysecretpass");

        // when
        faqResourceSteps.put_faq(validFaq());

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.FORBIDDEN);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.FAQ_MANAGEMENT, Release.VER_0_11_0})
    public void administrator_can_put_faq_via_api() {
        // given
        authRestSteps.include_admin_basic_auth_header_for_every_request();

        FaqDto faq = faqResourceSteps.get_faq().as(FaqDto.class);
        make_rollback_after_test_case(() -> faqResourceSteps.put_faq(faq));

        // when
        FaqDto newFaq = faqResourceSteps.put_faq(validFaq()).as(FaqDto.class);

        // then
        faqResourceSteps.should_contains_faq_content();
        assertThat(newFaq).isEqualTo(validFaq());
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.FAQ_MANAGEMENT, Release.VER_0_11_0})
    public void administrator_can_put_empty_faq_via_api() {
        // given
        authRestSteps.include_admin_basic_auth_header_for_every_request();

        FaqDto faq = faqResourceSteps.get_faq().as(FaqDto.class);
        make_rollback_after_test_case(() -> faqResourceSteps.put_faq(faq));

        FaqDto newFaq = validFaq();
        newFaq.setCategories(Lists.newArrayList());

        // when
        FaqDto updatedFaq = faqResourceSteps.put_faq(newFaq).as(FaqDto.class);

        // then
        faqResourceSteps.should_contains_faq_content();
        assertThat(updatedFaq).isEqualTo(newFaq);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.FAQ_MANAGEMENT, Release.VER_0_11_0})
    public void administrator_cant_update_faq_with_null_category_name_via_api() {
        // given
        authRestSteps.include_admin_basic_auth_header_for_every_request();

        FaqDto newFaq = validFaq();
        FaqCategoryDto faqCategory = newFaq.getCategories().get(0);
        faqCategory.setName(null);

        // when
        faqResourceSteps.put_faq(newFaq);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.VALIDATION_ERROR);
        faqResourceSteps.should_contain_error_detail_about_empty_category_name();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.FAQ_MANAGEMENT, Release.VER_0_11_0})
    public void administrator_cant_update_faq_with_blank_category_name_via_api() {
        // given
        authRestSteps.include_admin_basic_auth_header_for_every_request();

        FaqDto newFaq = validFaq();
        FaqCategoryDto faqCategory = newFaq.getCategories().get(0);
        faqCategory.setName("     ");

        // when
        faqResourceSteps.put_faq(newFaq);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.VALIDATION_ERROR);
        faqResourceSteps.should_contain_error_detail_about_empty_category_name();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.FAQ_MANAGEMENT, Release.VER_0_11_0})
    public void administrator_cant_update_faq_with_null_question_via_api() {
        // given
        authRestSteps.include_admin_basic_auth_header_for_every_request();

        FaqDto newFaq = validFaq();
        FaqCategoryDto faqCategory = newFaq.getCategories().get(0);
        FaqEntryDto faqEntry = faqCategory.getQuestions().get(0);
        faqEntry.setQuestion(null);

        // when
        faqResourceSteps.put_faq(newFaq);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.VALIDATION_ERROR);
        faqResourceSteps.should_contain_error_detail_about_empty_question();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.FAQ_MANAGEMENT, Release.VER_0_11_0})
    public void administrator_cant_update_faq_with_blank_question_via_api() {
        // given
        authRestSteps.include_admin_basic_auth_header_for_every_request();

        FaqDto newFaq = validFaq();
        FaqCategoryDto faqCategory = newFaq.getCategories().get(0);
        FaqEntryDto faqEntry = faqCategory.getQuestions().get(0);
        faqEntry.setQuestion("       ");

        // when
        faqResourceSteps.put_faq(newFaq);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.VALIDATION_ERROR);
        faqResourceSteps.should_contain_error_detail_about_empty_question();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.FAQ_MANAGEMENT, Release.VER_0_11_0})
    public void administrator_cant_update_faq_with_null_answer_via_api() {
        // given
        authRestSteps.include_admin_basic_auth_header_for_every_request();

        FaqDto newFaq = validFaq();
        FaqCategoryDto faqCategory = newFaq.getCategories().get(0);
        FaqEntryDto faqEntry = faqCategory.getQuestions().get(0);
        faqEntry.setAnswer(null);

        // when
        faqResourceSteps.put_faq(newFaq);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.VALIDATION_ERROR);
        faqResourceSteps.should_contain_error_detail_about_empty_answer();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.FAQ_MANAGEMENT, Release.VER_0_11_0})
    public void administrator_cant_update_faq_with_blank_answer_via_api() {
        // given
        authRestSteps.include_admin_basic_auth_header_for_every_request();

        FaqDto newFaq = validFaq();
        FaqCategoryDto faqCategory = newFaq.getCategories().get(0);
        FaqEntryDto faqEntry = faqCategory.getQuestions().get(0);
        faqEntry.setAnswer("   ");

        // when
        faqResourceSteps.put_faq(newFaq);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.VALIDATION_ERROR);
        faqResourceSteps.should_contain_error_detail_about_empty_answer();
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

    private FaqDto validFaq() {
        FaqDto dto = new FaqDto();
        FaqCategoryDto categoryDto = new FaqCategoryDto();
        FaqEntryDto faqEntryDto = new FaqEntryDto();
        faqEntryDto.setQuestion("Do you like potatoes?");
        faqEntryDto.setAnswer("No.");
        categoryDto.setName("Important");
        categoryDto.setQuestions(Lists.newArrayList(faqEntryDto));
        dto.setCategories(Lists.newArrayList(categoryDto));
        return dto;
    }

}
