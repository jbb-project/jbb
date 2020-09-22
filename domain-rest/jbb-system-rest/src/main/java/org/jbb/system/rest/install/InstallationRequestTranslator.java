/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.rest.install;

import org.jbb.install.InstallationData;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class InstallationRequestTranslator {

    private final ModelMapper modelMapper = new ModelMapper();

    public InstallationData toModel(InstallationRequestDto installationRequestDto) {
        InstallationData installationData = modelMapper.map(installationRequestDto, InstallationData.class);
        installationData.setCacheInstallationData(Optional.empty());
        return installationData;
    }
}
