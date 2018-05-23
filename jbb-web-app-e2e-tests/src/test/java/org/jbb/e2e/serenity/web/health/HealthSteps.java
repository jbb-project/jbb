/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.web.health;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;

public class HealthSteps extends ScenarioSteps {

    HealthPage healthPage;

    @Step
    public void open_health_page() {
        healthPage.open();
    }

    @Step
    public void should_contains_info_about_healthy_board() {
        healthPage.containsHealthyStatus();
    }

}
