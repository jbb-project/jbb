/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.integration.impl.webhooks.subscription.model;

import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.URL;
import org.jbb.integration.impl.webhooks.event.model.WebhookEventTypeValue;
import org.jbb.integration.impl.webhooks.subscription.model.validation.ValidSubscribedEventTypesPolicy;
import org.jbb.integration.impl.webhooks.subscription.model.validation.create.CreateGroup;
import org.jbb.integration.impl.webhooks.subscription.model.validation.create.UrlNotBusyCreate;
import org.jbb.integration.impl.webhooks.subscription.model.validation.update.UpdateGroup;
import org.jbb.integration.impl.webhooks.subscription.model.validation.update.UrlNotBusyUpdate;
import org.jbb.lib.db.domain.BaseEntity;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;

@Getter
@Setter
@Entity
@Audited
@Table(name = "JBB_INTEGRATION_WEBHOOK_SUBSCRIPTIONS")
@Builder
@EqualsAndHashCode(callSuper = true)
@UrlNotBusyUpdate(groups = UpdateGroup.class)
@ValidSubscribedEventTypesPolicy
public class WebhookSubscriptionEntity extends BaseEntity {

    @NotNull
    @Column(name = "enabled")
    private Boolean enabled;

    @URL
    @NotNull
    @UrlNotBusyCreate(groups = CreateGroup.class)
    @Column(name = "url")
    private String url;

    @NotNull
    @Column(name = "all_subscribed")
    private Boolean allSubscribed;

    @NotNull
    @Builder.Default
    @ElementCollection(targetClass = WebhookEventTypeValue.class)
    @CollectionTable(name = "JBB_INTEGRATION_WEBHOOK_SUBSCRIBED_EVENTS", joinColumns = {@JoinColumn(name = "subscription_id")})
    @Column(name = "subscribed_event_type")
    private Set<@Valid WebhookEventTypeValue> subscribedEventTypes = new HashSet<>();

    @NotNull
    @ElementCollection
    @MapKeyColumn(name = "name")
    @Column(name = "value")
    @CollectionTable(name = "JBB_INTEGRATION_WEBHOOK_SUBSCRIPTION_HEADERS", joinColumns = @JoinColumn(name = "subscription_id"))
    private Map<@NotEmpty String, @NotEmpty String> headers = new TreeMap<>();

    @Tolerate
    WebhookSubscriptionEntity() {
        // for JPA
    }

}
