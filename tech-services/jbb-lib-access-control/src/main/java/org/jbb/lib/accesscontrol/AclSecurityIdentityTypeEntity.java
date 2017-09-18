/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.accesscontrol;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.jbb.lib.db.domain.BaseEntity;

@Getter
@Setter
@Entity
@Audited
@Table(name = "JBB_ACL_SECURITY_IDENTITY_TYPES")
@Builder
@EqualsAndHashCode(callSuper = true)
public class AclSecurityIdentityTypeEntity extends BaseEntity {

    @NotNull
    private String name;

}
