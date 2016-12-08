/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.logging.logic;

import org.jbb.lib.logging.jaxb.Appender;
import org.jbb.lib.logging.jaxb.Encoder;
import org.jbb.lib.logging.jaxb.RollingPolicy;
import org.jbb.lib.logging.jaxb.Target;
import org.jbb.system.api.model.logging.LogAppender;
import org.jbb.system.api.model.logging.LogConsoleAppender;
import org.jbb.system.api.model.logging.LogFileAppender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

@Component
public class XmlAppenderBuilder {
    private final XmlFilterBuilder filterBuilder;

    @Autowired
    public XmlAppenderBuilder(XmlFilterBuilder filterBuilder) {
        this.filterBuilder = filterBuilder;
    }

    public Appender buildXml(LogAppender appender) {
        Appender xmlAppender = new Appender();
        xmlAppender.setName(appender.getName());

        if (appender instanceof LogConsoleAppender) {
            buildConsoleAppender((LogConsoleAppender) appender, xmlAppender);
        } else if (appender instanceof LogFileAppender) {
            buildFileAppender((LogFileAppender) appender, xmlAppender);
        } else {
            throw new IllegalArgumentException("Unsupported appender type: " + appender.getClass());
        }

        return xmlAppender;
    }

    private void buildConsoleAppender(LogConsoleAppender consoleAppender, Appender xmlAppender) {
        LogConsoleAppender.Target target = consoleAppender.getTarget();
        xmlAppender.getTargetOrFileOrWithJansi().add(Target.fromValue(target.getValue()));

        if (consoleAppender.getFilter() != null) {
            xmlAppender.getTargetOrFileOrWithJansi().add(filterBuilder.buildXml(consoleAppender.getFilter()));
        }

        Encoder encoder = new Encoder();
        JAXBElement pattern = new JAXBElement(new QName("pattern"), String.class, consoleAppender.getPattern());
        encoder.getCharsetOrImmediateFlushOrLayout().add(pattern);
        xmlAppender.getTargetOrFileOrWithJansi().add(encoder);

        JAXBElement withJansi = new JAXBElement(new QName("withJansi"), Boolean.class, consoleAppender.isUseColor());
        xmlAppender.getTargetOrFileOrWithJansi().add(withJansi);

        return;
    }

    private void buildFileAppender(LogFileAppender fileAppender, Appender xmlAppender) {
        JAXBElement file = new JAXBElement(new QName("file"), String.class, fileAppender.getCurrentLogFileName());
        xmlAppender.getTargetOrFileOrWithJansi().add(file);

        RollingPolicy rollingPolicy = new RollingPolicy();
        rollingPolicy.setClazz("ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy");
        JAXBElement rotationPattern = new JAXBElement(new QName("fileNamePattern"), String.class, fileAppender.getRotationFileNamePattern());
        rollingPolicy.getFileNamePatternOrMaxHistoryOrMinIndex().add(rotationPattern);
        JAXBElement maxFileSize = new JAXBElement(new QName("maxFileSize"), String.class, fileAppender.getMaxFileSize().toString());
        rollingPolicy.getFileNamePatternOrMaxHistoryOrMinIndex().add(maxFileSize);
        JAXBElement maxHistory = new JAXBElement(new QName("maxHistory"), Integer.class, fileAppender.getMaxHistory());
        rollingPolicy.getFileNamePatternOrMaxHistoryOrMinIndex().add(maxHistory);

        if (fileAppender.getFilter() != null) {
            xmlAppender.getTargetOrFileOrWithJansi().add(filterBuilder.buildXml(fileAppender.getFilter()));
        }

        Encoder encoder = new Encoder();
        JAXBElement pattern = new JAXBElement(new QName("pattern"), String.class, fileAppender.getPattern());
        encoder.getCharsetOrImmediateFlushOrLayout().add(pattern);
        JAXBElement charset = new JAXBElement(new QName("charset"), String.class, "UTF-8");
        encoder.getCharsetOrImmediateFlushOrLayout().add(charset);
        xmlAppender.getTargetOrFileOrWithJansi().add(encoder);
    }

    public LogAppender build(Appender xmlAppender) {
        // todo
        return null;
    }
}
