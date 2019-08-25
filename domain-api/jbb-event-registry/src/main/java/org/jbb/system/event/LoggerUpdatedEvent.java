/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.event;

import org.jbb.lib.eventbus.JbbEvent;
import org.jbb.lib.eventbus.LoggingRelatedEvent;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString(callSuper = true)
public class LoggerUpdatedEvent extends JbbEvent implements LoggingRelatedEvent {

    @NotBlank
    private final String loggerName;

}
