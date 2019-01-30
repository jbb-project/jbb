/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.integration.impl.webhooks;

import org.aeonbits.owner.Config;
import org.jbb.lib.properties.ModuleProperties;

@Config.HotReload(type = Config.HotReloadType.ASYNC)
@Config.Sources({"file:${jbb.home}/config/integration.properties"})
public interface WebhookProperties extends ModuleProperties { // NOSONAR (key names should stay)
    String WEBHOOK_CLEAN_AFTER_DAYS_KEY = "webhooks.clean.afterDays";
    String WEBHOOK_RETRY_AMOUNT_KEY = "webhooks.retry.amount";

    @Key(WEBHOOK_CLEAN_AFTER_DAYS_KEY)
    Integer cleanUpAfterDays();

    @Key(WEBHOOK_RETRY_AMOUNT_KEY)
    Integer numberOfRetries();
}