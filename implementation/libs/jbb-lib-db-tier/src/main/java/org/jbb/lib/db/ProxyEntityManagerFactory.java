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

import javax.persistence.EntityManagerFactory;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Delegate;


public class ProxyEntityManagerFactory implements EntityManagerFactory {
    @Delegate
    @Getter
    @Setter
    private EntityManagerFactory objectBeingProxied;

}
