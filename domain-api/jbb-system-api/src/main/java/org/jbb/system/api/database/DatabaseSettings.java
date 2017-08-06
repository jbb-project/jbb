package org.jbb.system.api.database;

import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.jbb.system.api.database.h2.H2ManagedServerSettings;

@Getter
@Setter
@Builder
public class DatabaseSettings {

    @NotNull
    private CommonDatabaseSettings commonSettings;

    @NotNull
    private H2ManagedServerSettings h2ManagedServerSettings;

    @NotNull
    private DatabaseProvider currentDatabaseProvider;

}
