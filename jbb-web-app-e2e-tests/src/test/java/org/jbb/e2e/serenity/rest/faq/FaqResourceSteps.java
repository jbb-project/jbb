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

import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.steps.ScenarioSteps;

import org.jbb.e2e.serenity.rest.RestUtils;
import org.jbb.e2e.serenity.rest.commons.AssertRestSteps;
import org.jbb.e2e.serenity.rest.commons.ErrorDetailDto;

import io.restassured.response.Response;

import static net.serenitybdd.rest.SerenityRest.then;
import static org.assertj.core.api.Assertions.assertThat;

public class FaqResourceSteps extends ScenarioSteps {

    public static final String V1_FAQ = "api/v1/faq";

    @Steps
    AssertRestSteps assertRestSteps;

    @Step
    public Response get_faq() {
        return RestUtils.prepareApiRequest()
                .basePath(V1_FAQ)
                .when()
                .get()
                .andReturn();
    }

    @Step
    public Response put_faq(FaqDto faqDto) {
        return RestUtils.prepareApiRequest()
                .basePath(V1_FAQ)
                .when()
                .body(faqDto)
                .put()
                .andReturn();
    }

    @Step
    public void should_contains_faq_content() {
        FaqDto faqDto = then().extract().response().as(FaqDto.class);
        assertThat(faqDto).isNotNull();
    }

    @Step
    public void should_contain_error_detail_about_empty_category_name() {
        assertRestSteps.assert_response_error_detail_exists(
                ErrorDetailDto.builder()
                        .name("categories[0].name")
                        .message("must not be blank").build()
        );
    }

    @Step
    public void should_contain_error_detail_about_empty_question() {
        assertRestSteps.assert_response_error_detail_exists(
                ErrorDetailDto.builder()
                        .name("categories[0].questions[0].question")
                        .message("must not be blank").build()
        );
    }

    @Step
    public void should_contain_error_detail_about_empty_answer() {
        assertRestSteps.assert_response_error_detail_exists(
                ErrorDetailDto.builder()
                        .name("categories[0].questions[0].answer")
                        .message("must not be blank").build()
        );
    }
}
