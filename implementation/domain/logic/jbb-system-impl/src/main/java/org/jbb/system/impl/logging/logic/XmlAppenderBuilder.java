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
import org.jbb.lib.logging.jaxb.Filter;
import org.jbb.lib.logging.jaxb.RollingPolicy;
import org.jbb.lib.logging.jaxb.Target;
import org.jbb.system.api.model.logging.LogAppender;
import org.jbb.system.api.model.logging.LogConsoleAppender;
import org.jbb.system.api.model.logging.LogFileAppender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Consumer;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

@Component
public class XmlAppenderBuilder {
    public static final String CONSOLE_APPENDER_CLASSNAME = "ch.qos.logback.core.ConsoleAppender";
    public static final String FILE_APPENDER_CLASSNAME = "ch.qos.logback.core.rolling.RollingFileAppender";

    private final XmlFilterBuilder filterBuilder;

    @Autowired
    public XmlAppenderBuilder(XmlFilterBuilder filterBuilder) {
        this.filterBuilder = filterBuilder;
    }

    public Appender buildXml(LogAppender appender) {
        Appender xmlAppender = new Appender();
        xmlAppender.setName(appender.getName());

        if (appender instanceof LogConsoleAppender) {
            buildXmlConsoleAppender((LogConsoleAppender) appender, xmlAppender);
        } else if (appender instanceof LogFileAppender) {
            buildXmlFileAppender((LogFileAppender) appender, xmlAppender);
        } else {
            throw new IllegalArgumentException("Unsupported appender type: " + appender.getClass());
        }

        return xmlAppender;
    }

    private void buildXmlConsoleAppender(LogConsoleAppender consoleAppender, Appender xmlAppender) {
        xmlAppender.setClazz(CONSOLE_APPENDER_CLASSNAME);

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

    private void buildXmlFileAppender(LogFileAppender fileAppender, Appender xmlAppender) {
        xmlAppender.setClazz(FILE_APPENDER_CLASSNAME);

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
        String clazz = xmlAppender.getClazz();
        LogAppender logAppender;
        if (CONSOLE_APPENDER_CLASSNAME.equals(clazz)) {
            logAppender = buildConsoleAppender(xmlAppender);
        } else if (FILE_APPENDER_CLASSNAME.equals(clazz)) {
            logAppender = buildFileAppender(xmlAppender);
        } else {
            throw new IllegalArgumentException("Unsupported appender class type: " + clazz);
        }
        return logAppender;
    }

    private LogConsoleAppender buildConsoleAppender(Appender xmlAppender) {
        List<Object> xmlElements = xmlAppender.getTargetOrFileOrWithJansi();

        LogConsoleAppender logConsoleAppender = new LogConsoleAppender();

        logConsoleAppender.setName(xmlAppender.getName());

        xmlElements.stream()
                .filter(o -> ((JAXBElement) o).getDeclaredType().equals(Target.class))
                .map(o -> (Target) ((JAXBElement) o).getValue())
                .findFirst()
                .ifPresent(target ->
                        logConsoleAppender.setTarget(LogConsoleAppender.Target.getFromStreamName(target.value())));

        xmlElements.stream()
                .filter(o -> ((JAXBElement) o).getDeclaredType().equals(Filter.class))
                .map(o -> (Filter) ((JAXBElement) o).getValue())
                .findFirst()
                .ifPresent(filter -> logConsoleAppender.setFilter(filterBuilder.build(filter)));

        xmlElements.stream()
                .filter(o -> o instanceof JAXBElement && "withJansi".equals((((JAXBElement) o).getName().getLocalPart())))
                .map(jaxb -> (Boolean) ((JAXBElement) jaxb).getValue())
                .findFirst()
                .ifPresent(withJansi -> logConsoleAppender.setUseColor(withJansi));

        xmlElements.stream()
                .filter(o -> ((JAXBElement) o).getDeclaredType().equals(Encoder.class))
                .map(o -> (Encoder) ((JAXBElement) o).getValue())
                .findFirst()
                .ifPresent(
                        encoder -> encoder.getCharsetOrImmediateFlushOrLayout().stream()
                                .filter(o -> "pattern".equals((o.getName().getLocalPart())))
                                .map(jaxb -> (String) ((JAXBElement) jaxb).getValue())
                                .findFirst()
                                .ifPresent(pattern -> logConsoleAppender.setPattern(pattern))
                );

        return logConsoleAppender;
    }

    private LogFileAppender buildFileAppender(Appender xmlAppender) {
        List<Object> xmlElements = xmlAppender.getTargetOrFileOrWithJansi();

        LogFileAppender logFileAppender = new LogFileAppender();

        logFileAppender.setName(xmlAppender.getName());

        xmlElements.stream()
                .filter(o -> o instanceof JAXBElement && "file".equals((((JAXBElement) o).getName().getLocalPart())))
                .map(jaxb -> (String) ((JAXBElement) jaxb).getValue())
                .findFirst()
                .ifPresent(currentLogFileName -> logFileAppender.setCurrentLogFileName(currentLogFileName));

        xmlElements.stream()
                .filter(o -> ((JAXBElement) o).getDeclaredType().equals(RollingPolicy.class))
                .map(o -> (RollingPolicy) ((JAXBElement) o).getValue())
                .findFirst()
                .ifPresent(
                        rollingPolicy -> {
                            List<JAXBElement<?>> jaxbElements = rollingPolicy.getFileNamePatternOrMaxHistoryOrMinIndex();

                            jaxbElements.stream()
                                    .filter(o -> "fileNamePattern".equals((o.getName().getLocalPart())))
                                    .map(jaxb -> (String) ((JAXBElement) jaxb).getValue())
                                    .findFirst()
                                    .ifPresent(pattern -> logFileAppender.setRotationFileNamePattern(pattern));

                            jaxbElements.stream()
                                    .filter(o -> "maxFileSize".equals((o.getName().getLocalPart())))
                                    .map(jaxb -> (String) ((JAXBElement) jaxb).getValue())
                                    .findFirst()
                                    .ifPresent(fileSize -> logFileAppender.setMaxFileSize(LogFileAppender.FileSize.valueOf(fileSize)));

                            jaxbElements.stream()
                                    .filter(o -> "maxHistory".equals((o.getName().getLocalPart())))
                                    .map(jaxb -> (Integer) ((JAXBElement) jaxb).getValue())
                                    .findFirst()
                                    .ifPresent(maxHistory -> logFileAppender.setMaxHistory(maxHistory));
                        }
                );

        xmlElements.stream()
                .filter(o -> ((JAXBElement) o).getDeclaredType().equals(Filter.class))
                .map(o -> (Filter) ((JAXBElement) o).getValue())
                .findFirst()
                .ifPresent(filter -> logFileAppender.setFilter(filterBuilder.build(filter)));

        xmlElements.stream()
                .filter(o -> ((JAXBElement) o).getDeclaredType().equals(Encoder.class))
                .map(o -> (Encoder) ((JAXBElement) o).getValue())
                .findFirst()
                .ifPresent(
                        setPattern(logFileAppender)
                );

        return logFileAppender;
    }

    private Consumer<Encoder> setPattern(LogFileAppender logFileAppender) {
        return encoder -> encoder.getCharsetOrImmediateFlushOrLayout().stream()
                .filter(o -> "pattern".equals((o.getName().getLocalPart())))
                .map(jaxb -> (String) ((JAXBElement) jaxb).getValue())
                .findFirst()
                .ifPresent(pattern -> logFileAppender.setPattern(pattern));
    }
}
