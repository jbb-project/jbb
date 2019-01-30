/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.integration.impl.webhooks.event.model;

import com.google.common.collect.Maps;

import org.hibernate.annotations.Columns;
import org.hibernate.envers.Audited;
import org.jbb.lib.db.domain.BaseEntity;

import java.time.LocalDateTime;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
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
@Table(name = "JBB_INTEGRATION_WEBHOOK_EVENTS")
@Builder
@EqualsAndHashCode(callSuper = true)
public class WebhookEventEntity extends BaseEntity {

    @NotBlank
    @Column(name = "event_id")
    private String eventId;

    @NotBlank
    @Column(name = "event_name")
    private String eventName;

    @NotBlank
    @Column(name = "event_version")
    private String eventVersion;

    @NotNull
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @NotNull
    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Column(name = "source_request_id")
    private String sourceRequestId;

    @Column(name = "source_member_id")
    private Long sourceMemberId;

    @Column(name = "source_oauth_client_id")
    private String sourceOAuthClientId;

    @Column(name = "source_ip_address")
    private String sourceIpAddress;

    @Column(name = "source_session_id")
    private String sourceSessionId;

    @NotNull
    @ElementCollection
    @MapKeyColumn(name = "name")
    @Columns(columns = {
            @Column(name = "value_string"),
            @Column(name = "value_class_name")
    })
    @CollectionTable(name = "JBB_INTEGRATION_WEBHOOK_EVENTS_DETAILS", joinColumns = @JoinColumn(name = "event_pid"))
    private Map<String, WebhookEventDetailValue> details = Maps.newTreeMap();

    @Tolerate
    WebhookEventEntity() {
        // for JPA
    }

}
