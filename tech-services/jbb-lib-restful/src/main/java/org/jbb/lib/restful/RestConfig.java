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

import org.jbb.lib.commons.security.OAuthScope;
import org.jbb.lib.mvc.MvcConfig;
import org.jbb.lib.restful.domain.ErrorInfo;
import org.jbb.lib.restful.error.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationCodeGrant;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.BasicAuth;
import springfox.documentation.service.ClientCredentialsGrant;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.ImplicitGrant;
import springfox.documentation.service.LoginEndpoint;
import springfox.documentation.service.OAuth;
import springfox.documentation.service.ResourceOwnerPasswordCredentialsGrant;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.TokenEndpoint;
import springfox.documentation.service.TokenRequestEndpoint;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.collect.Lists.newArrayList;
import static org.jbb.lib.restful.RestConstants.API;

@Configuration
@EnableSwagger2
@ComponentScan
@PropertySource("classpath:/swagger.properties")
@Import(MvcConfig.class)
public class RestConfig {

    public static final int DOMAIN_REST_CONTROLLER_ADVICE_ORDER = 1;

    @Autowired
    private TypeResolver typeResolver;

    @Bean
    public SecurityConfiguration securityInfo() {
        return SecurityConfigurationBuilder.builder()
                .useBasicAuthenticationWithAccessCodeGrant(true)
                .build();
    }

    @Bean
    public Docket swaggerDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant(API + "/**"))
                .build()
                .securitySchemes(Lists.newArrayList(basicAuth(), passwordOAuth(), clientCredentialsOAuth(), implicitOAuth(), authorizationCodeOAuth()))
                .securityContexts(Collections.singletonList(securityContext()))
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

        return newArrayList(standard500Response);
    }


    private ResponseMessage getResponseMessage(ErrorResponse errorResponse) {
        return new ResponseMessageBuilder()
                .code(errorResponse.getStatus().value())
                .message(errorResponse.getCode() + ": " + errorResponse.getMessage())
                .build();
    }

    private BasicAuth basicAuth() {
        return new BasicAuth("basicAuth");
    }

    private OAuth passwordOAuth() {
        List<GrantType> grantTypes = newArrayList();
        GrantType creGrant = new ResourceOwnerPasswordCredentialsGrant("/oauth/token");

        grantTypes.add(creGrant);

        return new OAuth("passwordOAuth2", scopes(), grantTypes);
    }

    private OAuth clientCredentialsOAuth() {
        List<GrantType> grantTypes = newArrayList();
        ClientCredentialsGrant clientCredentialsGrant = new ClientCredentialsGrant("/oauth/token");

        grantTypes.add(clientCredentialsGrant);

        return new OAuth("clientCredentialsOAuth2", scopes(), grantTypes);

    }

    private OAuth implicitOAuth() {
        List<GrantType> grantTypes = newArrayList();

        ImplicitGrant implicitGrant = new ImplicitGrant(new LoginEndpoint("/signin"), "auth");

        grantTypes.add(implicitGrant);

        return new OAuth("implicitOAuth2", scopes(), grantTypes);
    }

    private OAuth authorizationCodeOAuth() {
        List<GrantType> grantTypes = newArrayList();

        AuthorizationCodeGrant authorizationCodeGrant = new AuthorizationCodeGrant(
                new TokenRequestEndpoint("/oauth/authorize", "trusted", "secret"), new TokenEndpoint("/oauth/token", "token"));
        grantTypes.add(authorizationCodeGrant);

        return new OAuth("authorizationCodeOAuth2", scopes(), grantTypes);
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.ant("/api/**"))
                .build();
    }

    private List<SecurityReference> defaultAuth() {

        final AuthorizationScope[] authorizationScopes = scopes().toArray(new AuthorizationScope[]{});

        return Lists.newArrayList(new SecurityReference("passwordOAuth2", authorizationScopes),
                new SecurityReference("clientCredentialsOAuth2", authorizationScopes),
                new SecurityReference("basicAuth", authorizationScopes),
                new SecurityReference("implicitOAuth2", authorizationScopes),
                new SecurityReference("authorizationCodeOAuth2", authorizationScopes));
    }

    private List<AuthorizationScope> scopes() {
        return Arrays.stream(OAuthScope.values())
                .map(scope -> new AuthorizationScope(scope.getName(), scope.getDescription()))
                .collect(Collectors.toList());
    }

}
