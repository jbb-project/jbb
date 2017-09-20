/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.impl;

import org.jbb.lib.db.DbConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(
    basePackages = {"org.jbb.permissions.impl.acl.dao", "org.jbb.permissions.impl.role.dao"},
    entityManagerFactoryRef = DbConfig.EM_FACTORY_BEAN_NAME,
    transactionManagerRef = DbConfig.JPA_MANAGER_BEAN_NAME)
@EnableTransactionManagement
@ComponentScan("org.jbb.permissions.impl")
public class PermissionsConfig {

}
