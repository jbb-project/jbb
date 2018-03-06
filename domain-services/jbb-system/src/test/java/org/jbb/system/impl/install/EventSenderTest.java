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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class EventSenderTest {

    @Mock
    private JbbMetaData jbbMetaDataMock;

    @Mock
    private JbbEventBus eventBusMock;

    @InjectMocks
    private EventSender eventSender;

    @Test
    public void shouldSendInstallUpgradePerformedEvent() {
        // when
        eventSender.sentInstallEvent();

        // then
        Mockito.verify(eventBusMock).post(any(InstallUpgradePerformedEvent.class));
    }
}