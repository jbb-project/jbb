/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.install.dao;

import java.util.List;
import org.jbb.system.impl.install.model.InstalledStepEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstalledStepRepository extends CrudRepository<InstalledStepEntity, Long> {

    @Query("SELECT i FROM InstalledStepEntity i ORDER BY i.installedDateTime ASC")
    List<InstalledStepEntity> findAllByOrderByInstalledDateTimeAsc();

}
