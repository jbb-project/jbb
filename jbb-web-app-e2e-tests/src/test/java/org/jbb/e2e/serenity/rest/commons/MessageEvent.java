/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.rest.commons;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageEvent {

    private final String id;

    private final String eventName;

    private final String data;

    public static MessageEvent of(String id, String eventName, String data) {
        return new MessageEvent(id, eventName, data);
    }
}
