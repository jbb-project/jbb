/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.event;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.jbb.lib.eventbus.JbbEvent;

@Getter
@ToString
@RequiredArgsConstructor
public class MemberUnlockedEvent extends JbbEvent {

    @NotNull
    private final Long memberId;

}
