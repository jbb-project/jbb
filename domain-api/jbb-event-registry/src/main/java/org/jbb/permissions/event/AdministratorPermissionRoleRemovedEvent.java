/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.event;

import org.jbb.lib.eventbus.JbbEvent;
import org.jbb.lib.eventbus.webhooks.WebhookEvent;
import org.jbb.lib.eventbus.webhooks.WebhookField;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString(callSuper = true)
@WebhookEvent(name = "AdministratorPermissionRoleRemoved", versions = "1.0.0")
public class AdministratorPermissionRoleRemovedEvent extends JbbEvent {

    @NotNull
    @WebhookField(versions = "1.0.0")
    private final Long roleId;

}
