/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.rest.account;

import static net.serenitybdd.rest.SerenityRest.then;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.Response;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;
import org.jbb.e2e.serenity.rest.RestUtils;

public class MemberAccountResourceSteps extends ScenarioSteps {

    public static final String V1_MEMBERS_ACCOUNT = "api/v1/members/{memberId}/account";

    @Step
    public Response get_member_account(String memberId) {
        return RestUtils.prepareApiRequest()
            .basePath(V1_MEMBERS_ACCOUNT)
            .pathParam("memberId", memberId)
            .when()
            .get()
            .andReturn();
    }

    @Step
    public void account_should_contains_email(String email) {
        AccountDto accountDto = then().extract().response().as(AccountDto.class);
        assertThat(accountDto.getEmail()).isEqualTo(email);
    }
}
