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
import org.jbb.e2e.serenity.rest.commons.TestMember;
import org.jbb.e2e.serenity.rest.commons.TestOAuthClient;
import org.jbb.e2e.serenity.rest.members.SetupMemberSteps;
import org.jbb.e2e.serenity.rest.oauthclient.SetupOAuthSteps;
import org.jbb.lib.restful.domain.ErrorInfo;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jbb.lib.commons.security.OAuthScope.FAQ_READ_WRITE;

public class PutFaqRestStories extends EndToEndRestStories {

    @Steps
    FaqResourceSteps faqResourceSteps;

    @Steps
    SetupMemberSteps setupMemberSteps;

    @Steps
    SetupOAuthSteps setupOAuthSteps;

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
        TestMember member = setupMemberSteps.create_member();
        make_rollback_after_test_case(setupMemberSteps.delete_member(member));
        authRestSteps.include_basic_auth_header_for_every_request(member);

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
        make_rollback_after_test_case(restore(faq));

        // when
        FaqDto newFaq = faqResourceSteps.put_faq(validFaq()).as(FaqDto.class);

        // then
        faqResourceSteps.should_contains_faq_content();
        assertThat(newFaq).isEqualTo(validFaq());
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.FAQ_MANAGEMENT, Release.VER_0_12_0})
    public void client_with_faq_write_scope_can_put_faq_via_api() {
        // given
        TestOAuthClient client = setupOAuthSteps.create_client_with_scope(FAQ_READ_WRITE);
        make_rollback_after_test_case(setupOAuthSteps.delete_oauth_client(client));
        authRestSteps.authorize_every_request_with_oauth_client(client);

        FaqDto faq = faqResourceSteps.get_faq().as(FaqDto.class);
        make_rollback_after_test_case(restore(faq));

        // when
        FaqDto newFaq = faqResourceSteps.put_faq(validFaq()).as(FaqDto.class);

        // then
        faqResourceSteps.should_contains_faq_content();
        assertThat(newFaq).isEqualTo(validFaq());
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.FAQ_MANAGEMENT, Release.VER_0_12_0})
    public void client_without_faq_write_scope_cant_put_faq_via_api() {
        // given
        TestOAuthClient client = setupOAuthSteps.create_client_with_all_scopes_except(FAQ_READ_WRITE);
        make_rollback_after_test_case(setupOAuthSteps.delete_oauth_client(client));
        authRestSteps.authorize_every_request_with_oauth_client(client);

        // when
        faqResourceSteps.put_faq(validFaq());

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.FORBIDDEN);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.FAQ_MANAGEMENT, Release.VER_0_11_0})
    public void administrator_can_put_empty_faq_via_api() {
        // given
        authRestSteps.include_admin_basic_auth_header_for_every_request();

        FaqDto faq = faqResourceSteps.get_faq().as(FaqDto.class);
        make_rollback_after_test_case(restore(faq));

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

    private RollbackAction restore(FaqDto faqDto) {
        return () -> {
            authRestSteps.include_admin_basic_auth_header_for_every_request();
            faqResourceSteps.put_faq(faqDto);
            authRestSteps.remove_authorization_headers_from_request();
        };
    }

}
