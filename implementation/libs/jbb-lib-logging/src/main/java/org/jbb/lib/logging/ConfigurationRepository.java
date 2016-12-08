/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.logging;

import org.jbb.lib.logging.jaxb.Configuration;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

public class ConfigurationRepository {
    private final LoggingBootstrapper loggingBootstrapper;

    public ConfigurationRepository(LoggingBootstrapper loggingBootstrapper) {
        this.loggingBootstrapper = loggingBootstrapper;
    }

    public Configuration getConfiguration() {
        File logbackConfigurationFile = logbackConfigurationFile();

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Configuration.class);
            Source streamSource = new StreamSource(logbackConfigurationFile);
            return jaxbContext.createUnmarshaller()
                    .unmarshal(streamSource, Configuration.class).getValue();
        } catch (JAXBException e) {
            throw new LoggingConfigurationException("Getting logging configuration failed", e);
        }
    }

    public void persistNewConfiguration(Configuration newConfiguration) {
        File logbackConfigurationFile = logbackConfigurationFile();

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Configuration.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(newConfiguration, logbackConfigurationFile);
        } catch (JAXBException e) {
            throw new LoggingConfigurationException("Saving logging configuration failed", e);
        }
    }

    private File logbackConfigurationFile() {
        String logConfFilePath = loggingBootstrapper.getLogConfFilePath();
        return new File(logConfFilePath);
    }
}
