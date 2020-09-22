/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.rest.cache;

import org.jbb.system.api.cache.CacheSettings;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CacheSettingsTranslator {

    private final ModelMapper modelMapper = new ModelMapper();

    public CacheSettingsDto toDto(CacheSettings cacheSettings) {
        return modelMapper.map(cacheSettings, CacheSettingsDto.class);
    }

    public CacheSettings toModel(EditCacheSettingsDto dto) {
        return modelMapper.map(dto, CacheSettings.class);
    }
}
