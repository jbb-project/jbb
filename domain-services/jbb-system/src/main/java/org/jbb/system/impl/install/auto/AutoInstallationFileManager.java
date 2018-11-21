/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.install.auto;

import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.io.FileUtils;
import org.jbb.install.InstallationData;
import org.jbb.lib.commons.JbbMetaData;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
@RequiredArgsConstructor
public class AutoInstallationFileManager {

    private static final String AUTO_INSTALL_FILE_NAME = "jbb-autoinstall.properties";
    private static final String LEAVE_AUTO_INSTALL_CONFIG_ENV = "JBB_LEAVE_AUTO_INSTALL_FILE";

    private final JbbMetaData jbbMetaData;
    private final List<AutoInstallationDataReader> autoInstallationDataReaders;

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
            InstallationData installationData = InstallationData.builder().build();
            autoInstallationDataReaders
                    .forEach(reader -> reader.updateInstallationData(installationData, configuration));
            return installationData;
        } catch (ConfigurationException e) {
            throw new IllegalStateException(e);
        }
    }

    public boolean removeAutoInstallFile() {
        try {
            File autoInstallFile = getAutoInstallFile();
            if (autoInstallFile.exists()) {
                String leaveAutoInstallFile = System.getenv(LEAVE_AUTO_INSTALL_CONFIG_ENV);
                if (leaveAutoInstallFile != null &&
                        Boolean.TRUE.equals(Boolean.valueOf(leaveAutoInstallFile))) {
                    log.warn(
                        "Skip removing jBB auto install file ({}) - IT CAN CONTAIN SENSITIVE DATA !!!",
                        getAutoInstallFile().getAbsolutePath());
                    return false;
                } else {
                    FileUtils.forceDelete(autoInstallFile);
                    return true;
                }
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

}
