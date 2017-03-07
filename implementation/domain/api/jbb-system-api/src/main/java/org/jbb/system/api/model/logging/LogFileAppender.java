/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.api.model.logging;

import org.apache.commons.lang3.Validate;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogFileAppender implements LogAppender {
    @NotBlank
    @LogAppenderNameUnique(groups = AddingModeGroup.class)
    private String name;

    @NotBlank
    private String currentLogFileName;

    @NotBlank
    private String rotationFileNamePattern;

    @ValidFileSize
    private FileSize maxFileSize;

    @Min(0)
    private int maxHistory;

    private LogFilter filter;

    @NotBlank
    private String pattern;

    @Getter
    @Setter
    public static class FileSize {
        private int value;
        private Unit unit;

        public static FileSize valueOf(String fileSize) {
            FileSize result = new FileSize();

            try {
                Validate.notEmpty(fileSize);

                String sizeWithoutSpaces = fileSize.replaceAll("\\s", "");

                Integer value = Integer.valueOf(sizeWithoutSpaces.substring(0, sizeWithoutSpaces.length() - 2));
                result.setValue(value);

                String unit = sizeWithoutSpaces.substring(sizeWithoutSpaces.length() - 2, sizeWithoutSpaces.length());
                result.setUnit(Unit.valueOf(unit.toUpperCase()));

            } catch (StringIndexOutOfBoundsException | IllegalArgumentException e) {
                result.setValue(0);
                result.setUnit(null);
            }

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
