/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.role.logic;

import org.jbb.security.impl.role.model.AdministratorEntity;
import org.springframework.stereotype.Component;

@Component
public class AdministratorEntityFactory {

    public AdministratorEntity create(Long memberId) {
        return AdministratorEntity
                .builder()
                .memberId(memberId)
                .build();
    }
}
