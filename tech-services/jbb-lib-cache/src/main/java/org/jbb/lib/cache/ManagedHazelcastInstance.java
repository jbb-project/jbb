/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.cache;

import com.hazelcast.core.HazelcastInstance;

import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

import lombok.Setter;
import lombok.experimental.Delegate;

@Component
public class ManagedHazelcastInstance {

    @Delegate
    @Setter
    private HazelcastInstance target;

    @PreDestroy
    void shutdownOnClose() {
        if (target != null) {
            target.shutdown();
        }
    }
}
