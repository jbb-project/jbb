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

import static org.assertj.core.api.Assertions.assertThat;
import static org.jbb.e2e.serenity.Tags.Feature;
import static org.jbb.e2e.serenity.Tags.Release;
import static org.jbb.e2e.serenity.Tags.Type;

import net.thucydides.core.annotations.WithTagValuesOf;
import org.jbb.e2e.serenity.JbbBaseRestStories;
import org.jbb.e2e.serenity.Tags.Interface;
import org.junit.Test;

public class RegistrationRestStories extends JbbBaseRestStories {

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.REGISTRATION, Release.VER_0_10_0})
    public void testMemberGet() throws Exception {
        Page<MemberPublicDto> pageResult = prepareApiRequest()
            .basePath("api/v1/members")
            .when()
            .get()
            .as(Page.class);
        assertThat(pageResult.getContent()).isNotEmpty();

    }


}
