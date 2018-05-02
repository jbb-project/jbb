/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.metrics.reporter;

import com.codahale.metrics.CsvFileProvider;

import java.io.File;

public class SafeNameCsvFileProvider implements CsvFileProvider {
    @Override
    public File getFile(File directory, String metricName) {
        return new File(directory, sanitize(metricName) + ".csv");
    }

    protected String sanitize(String metricName) {
        //Forward slash character is definitely illegal in both Windows and Linux
        //https://msdn.microsoft.com/en-us/library/windows/desktop/aa365247(v=vs.85).aspx
        return metricName.replaceFirst("^/", "")
                .replaceAll("/", ".")
                .replaceAll("\\*", "");
    }
}
