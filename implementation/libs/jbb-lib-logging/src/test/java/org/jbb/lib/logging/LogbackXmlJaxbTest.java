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


//import org.jbb.lib.logging.jaxb.Configuration;

import org.apache.commons.io.FileUtils;
import org.jbb.lib.logging.jaxb.Appender;
import org.jbb.lib.logging.jaxb.AppenderRef;
import org.jbb.lib.logging.jaxb.Configuration;
import org.jbb.lib.logging.jaxb.Root;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.StringWriter;
import java.nio.charset.Charset;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import static org.assertj.core.api.Assertions.assertThat;

public class LogbackXmlJaxbTest {
    @Test
    public void shouldUnmarshall() throws Exception {
        // given
        JAXBContext jaxbContext = JAXBContext.newInstance(Configuration.class);
        Source streamSource = new StreamSource(new ClassPathResource("logback-webapp-test.xml").getInputStream());

        // when
        Configuration configuration = jaxbContext.createUnmarshaller()
                .unmarshal(streamSource, Configuration.class).getValue();

        // then
        assertThat(configuration.isDebug()).isFalse();
        assertThat(configuration.getShutdownHookOrStatusListenerOrContextListener()).hasSize(6);
    }

    @Test
    public void shouldMarshall() throws Exception {
        // given
        JAXBContext jaxbContext = JAXBContext.newInstance(Configuration.class);

        String expectedFile = FileUtils.readFileToString(new ClassPathResource("expected-result.xml").getFile(), Charset.forName("UTF-8"));

        Configuration configuration = new Configuration();
        configuration.setDebug(true);
        configuration.setScan("false");
        configuration.setScanPeriod("10 seconds");

        Appender appender = new Appender();
        appender.setName("Default-console");
        appender.setClazz("ch.qos.logback.core.ConsoleAppender");

        configuration.getShutdownHookOrStatusListenerOrContextListener().add(appender);

        AppenderRef appenderRef = new AppenderRef();
        appenderRef.setRef("Default-console");

        Root root = new Root();
        root.setLevel("INFO");
        root.getAppenderRef().add(appenderRef);

        configuration.getShutdownHookOrStatusListenerOrContextListener().add(root);

        // when
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        StringWriter sw = new StringWriter();
        marshaller.marshal(configuration, sw);
        String resultFile = sw.toString();

        // then
        assertThat(resultFile).isNotEqualToIgnoringWhitespace(expectedFile);
    }
}