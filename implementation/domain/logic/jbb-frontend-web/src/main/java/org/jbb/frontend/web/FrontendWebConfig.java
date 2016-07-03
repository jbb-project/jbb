/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.web;

import org.jbb.frontend.web.interceptors.BoardNameInterceptor;
import org.jbb.frontend.web.interceptors.ReplacingViewInterceptor;
import org.jbb.lib.mvc.MvcConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("org.jbb.frontend")
public class FrontendWebConfig extends MvcConfig {
    @Bean
    public BoardNameInterceptor boardNameInterceptor() {
        return new BoardNameInterceptor();
    }

    @Bean
    public ReplacingViewInterceptor replacingViewInterceptor() {
        return new ReplacingViewInterceptor();
    }

}
