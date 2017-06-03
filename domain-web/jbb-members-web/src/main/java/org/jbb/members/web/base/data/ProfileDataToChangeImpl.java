/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.web.base.data;

import org.jbb.members.api.data.DisplayedName;
import org.jbb.members.api.data.ProfileDataToChange;

import java.util.Optional;

public class ProfileDataToChangeImpl implements ProfileDataToChange {
    private Optional<DisplayedName> displayedName;

    @Override
    public Optional<DisplayedName> getDisplayedName() {
        return displayedName;
    }

    public void setDisplayedName(DisplayedName displayedName) {
        this.displayedName = Optional.of(displayedName);
    }
}
