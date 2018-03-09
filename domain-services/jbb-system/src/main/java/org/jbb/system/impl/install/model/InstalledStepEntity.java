/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.install.model;

import org.hibernate.envers.Audited;
import org.jbb.lib.db.domain.BaseEntity;
import org.jbb.system.api.install.InstalledStep;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;

@Getter
@Setter
@Entity
@Audited
@Table(name = "JBB_SYSTEM_INSTALLED_STEPS")
@Builder
@EqualsAndHashCode(callSuper = true)
public class InstalledStepEntity extends BaseEntity implements InstalledStep {

    @NotBlank
    private String name;

    @NotBlank
    @Column(name = "from_version")
    private String fromVersion;

    @NotNull
    @Column(name = "installed_date_time")
    private LocalDateTime installedDateTime;

    @Tolerate
    InstalledStepEntity() {
        // for JPA
    }

}
