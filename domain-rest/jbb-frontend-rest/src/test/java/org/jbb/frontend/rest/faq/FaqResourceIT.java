/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.rest.faq;

import org.assertj.core.util.Lists;
import org.jbb.frontend.api.faq.Faq;
import org.jbb.frontend.api.faq.FaqService;
import org.jbb.frontend.rest.BaseIT;
import org.jbb.lib.restful.domain.ErrorInfo;
import org.jbb.lib.restful.error.ErrorResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

public class FaqResourceIT extends BaseIT {

    @Autowired
    private FaqService faqServiceMock;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        Mockito.reset(faqServiceMock);
    }

    @Test
    public void gettingFaq_success() {
        // given
        given(faqServiceMock.getFaq()).willReturn(exampleFaq());

        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given();
        MockMvcResponse response = request.when().get("/api/v1/faq");

        // then
        response.then().statusCode(200);
        FaqDto resultBody = response.then().extract().body().as(FaqDto.class);
        assertThat(resultBody.getCategories().get(0).getName()).isEqualTo("faq category");
        assertThat(resultBody.getCategories().get(0).getQuestions().get(0).getQuestion()).isEqualTo("how are you?");
        assertThat(resultBody.getCategories().get(0).getQuestions().get(0).getAnswer()).isEqualTo("fine");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    public void updatingFaq_shouldBePossibleForAdministrators() {
        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .body(validFaqDto())
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        MockMvcResponse response = request.when().put("/api/v1/faq");

        // then
        response.then().statusCode(200);
        FaqDto resultBody = response.then().extract().body().as(FaqDto.class);
        assertThat(resultBody.getCategories().get(0).getName()).isEqualTo("faq category");
        assertThat(resultBody.getCategories().get(0).getQuestions().get(0).getQuestion()).isEqualTo("how are you?");
        assertThat(resultBody.getCategories().get(0).getQuestions().get(0).getAnswer()).isEqualTo("fine");
    }

    @Test
    @WithMockUser(username = "member")
    public void updatingFaq_shouldBeNotPossibleForRegularMembers() {
        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .body(validFaqDto())
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        MockMvcResponse response = request.when().put("/api/v1/faq");

        // then
        assertError(response, 403, ErrorInfo.FORBIDDEN);
    }

    @Test
    public void updatingFaq_shouldBeNotPossibleForGuests() {
        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .body(validFaqDto())
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        MockMvcResponse response = request.when().put("/api/v1/faq");

        // then
        assertError(response, 401, ErrorInfo.UNAUTHORIZED);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    public void updatingFaq_shouldfail_whenNewFaqIsNotValid() {
        // when
        FaqDto faqDto = validFaqDto();
        faqDto.getCategories().get(0).setName("");
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .body(faqDto)
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        MockMvcResponse response = request.when().put("/api/v1/faq");

        // then
        assertError(response, 400, ErrorInfo.VALIDATION_ERROR);

    }

    private FaqDto validFaqDto() {
        return FaqDto.builder()
                .categories(Lists.newArrayList(FaqCategoryDto.builder()
                        .name("faq category")
                        .questions(Lists.newArrayList(FaqEntryDto.builder()
                                .question("how are you?")
                                .answer("fine")
                                .build()))
                        .build()))
                .build();
    }

    private Faq exampleFaq() {
        return Faq.builder()
                .categories(Lists.newArrayList(Faq.Category.builder()
                        .name("faq category")
                        .questions(Lists.newArrayList(Faq.Entry.builder()
                                .question("how are you?")
                                .answer("fine")
                                .build()
                        ))
                        .build()))
                .build();
    }

    private void assertError(MockMvcResponse response, int httpStatus, ErrorInfo errorInfo) {
        response.then().statusCode(httpStatus);
        ErrorResponse resultBody = response.then().extract().body().as(ErrorResponse.class);
        assertThat(resultBody.getCode()).isEqualTo(errorInfo.getCode());
    }

}