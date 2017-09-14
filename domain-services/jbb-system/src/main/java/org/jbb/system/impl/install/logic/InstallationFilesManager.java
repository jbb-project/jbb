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
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.io.FileUtils;
import org.jbb.install.InstallationData;
import org.jbb.lib.commons.JbbMetaData;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InstallationFilesManager {

    private static final String AUTO_INSTALL_FILE_NAME = "jbb-autoinstall.properties";
    private static final String INSTALL_FILE_NAME = "installation.data";

    private static final String ADMIN_USERNAME = "admin.username";
    private static final String ADMIN_DISPLAYED_NAME = "admin.displayedName";
    private static final String ADMIN_EMAIL = "admin.email";
    private static final String ADMIN_PASSWORD = "admin.password";
    private static final String BOARD_NAME = "board.name";

    private final JbbMetaData jbbMetaData;

    public Optional<InstallationData> readAutoInstallFile() {
        File autoInstallFile = getAutoInstallFile();
        if (autoInstallFile.exists()) {
            return Optional.of(buildInstallationData(autoInstallFile));
        } else {
            return Optional.empty();
        }

    }

    private InstallationData buildInstallationData(File autoInstallFile) {
        Parameters params = new Parameters();
        FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
            new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                .configure(params.fileBased()
                    .setFile(autoInstallFile));
        try {
            FileBasedConfiguration configuration = builder.getConfiguration();
            return InstallationData.builder()
                .adminUsername(configuration.getString(ADMIN_USERNAME))
                .adminDisplayedName(configuration.getString(ADMIN_DISPLAYED_NAME))
                .adminEmail(configuration.getString(ADMIN_EMAIL))
                .adminPassword(configuration.getString(ADMIN_PASSWORD))
                .boardName(configuration.getString(BOARD_NAME))
                .build();
        } catch (ConfigurationException e) {
            throw new IllegalStateException(e);
        }
    }

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

    public boolean removeAutoInstallFile() {
        try {
            File autoInstallFile = getAutoInstallFile();
            if (autoInstallFile.exists()) {
                FileUtils.forceDelete(autoInstallFile);
                return true;
            }
            return false;
        } catch (IOException e) {
            log.warn("Cannot remove auto install file", e);
            return false;
        }
    }

    private File getAutoInstallFile() {
        return new File(jbbMetaData.jbbHomePath() + File.separator + AUTO_INSTALL_FILE_NAME);
    }

    private File getInstallFile() {
        return new File(jbbMetaData.jbbHomePath() + File.separator + INSTALL_FILE_NAME);
    }

}
