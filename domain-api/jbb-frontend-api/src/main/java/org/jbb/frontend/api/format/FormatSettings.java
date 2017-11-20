/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.api.format;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jbb.frontend.api.format.validation.ValidDateFormat;
import org.jbb.frontend.api.format.validation.ValidDurationFormat;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FormatSettings {

    @ValidDateFormat
    @NotBlank
    private String dateFormat;

    @ValidDurationFormat
    @NotBlank
    private String durationFormat;

}
