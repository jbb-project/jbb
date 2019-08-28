/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.rest.session;

import org.jbb.system.api.session.MemberSession;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class SessionsTranslator {

    private final ModelMapper modelMapper = new ModelMapper();

    public MemberSessionDto toDto(MemberSession session) {
        MemberSessionDto dto = modelMapper.map(session, MemberSessionDto.class);
        dto.setMaxActiveTime(dto.getLastAccessedTime().plus(session.getTimeToLive()));
        return dto;
    }
}
