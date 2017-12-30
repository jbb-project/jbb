/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.api.base;

import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class ProfileDataToChangeTest {

    private ProfileDataToChange profileDataToChange = new ProfileDataToChange();

    @Test
    public void shouldGetDisplayedName_afterSet() throws Exception {
        // given
        DisplayedName expectedDisplayedName = DisplayedName.builder().value("John").build();

        // when
        profileDataToChange.setDisplayedName(Optional.of(expectedDisplayedName));
        Optional<DisplayedName> displayedName = profileDataToChange.getDisplayedName();

        // then
        assertThat(displayedName.get()).isEqualTo(expectedDisplayedName);
    }

}