/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.metrics;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;
import org.jbb.lib.properties.ModuleProperties;

@Config.HotReload(type = Config.HotReloadType.ASYNC)
@Sources({"file:${jbb.home}/config/metrics.properties"})
public interface MetricProperties extends ModuleProperties { // NOSONAR (key names should stay)

    String METRICS_REPORTER_CONSOLE_ENABLED = "metrics.reporter.console.enabled";
    String METRICS_REPORTER_CONSOLE_PERIOD_SECONDS = "metrics.reporter.console.periodSeconds";

    String METRICS_REPORTER_JMX_ENABLED = "metrics.reporter.jmx.enabled";

    String METRICS_REPORTER_CSV_ENABLED = "metrics.reporter.csv.enabled";
    String METRICS_REPORTER_CSV_PERIOD_SECONDS = "metrics.reporter.csv.periodSeconds";

    String METRICS_REPORTER_LOG_ENABLED = "metrics.reporter.log.enabled";
    String METRICS_REPORTER_LOG_PERIOD_SECONDS = "metrics.reporter.log.periodSeconds";

    @Key(METRICS_REPORTER_CONSOLE_ENABLED)
    Boolean consoleReporterEnabled();

    @Key(METRICS_REPORTER_CONSOLE_PERIOD_SECONDS)
    Integer consoleReporterPeriodSeconds();

    @Key(METRICS_REPORTER_JMX_ENABLED)
    Boolean jmxReporterEnabled();

    @Key(METRICS_REPORTER_CSV_ENABLED)
    Boolean csvReporterEnabled();

    @Key(METRICS_REPORTER_CSV_PERIOD_SECONDS)
    Integer csvReporterPeriodSeconds();

    @Key(METRICS_REPORTER_LOG_ENABLED)
    Boolean logReporterEnabled();

    @Key(METRICS_REPORTER_LOG_PERIOD_SECONDS)
    Integer logReporterPeriodSeconds();

}