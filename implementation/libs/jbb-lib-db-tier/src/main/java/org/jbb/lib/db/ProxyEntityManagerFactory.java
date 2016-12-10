/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.db;

import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;


public class ProxyEntityManagerFactory extends LocalContainerEntityManagerFactoryBean {
    private LocalContainerEntityManagerFactoryBean delegateLocalContainerEntityManagerFactoryBean;


}
