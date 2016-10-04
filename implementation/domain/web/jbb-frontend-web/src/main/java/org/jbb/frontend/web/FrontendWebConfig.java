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

import com.google.common.collect.Lists;

import org.jbb.frontend.web.base.logic.view.AcpReplacingViewStrategy;
import org.jbb.frontend.web.base.logic.view.DefaultReplacingViewStrategy;
import org.jbb.frontend.web.base.logic.view.RedirectReplacingViewStrategy;
import org.jbb.frontend.web.base.logic.view.ReplacingViewStrategy;
import org.jbb.frontend.web.base.logic.view.UcpReplacingViewStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ComponentScan("org.jbb.frontend.web")
public class FrontendWebConfig {
    @Bean
    public List<ReplacingViewStrategy> replacingStrategies(RedirectReplacingViewStrategy redirectReplacingViewStrategy,
                                                           UcpReplacingViewStrategy ucpReplacingViewStrategy,
                                                           AcpReplacingViewStrategy acpReplacingViewStrategy,
                                                           DefaultReplacingViewStrategy defaultReplacingViewStrategy) {
        return Lists.newArrayList(
                redirectReplacingViewStrategy,
                ucpReplacingViewStrategy,
                acpReplacingViewStrategy,
                defaultReplacingViewStrategy);
    }

}
