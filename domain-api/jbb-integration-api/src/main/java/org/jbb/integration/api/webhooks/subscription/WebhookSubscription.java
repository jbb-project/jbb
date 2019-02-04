/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.integration.api.webhooks.subscription;

import java.util.Map;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WebhookSubscription extends CreateUpdateWebhookSubscription {

    @NotBlank
    private Long subscriptionId;

    @Builder(builderMethodName = "subscriptionBuilder")
    public WebhookSubscription(Boolean enabled, String url,
                               SubscribedEventTypesPolicy subscribedEventTypes,
                               Map<String, String> headers, Long subscriptionId) {
        super(enabled, url, subscribedEventTypes, headers);
        this.subscriptionId = subscriptionId;
    }
}
