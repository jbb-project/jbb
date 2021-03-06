/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.properties;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
class UpdateFilePropertyChangeListenerFactoryBean implements FactoryBean<UpdateFilePropertyChangeListener> {
    private final ApplicationContext context;

    private Class<? extends ModuleStaticProperties> clazz;

    public UpdateFilePropertyChangeListenerFactoryBean setClass(Class<? extends ModuleStaticProperties> clazz) {
        this.clazz = clazz;
        return this;
    }

    @Override
    public UpdateFilePropertyChangeListener getObject() {
        JbbPropertyFilesResolver propResolver = context.getBean(JbbPropertyFilesResolver.class);
        return new UpdateFilePropertyChangeListener(propResolver, clazz);
    }

    @Override
    public Class<?> getObjectType() {
        return UpdateFilePropertyChangeListener.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
