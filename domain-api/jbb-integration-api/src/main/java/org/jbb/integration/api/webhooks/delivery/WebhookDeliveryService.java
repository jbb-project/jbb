/*
 * Copyright (C) 2020 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.integration.api.webhooks.delivery;

import java.util.List;

public interface WebhookDeliveryService {

    List<WebhookDelivery> getDeliveries(String webhookEventId);

    WebhookDelivery getDelivery(Long webhookDeliveryId);

    WebhookDelivery retryDelivery(Long webhookDeliveryId);

    void deleteDelivery(Long webhookDeliveryId);

}
