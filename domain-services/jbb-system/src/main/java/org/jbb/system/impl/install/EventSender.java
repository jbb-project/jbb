/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.install;

import org.jbb.lib.commons.JbbMetaData;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.system.event.InstallUpgradePerformedEvent;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EventSender {

    private final JbbMetaData jbbMetaData;
    private final JbbEventBus eventBus;

    public void sentInstallEvent() {
        eventBus.post(new InstallUpgradePerformedEvent(jbbMetaData.jbbVersion()));
    }


}
