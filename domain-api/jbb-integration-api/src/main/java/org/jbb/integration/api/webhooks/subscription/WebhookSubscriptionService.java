/*
 * Copyright (C) 2020 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.integration.api.webhooks.subscription;

import org.jbb.integration.api.webhooks.event.EventType;
import org.springframework.data.domain.Page;

import java.util.List;

public interface WebhookSubscriptionService {

    WebhookSubscription createSubscription(CreateUpdateWebhookSubscription webhookSubscription);

    WebhookSubscription updateSubscription(Long subscriptionId, CreateUpdateWebhookSubscription webhookSubscription);

    void deleteSubscription(Long webhookSubscriptionId);

    WebhookSubscription getSubscription(Long webhookSubscriptionId);

    Page<WebhookSubscription> getSubscription(SubscriptionSearchCriteria criteria);

    List<WebhookSubscription> getEnabledSubscriptionsForEventType(EventType eventType);
}
