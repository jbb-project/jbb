/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.rest.commons;

import net.serenitybdd.core.Serenity;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;

import org.assertj.core.api.Condition;

import java.util.List;

import io.restassured.response.Response;

import static org.assertj.core.api.Assertions.assertThat;

public class AssertSseRestSteps extends ScenarioSteps {

    public void assert_getting_event_with_name(Response sseStream, String eventName) {
        List<MessageEvent> messageEvents = EventStreamParser.parser().parseEvents(sseStream.asString());
        Serenity.setSessionVariable("sseEvents").to(messageEvents);
        assert_getting_one_event(eventName);
    }

    @Step
    private void assert_getting_one_event(String eventName) {
        List<MessageEvent> messageEvents = Serenity.sessionVariableCalled("sseEvents");
        assertThat(messageEvents).areExactly(1, new Condition<>(
                event -> eventName.equals(event.getEventName()), "Event name matching"
        ));
    }

}
