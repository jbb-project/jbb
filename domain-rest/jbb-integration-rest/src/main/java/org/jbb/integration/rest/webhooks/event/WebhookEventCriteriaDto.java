/*
 * Copyright (C) 2020 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.integration.rest.webhooks.event;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WebhookEventCriteriaDto {

    private String eventName;

    private String eventVersion;

    @Min(0)
    @ApiModelProperty(example = "0")
    private Integer page = 0;

    @Min(1)
    @Max(100)
    @ApiModelProperty(example = "20")
    private Integer pageSize = 20;

}
