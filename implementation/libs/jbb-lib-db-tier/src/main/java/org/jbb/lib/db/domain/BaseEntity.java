/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.db.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;

import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter
    private Long id;

    @Version
    @Column(name = "version")
    @Getter
    @Setter
    private Integer version = 0;

    @Column(name = "create_date_time")
    @Getter
    @Setter
    private LocalDateTime createDateTime;

    @Column(name = "update_date_time")
    @Getter
    @Setter
    private LocalDateTime updateDateTime;

    @PrePersist
    private void onCreate() {
        this.createDateTime = LocalDateTime.now();
        this.updateDateTime = this.createDateTime;
    }

    @PreUpdate
    private void onUpdate() {
        this.updateDateTime = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BaseEntity)) {
            return false;
        }
        BaseEntity that = (BaseEntity) o;
        return id != null ? id.equals(that.id) : that.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
