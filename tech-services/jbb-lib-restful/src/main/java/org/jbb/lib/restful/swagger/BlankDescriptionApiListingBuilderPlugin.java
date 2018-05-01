/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.restful.swagger;

import org.springframework.stereotype.Component;

import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ApiListingBuilderPlugin;
import springfox.documentation.spi.service.contexts.ApiListingContext;

import static org.apache.commons.lang3.StringUtils.SPACE;

@Component
public class BlankDescriptionApiListingBuilderPlugin implements ApiListingBuilderPlugin {

    @Override
    public void apply(ApiListingContext apiListingContext) {
        apiListingContext.apiListingBuilder().description(SPACE);
    }

    @Override
    public boolean supports(DocumentationType documentationType) {
        return true;
    }
}
