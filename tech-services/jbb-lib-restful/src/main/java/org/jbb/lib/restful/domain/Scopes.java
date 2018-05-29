/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.restful.domain;

public enum Scopes {
    BOARD_READ("board_read", "Read board structure"),
    BOARD_READ_WRITE("board_read_write", "Read & write board structure"),
    BOARD_SETTINGS_READ("board_settings_read", "Read board settings"),
    BOARD_SETTINGS_READ_WRITE("board_settings_read_write", "Read & write board settings");

    private final String name;
    private final String description;

    Scopes(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
