/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.rest;

import org.jbb.frontend.api.faq.FaqService;
import org.jbb.frontend.api.format.FormatSettingsService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MockFrontendRestConfig {

    @Bean
    @Primary
    public FaqService faqService() {
        return Mockito.mock(FaqService.class);
    }

    @Bean
    @Primary
    public FormatSettingsService formatSettingsService() {
        return Mockito.mock(FormatSettingsService.class);
    }

}
