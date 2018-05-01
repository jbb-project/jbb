/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.install.auto;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.io.FileUtils;
import org.jbb.install.InstallationData;
import org.jbb.lib.commons.JbbMetaData;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static org.jbb.lib.commons.PropertiesUtils.buildPropertiesConfiguration;


@Slf4j
@Component
@RequiredArgsConstructor
public class AutoInstallationFileManager {

    private static final String AUTO_INSTALL_FILE_NAME = "jbb-autoinstall.properties";
    private static final String INSTALL_CLASSPATH_CONFIG_FILENAME = "install.config";
    private static final String LEAVE_AUTO_INSTALL_FILE_KEY = "leaveAutoInstallFile";

    private final JbbMetaData jbbMetaData;
    private final List<AutoInstallationDataReader> autoInstallationDataReaders;

    private Configuration installData;

    @PostConstruct
    public void setInstallData() throws IOException {
        ClassPathResource installConfigData = new ClassPathResource(INSTALL_CLASSPATH_CONFIG_FILENAME);
        installData = buildPropertiesConfiguration(installConfigData.getURL());
    }

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
                if (!installData.getBoolean(LEAVE_AUTO_INSTALL_FILE_KEY)) {
                    FileUtils.forceDelete(autoInstallFile);
                    return true;
                } else {
                    log.warn(
                            "Skip removing jBB auto install file ({}) - IT CAN CONTAIN SENSITIVE DATA !!!",
                            getAutoInstallFile().getAbsolutePath());
                    return false;
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
