/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.rest.health;

import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.WithTagValuesOf;

import org.jbb.e2e.serenity.Tags;
import org.jbb.e2e.serenity.rest.EndToEndRestStories;
import org.junit.Test;
import org.springframework.http.HttpStatus;

public class GetHealthRestStories extends EndToEndRestStories {

    @Steps
    HealthResourceSteps healthResourceSteps;

    @Test
    @WithTagValuesOf({Tags.Interface.REST, Tags.Type.SMOKE, Tags.Feature.HEALTH_CHECK, Tags.Release.VER_0_11_0})
    public void getting_api_error_code_should_be_available() {
        // when
        healthResourceSteps.get_health();

        // then
        assertRestSteps.assert_response_status(HttpStatus.OK);
        healthResourceSteps.assert_response_contains_healthy_status();
    }
}
