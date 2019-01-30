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

import com.google.common.collect.Maps;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
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
public class WebhookDelivery {

    @NotNull
    private Long deliveryId;

    @NotNull
    private String eventId;

    @NotNull
    private String url;

    @NotNull
    @Builder.Default
    private Map<String, String> headers = Maps.newTreeMap();

    @NotBlank
    private String content;

    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    @Builder.Default
    private List<@Valid WebhookDeliveryAttempt> attempts = new ArrayList<>();

    public DeliveryStatus getDeliveryStatus() {
        return attempts.stream().max(Comparator.comparing(WebhookDeliveryAttempt::getStartedAt))
                .map(WebhookDeliveryAttempt::getAttemptStatus)
                .orElse(DeliveryStatus.PENDING);
    }

    public LocalDateTime getLastUpdatedAt() {
        return attempts.stream().max(Comparator.comparing(WebhookDeliveryAttempt::getLastUpdatedAt))
                .map(WebhookDeliveryAttempt::getLastUpdatedAt)
                .orElse(createdAt);
    }

}
