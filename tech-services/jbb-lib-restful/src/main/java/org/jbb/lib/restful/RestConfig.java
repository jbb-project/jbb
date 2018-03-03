/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.restful;

import com.google.common.collect.Lists;

import com.fasterxml.classmate.TypeResolver;

import org.jbb.lib.restful.domain.ErrorInfo;
import org.jbb.lib.restful.error.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Optional;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static org.jbb.lib.restful.RestConstants.API;

@Configuration
@EnableSwagger2
@ComponentScan
public class RestConfig {

    public static final int DOMAIN_REST_CONTROLLER_ADVICE_ORDER = 1;

    @Autowired
    private TypeResolver typeResolver;

    @Bean
    public Docket swaggerDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant(API + "/**"))
                .build()
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.GET, getStandardResponses())
                .globalResponseMessage(RequestMethod.POST, getStandardResponses())
                .globalResponseMessage(RequestMethod.PUT, getStandardResponses())
                .globalResponseMessage(RequestMethod.DELETE, getStandardResponses())
                .genericModelSubstitutes(Optional.class)
                .additionalModels(typeResolver.resolve(ErrorResponse.class));

    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("jBB REST API")
                .description("RESTful endpoints for java bulletin board services")
                .version("LATEST")
                .build();
    }

    private List<ResponseMessage> getStandardResponses() {
        ResponseMessage standard500Response = getResponseMessage(
                ErrorResponse.createFrom(ErrorInfo.INTERNAL_ERROR));

        return Lists.newArrayList(standard500Response);
    }


    private ResponseMessage getResponseMessage(ErrorResponse errorResponse) {
        return new ResponseMessageBuilder()
                .code(errorResponse.getStatus().value())
                .message(errorResponse.getCode() + ": " + errorResponse.getMessage())
                .build();
    }

}
