/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
public class Oauth2ResourceServerConfig extends ResourceServerConfigurerAdapter {

//    @Autowired
//    private RootAuthSuccessHandler rootAuthSuccessHandler;
//
//    @Autowired
//    private RootAuthFailureHandler rootAuthFailureHandler;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.requestMatchers().antMatchers("/api/**")
                .and().authorizeRequests().anyRequest().permitAll();
//        http.oauth2Login().successHandler(rootAuthSuccessHandler);
//        http.oauth2Login().failureHandler(rootAuthFailureHandler);
    }
}