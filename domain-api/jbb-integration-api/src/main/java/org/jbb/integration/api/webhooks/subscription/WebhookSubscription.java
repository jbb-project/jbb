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

import org.hibernate.validator.constraints.URL;

import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
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
public class WebhookSubscription {

    @NotBlank
    private Long subscriptionId;

    @NotNull
    private Boolean enabled;

    @URL
    @NotBlank
    private String url;

    @NotNull
    @Valid
    private SubscribedEventTypesPolicy subscribedEventTypes;

    @NotNull
    private Map<@NotEmpty String, @NotEmpty String> headers;

}
