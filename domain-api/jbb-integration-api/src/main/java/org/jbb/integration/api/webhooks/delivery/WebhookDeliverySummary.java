/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.integration.api.webhooks.delivery;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WebhookDeliverySummary {

    @NotNull
    private Long deliveryId;

    @NotNull
    private String eventId;

    @NotNull
    private String url;

    @NotNull
    private LocalDateTime lastUpdatedAt;

    @NotNull
    private DeliveryStatus status;

}
