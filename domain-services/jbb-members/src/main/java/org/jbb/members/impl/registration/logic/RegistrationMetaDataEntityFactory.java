/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.impl.registration.logic;

import java.time.LocalDateTime;
import org.jbb.members.api.registration.RegistrationRequest;
import org.jbb.members.impl.registration.model.RegistrationMetaDataEntity;
import org.springframework.stereotype.Component;

@Component
public class RegistrationMetaDataEntityFactory {

    public RegistrationMetaDataEntity create(RegistrationRequest request) {
        return RegistrationMetaDataEntity.builder()
            .ipAddress(request.getIpAddress())
                .joinDateTime(LocalDateTime.now())
                .build();
    }
}
