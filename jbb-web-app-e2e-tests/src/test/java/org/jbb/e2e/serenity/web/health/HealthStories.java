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

import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.WithTagValuesOf;

import org.jbb.e2e.serenity.Tags.Feature;
import org.jbb.e2e.serenity.Tags.Interface;
import org.jbb.e2e.serenity.Tags.Release;
import org.jbb.e2e.serenity.Tags.Type;
import org.jbb.e2e.serenity.web.EndToEndWebStories;
import org.junit.Test;

public class HealthStories extends EndToEndWebStories {

    @Steps
    HealthSteps healthSteps;

    @Test
    @WithTagValuesOf({Interface.WEB, Type.SMOKE, Feature.HEALTH_CHECK, Release.VER_0_11_0})
    public void health_check_page_should_present_healthy_state() {
        // when
        healthSteps.open_health_page();

        // then
        healthSteps.should_contains_info_about_healthy_board();
    }

}
