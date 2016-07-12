/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.entities;


import org.jbb.lib.core.vo.IPAddress;
import org.jbb.members.api.model.RegistrationInfo;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Data;

@Data
@Entity
@Table(name = "JBB_MEMBER_REGISTRATION_INFO")
@Builder
public class RegistrationInfoEntity implements RegistrationInfo, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "registrationDate")
    private LocalDateTime registrationDate;

    @Embedded
    @AttributeOverrides(@AttributeOverride(name = "value", column = @Column(name = "ipAddress")))
    private IPAddress ipAddress;
}
