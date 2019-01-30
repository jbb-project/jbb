/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.integration.rest;

import lombok.experimental.UtilityClass;

@UtilityClass
public class IntegrationRestConstants {

    public static final String LOOPBACK = "/loopback";

    public static final String WEBHOOK_EVENT_TYPES = "/webhook-event-types";

    public static final String WEBHOOK_EVENTS = "/webhook-events";
    public static final String EVENT_ID_VAR = "eventId";
    public static final String EVENT_ID = "/{" + EVENT_ID_VAR + "}";

    public static final String ACTION_RETRY_PARAM = "action=retry";

}
