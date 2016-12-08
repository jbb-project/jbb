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
import org.jbb.system.api.model.logging.LogLevelFilter;
import org.jbb.system.api.model.logging.LogThresholdFilter;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

@Component
public class XmlFilterBuilder {
    public Filter buildXml(LogFilter filter) {
        Filter xmlFilter = new Filter();

        if (filter instanceof LogLevelFilter) {
            buildLevelFilter((LogLevelFilter) filter, xmlFilter);
        } else if (filter instanceof LogThresholdFilter) {
            buildThresholdFilter((LogThresholdFilter) filter, xmlFilter);
        } else {
            throw new IllegalArgumentException("Unsupported filter type: " + filter.getClass());
        }

        return xmlFilter;
    }

    private void buildLevelFilter(LogLevelFilter levelFilter, Filter xmlFilter) {
        xmlFilter.setClazz("ch.qos.logback.classic.filter.LevelFilter");

        JAXBElement level = new JAXBElement(new QName("level"), String.class,
                levelFilter.getLogLevel().toString().toUpperCase());
        xmlFilter.getLevelOrOnMatchOrOnMismatch().add(level);

        JAXBElement onMatch = new JAXBElement(new QName("OnMatch"), MatchValues.class, MatchValues.ACCEPT);
        xmlFilter.getLevelOrOnMatchOrOnMismatch().add(onMatch);

        JAXBElement onMismatch = new JAXBElement(new QName("OnMismatch"), MatchValues.class, MatchValues.ACCEPT);
        xmlFilter.getLevelOrOnMatchOrOnMismatch().add(onMismatch);
    }

    private void buildThresholdFilter(LogThresholdFilter thresholdFilter, Filter xmlFilter) {
        xmlFilter.setClazz("ch.qos.logback.classic.filter.ThresholdFilter");

        JAXBElement level = new JAXBElement(new QName("level"), String.class,
                thresholdFilter.getLogLevel().toString().toUpperCase());
        xmlFilter.getLevelOrOnMatchOrOnMismatch().add(level);
    }
}
