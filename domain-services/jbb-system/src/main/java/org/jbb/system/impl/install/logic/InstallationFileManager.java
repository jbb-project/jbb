/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.install.logic;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.io.FileUtils;
import org.jbb.install.InstallationData;
import org.jbb.lib.commons.JbbMetaData;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InstallationFileManager {

    public static final String INSTALL_FILE_NAME = "installation.data";

    private final JbbMetaData jbbMetaData;

    public void createInstallationFile(InstallationData installationData) {
        Parameters params = new Parameters();
        File installFile = getInstallFile();
        try {
            FileUtils.touch(installFile);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
            new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                .configure(params.fileBased()
                    .setFile(installFile));
        builder.setAutoSave(true);

        try {
            FileBasedConfiguration configuration = builder.getConfiguration();
            configuration.addProperty("installationId", UUID.randomUUID().toString());
            configuration.addProperty("installationDate", LocalDateTime.now().toString());
            configuration.addProperty("boardFounderUsername", installationData.getAdminUsername());
            builder.save();
        } catch (ConfigurationException e) {
            throw new IllegalStateException(e);
        }
    }

    public boolean installationFileExists() {
        return getInstallFile().exists();
    }

    private File getInstallFile() {
        return new File(jbbMetaData.jbbHomePath() + File.separator + INSTALL_FILE_NAME);
    }

}
