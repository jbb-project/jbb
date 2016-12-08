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

import org.jbb.lib.logging.jaxb.Filter;
import org.jbb.lib.logging.jaxb.MatchValues;
import org.jbb.system.api.model.logging.LogFilter;
import org.jbb.system.api.model.logging.LogLevel;
import org.jbb.system.api.model.logging.LogLevelFilter;
import org.jbb.system.api.model.logging.LogThresholdFilter;
import org.springframework.stereotype.Component;

import java.util.Optional;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

@Component
public class XmlFilterBuilder {

    public static final String LEVEL_LITER_CLASSNAME = "ch.qos.logback.classic.filter.LevelFilter";
    public static final String THRESHOLD_FILTER_CLASSNAME = "ch.qos.logback.classic.filter.ThresholdFilter";

    public Filter buildXml(LogFilter filter) {
        Filter xmlFilter = new Filter();

        if (filter instanceof LogLevelFilter) {
            buildXmlLevelFilter((LogLevelFilter) filter, xmlFilter);
        } else if (filter instanceof LogThresholdFilter) {
            buildXmlThresholdFilter((LogThresholdFilter) filter, xmlFilter);
        } else {
            throw new IllegalArgumentException("Unsupported filter type: " + filter.getClass());
        }

        return xmlFilter;
    }

    private void buildXmlLevelFilter(LogLevelFilter levelFilter, Filter xmlFilter) {
        xmlFilter.setClazz(LEVEL_LITER_CLASSNAME);

        JAXBElement level = new JAXBElement(new QName("level"), String.class,
                levelFilter.getLogLevel().toString().toUpperCase());
        xmlFilter.getLevelOrOnMatchOrOnMismatch().add(level);

        JAXBElement onMatch = new JAXBElement(new QName("OnMatch"), MatchValues.class, MatchValues.ACCEPT);
        xmlFilter.getLevelOrOnMatchOrOnMismatch().add(onMatch);

        JAXBElement onMismatch = new JAXBElement(new QName("OnMismatch"), MatchValues.class, MatchValues.ACCEPT);
        xmlFilter.getLevelOrOnMatchOrOnMismatch().add(onMismatch);
    }

    private void buildXmlThresholdFilter(LogThresholdFilter thresholdFilter, Filter xmlFilter) {
        xmlFilter.setClazz(THRESHOLD_FILTER_CLASSNAME);

        JAXBElement level = new JAXBElement(new QName("level"), String.class,
                thresholdFilter.getLogLevel().toString().toUpperCase());

        xmlFilter.getLevelOrOnMatchOrOnMismatch().add(level);
    }

    public LogFilter build(Filter xmlFilter) {
        String clazz = xmlFilter.getClazz();
        LogFilter logFilter;
        if (LEVEL_LITER_CLASSNAME.equals(clazz)) {
            logFilter = buildLevelFilter(xmlFilter);
        } else if (THRESHOLD_FILTER_CLASSNAME.equals(clazz)) {
            logFilter = buildThresholdFilter(xmlFilter);
        } else {
            throw new IllegalArgumentException("Unsupported filter class name: " + clazz);
        }
        return logFilter;
    }

    private LogLevelFilter buildLevelFilter(Filter xmlFilter) {
        LogLevelFilter filter = new LogLevelFilter();
        Optional<String> level = getLevel(xmlFilter);
        if (level.isPresent()) {
            filter.setLogLevel(LogLevel.valueOf(level.get().toUpperCase()));
        }
        return filter;
    }

    private LogThresholdFilter buildThresholdFilter(Filter xmlFilter) {
        LogThresholdFilter filter = new LogThresholdFilter();
        Optional<String> level = getLevel(xmlFilter);
        if (level.isPresent()) {
            filter.setLogLevel(LogLevel.valueOf(level.get().toUpperCase()));
        }
        return filter;
    }

    private Optional<String> getLevel(Filter xmlFilter) {
        return xmlFilter.getLevelOrOnMatchOrOnMismatch().stream()
                .filter(o -> o instanceof JAXBElement && "level".equals((((JAXBElement) o).getName())))
                .map(jaxb -> (String) ((JAXBElement) jaxb).getValue())
                .findFirst();
    }
}
