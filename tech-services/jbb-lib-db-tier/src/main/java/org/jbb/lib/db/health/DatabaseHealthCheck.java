/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.db.health;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jbb.lib.health.JbbHealthCheck;
import org.springframework.jdbc.IncorrectResultSetColumnCountException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseHealthCheck extends JbbHealthCheck {

    private static final String DEFAULT_QUERY = "SELECT 1";

    private final DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void createJdbcTemplate() {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public String getName() {
        return "Database status";
    }

    @Override
    protected Result check() throws Exception {
        try {
            jdbcTemplate.query(DEFAULT_QUERY, new SingleColumnRowMapper());
            return Result.healthy();
        } catch (Exception e) {
            log.error("Database health check failed", e);
            return Result.unhealthy(e);
        }
    }

    private static class SingleColumnRowMapper implements RowMapper<Object> {

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ResultSetMetaData metaData = rs.getMetaData();
            int columns = metaData.getColumnCount();
            if (columns != 1) {
                throw new IncorrectResultSetColumnCountException(1, columns);
            }
            return JdbcUtils.getResultSetValue(rs, 1);
        }

    }
}

