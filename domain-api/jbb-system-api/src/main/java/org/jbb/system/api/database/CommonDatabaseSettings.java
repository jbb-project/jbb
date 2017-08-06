package org.jbb.system.api.database;

import javax.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonDatabaseSettings {

    @Min(1)
    private int minimumIdleConnections;

    @Min(1)
    private int maximumPoolSize;

    @Min(0)
    private int connectionTimeoutMilliseconds;

    @Min(0)
    private int connectionMaxLifetimeMilliseconds;

    @Min(0)
    private int idleTimeoutMilliseconds;

    @Min(0)
    private int validationTimeoutMilliseconds;

    @Min(0)
    private int leakDetectionThresholdMilliseconds;

    private boolean failAtStartingImmediately;

    private boolean dropDatabaseAtStart;

    private boolean auditEnabled;

}
