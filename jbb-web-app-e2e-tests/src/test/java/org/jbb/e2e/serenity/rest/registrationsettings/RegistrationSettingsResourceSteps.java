/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.rest.registrationsettings;

import static net.serenitybdd.rest.SerenityRest.then;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.Response;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;
import org.jbb.e2e.serenity.rest.RestUtils;

public class RegistrationSettingsResourceSteps extends ScenarioSteps {

    public static final String V1_REGISTRATION_SETTINGS = "api/v1/registration-settings";

    @Step
    public Response get_registration_settings() {
        return RestUtils.prepareApiRequest()
            .basePath(V1_REGISTRATION_SETTINGS)
            .when()
            .get()
            .andReturn();
    }

    @Step
    public Response put_registration_settings(RegistrationSettingsDto registrationSettingsDto) {
        return RestUtils.prepareApiRequest()
            .basePath(V1_REGISTRATION_SETTINGS)
            .when()
            .body(registrationSettingsDto)
            .put()
            .andReturn();
    }

    @Step
    public void registration_settings_should_contain_email_duplication() {
        RegistrationSettingsDto registrationSettingsDto = then().extract().response()
            .as(RegistrationSettingsDto.class);
        assertThat(registrationSettingsDto.getEmailDuplicationAllowed()).isNotNull();
    }

    @Step
    public void email_duplication_should_be_enabled() {
        RegistrationSettingsDto registrationSettingsDto = then().extract().response()
            .as(RegistrationSettingsDto.class);
        assertThat(registrationSettingsDto.getEmailDuplicationAllowed()).isEqualTo(true);
    }

    @Step
    public void email_duplication_should_be_false() {
        RegistrationSettingsDto registrationSettingsDto = then().extract().response()
            .as(RegistrationSettingsDto.class);
        assertThat(registrationSettingsDto.getEmailDuplicationAllowed()).isEqualTo(false);
    }

}
