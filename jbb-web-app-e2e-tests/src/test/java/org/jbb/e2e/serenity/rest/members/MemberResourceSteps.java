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

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.Response;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;
import org.jbb.e2e.serenity.rest.RestUtils;
import org.jbb.e2e.serenity.rest.commons.ErrorDto;
import org.jbb.e2e.serenity.rest.commons.PageDto;
import org.springframework.http.HttpStatus;

public class MemberResourceSteps extends ScenarioSteps {

    @Step
    public PageDto<MemberPublicDto> getWithDisplayedName(String displayedName) {
        return RestUtils.prepareApiRequest()
            .basePath("api/v1/members")
            .param("displayedName", displayedName)
            .when()
            .get()
            .as(PageDto.class);
    }

    @Step
    public ErrorDto getErrorWithPage(String page) {
        return RestUtils.prepareApiRequest()
            .basePath("api/v1/members")
            .param("page", page)
            .when()
            .get()
            .as(ErrorDto.class);
    }

    @Step
    public Response getMemberPage(String page) {
        return RestUtils.prepareApiRequest()
            .basePath("api/v1/members")
            .param("page", page)
            .when()
            .get()
            .andReturn();
    }

    @Step
    public void assertBadRequestError(Response response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Step
    public ErrorDto assertErrorDto(Response response) {
        try {
            return response.as(ErrorDto.class);
        } catch (Exception e) {
            throw e;
        }
    }

}
