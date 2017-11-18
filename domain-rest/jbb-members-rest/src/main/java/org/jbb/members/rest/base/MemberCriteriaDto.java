/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.rest.base;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberCriteriaDto {

    private String displayedName;

    @Min(0)
    @ApiModelProperty(example = "0")
    private Integer page;

    @Min(1)
    @Max(100)
    @ApiModelProperty(example = "20")
    private Integer pageSize;

}
