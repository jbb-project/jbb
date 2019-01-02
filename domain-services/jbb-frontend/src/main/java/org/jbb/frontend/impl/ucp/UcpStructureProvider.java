/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.ucp;

import org.jbb.frontend.api.ucp.UcpStructure;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Getter
@Component
public class UcpStructureProvider {

    private final UcpStructure ucpStructure;

    public UcpStructureProvider() {
        ucpStructure = new UcpStructure.Builder()
                .add(new UcpStructure.Category.Builder().name("Overview").viewName("overview")
                        .add(UcpStructure.Element.of("Statistics", "statistics"))
                        .build())
                .add(new UcpStructure.Category.Builder().name("Profile").viewName("profile")
                        .add(UcpStructure.Element.of("Edit profile", "edit"))
                        .add(UcpStructure.Element.of("Edit account settings", "editAccount"))
                        .build())
                .build();
    }

}
