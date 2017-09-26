/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.web.faq.data;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.jbb.frontend.api.faq.FaqEntry;

@Getter
@Setter
@Builder
public class FaqEntryData implements FaqEntry {

    private Long id;

    private String question;

    private String answer;

}
