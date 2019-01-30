/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.integration.impl;

import org.jbb.integration.impl.webhooks.WebhookProperties;
import org.jbb.lib.amqp.AmqpBrokerConfig;
import org.jbb.lib.commons.CommonsConfig;
import org.jbb.lib.db.DbConfig;
import org.jbb.lib.eventbus.EventBusConfig;
import org.jbb.lib.properties.ModulePropertiesFactory;
import org.jbb.lib.properties.PropertiesConfig;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan
@EnableJpaRepositories(
        basePackages = {"org.jbb.integration.impl.webhooks.event.dao"},
        entityManagerFactoryRef = DbConfig.EM_FACTORY_BEAN_NAME,
        transactionManagerRef = DbConfig.JPA_MANAGER_BEAN_NAME)
@EnableTransactionManagement
@Import({CommonsConfig.class, PropertiesConfig.class, DbConfig.class, EventBusConfig.class, AmqpBrokerConfig.class})
public class IntegrationConfig {

    public static final String ROUTING_QUEUE_NAME = "webhooks.routing";

    @Bean
    public WebhookProperties webhookProperties(ModulePropertiesFactory propertiesFactory) {
        return propertiesFactory.create(WebhookProperties.class);
    }

    @Bean
    public Queue routingQueue() {
        return new Queue(ROUTING_QUEUE_NAME);
    }

}
