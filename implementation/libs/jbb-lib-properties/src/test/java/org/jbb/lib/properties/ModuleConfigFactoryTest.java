package org.jbb.lib.properties;

import org.junit.Test;

import static org.aeonbits.owner.Config.LoadPolicy;
import static org.aeonbits.owner.Config.LoadType;
import static org.aeonbits.owner.Config.Sources;
import static org.assertj.core.api.Assertions.assertThat;

public class ModuleConfigFactoryTest {

    @Test
    public void shouldUseValuesFromFileOnClasspath() throws Exception {
        // when
        ExampleConfig exampleConfig = ModuleConfigFactory.create(ExampleConfig.class);

        // then
        assertThat(exampleConfig.foo()).isEqualTo("value1");
        assertThat(exampleConfig.bar()).isEqualTo("value2");
    }

    @LoadPolicy(LoadType.MERGE)
    @Sources({"classpath:test.properties"})
    private interface ExampleConfig extends ModuleConfig {

        String foo();

        String bar();
    }
}
