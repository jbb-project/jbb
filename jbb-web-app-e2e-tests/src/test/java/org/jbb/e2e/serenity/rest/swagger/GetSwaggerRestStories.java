/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.rest.swagger;

import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.WithTagValuesOf;

import org.jbb.e2e.serenity.Tags.Feature;
import org.jbb.e2e.serenity.Tags.Interface;
import org.jbb.e2e.serenity.Tags.Release;
import org.jbb.e2e.serenity.Tags.Type;
import org.jbb.e2e.serenity.rest.EndToEndRestStories;
import org.junit.Test;
import org.springframework.http.HttpStatus;

public class GetSwaggerRestStories extends EndToEndRestStories {

    @Steps
    SwaggerResourceSteps swaggerResourceSteps;

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.REST_API, Release.VER_0_10_0})
    public void swagger_api_docs_as_json_should_be_available()
            throws Exception {
        // when
        swaggerResourceSteps.get_swagger_api_docs();

        // then
        assertRestSteps.assert_response_status(HttpStatus.OK);
        assertRestSteps.assert_json_content_type();
    }

}
