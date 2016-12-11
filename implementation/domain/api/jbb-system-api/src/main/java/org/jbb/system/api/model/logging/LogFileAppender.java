/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.api.model.logging;

import org.apache.commons.lang3.Validate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogFileAppender implements LogAppender {
    private String name;
    private String currentLogFileName;
    private String rotationFileNamePattern;
    private FileSize maxFileSize;
    private int maxHistory;
    private LogFilter filter;
    private String pattern;

    @Getter
    @Setter
    public static class FileSize {
        private int value;
        private Unit unit;

        public static FileSize valueOf(String fileSize) {
            Validate.notEmpty(fileSize);

            String sizeWithoutSpaces = fileSize.replaceAll("\\s", "");
            FileSize result = new FileSize();

            Integer value = Integer.valueOf(sizeWithoutSpaces.substring(0, sizeWithoutSpaces.length() - 2));
            result.setValue(value);

            String unit = sizeWithoutSpaces.substring(sizeWithoutSpaces.length() - 2, sizeWithoutSpaces.length());
            result.setUnit(Unit.valueOf(unit.toUpperCase()));

            return result;
        }

        @Override
        public String toString() {
            return value + " " + unit.toString().toUpperCase();
        }

        public enum Unit {
            KB, MB, GB
        }
    }
}
