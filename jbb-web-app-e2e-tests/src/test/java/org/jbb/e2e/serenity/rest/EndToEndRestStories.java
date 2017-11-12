/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.rest;

import net.thucydides.core.annotations.Steps;
import org.jbb.e2e.serenity.rest.commons.AssertRestSteps;
import org.jbb.e2e.serenity.rest.commons.AuthRestSteps;
import org.jbb.e2e.serenity.web.EndToEndWebStories;

public abstract class EndToEndRestStories extends EndToEndWebStories {

    @Steps
    protected AssertRestSteps assertRestSteps;

    @Steps
    protected AuthRestSteps authRestSteps;
}
