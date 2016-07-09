/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoreConfig {
    @Bean
    public JbbHomePath jbbHomePath() {
        JbbHomePath jbbHomePath = new JbbHomePath();
        jbbHomePath.resolveEffectiveAndStoreToSystemProperty();
        jbbHomePath.createIfNotExists();
        return jbbHomePath;
    }

    @Bean
    public JbbMetaData jbbMetaData(JbbHomePath jbbHomePath) {
        return new JbbMetaData(jbbHomePath);
    }
}
