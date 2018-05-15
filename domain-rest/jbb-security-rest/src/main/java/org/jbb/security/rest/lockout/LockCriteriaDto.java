/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.rest.lockout;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LockCriteriaDto {

    private Long memberId;

    private Boolean active;

    @Min(0)
    @ApiModelProperty(example = "0")
    private Integer page = 0;

    @Min(1)
    @Max(100)
    @ApiModelProperty(example = "20")
    private Integer pageSize = 20;

}
