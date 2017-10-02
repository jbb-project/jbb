/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.commons;

import java.util.UUID;
import lombok.experimental.UtilityClass;
import org.slf4j.MDC;

@UtilityClass
public class RequestIdUtils {

    public static final String REQUEST_ID_KEY = "RequestId";

    public String generateNewRequestId() {
        MDC.put(REQUEST_ID_KEY, UUID.randomUUID().toString());
        return getCurrentRequestId();
    }

    public String getCurrentRequestId() {
        return MDC.get(REQUEST_ID_KEY);
    }

    public void cleanRequestId() {
        MDC.remove(REQUEST_ID_KEY);
    }

}
