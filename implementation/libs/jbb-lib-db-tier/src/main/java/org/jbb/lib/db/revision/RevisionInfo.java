/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.db.revision;

import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@RevisionEntity(NewRevisionListener.class)
public class RevisionInfo {
    @Id
    @GeneratedValue
    @RevisionNumber
    private int id;

    @Column(name = "rev_timestamp")
    @RevisionTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date revTimestamp;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "session_id")
    private String sessionId;
}
