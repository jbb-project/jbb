package org.jbb.system.impl.cache.logic.provider;

import com.hazelcast.config.Config;
import lombok.RequiredArgsConstructor;
import org.jbb.lib.cache.HazelcastConfigFilesManager;
import org.jbb.system.api.cache.CacheProvider;
import org.jbb.system.api.cache.CacheSettings;
import org.jbb.system.api.cache.HazelcastServerSettings;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HazelcastServerProviderManager implements
    CacheProviderManager<HazelcastServerSettings> {

    public static final String PROVIDER_PROPERTY_VALUE = "hazelcast-server";

    private final HazelcastConfigFilesManager hazelcastConfigFilesManager;

    @Override
    public CacheProvider getProviderName() {
        return CacheProvider.HAZELCAST_SERVER;
    }

    @Override
    public HazelcastServerSettings getCurrentProviderSettings() {
        Config hazelcastServerConfig = hazelcastConfigFilesManager.getHazelcastServerConfig();

        HazelcastServerSettings settings = new HazelcastServerSettings();
        settings.setGroupName(hazelcastServerConfig.getGroupConfig().getName());
        settings.setGroupPassword(hazelcastServerConfig.getGroupConfig().getPassword());
        settings.setMembers(
            hazelcastServerConfig.getNetworkConfig().getJoin().getTcpIpConfig().getMembers());
        settings.setServerPort(hazelcastServerConfig.getNetworkConfig().getPort());

        return settings;
    }

    @Override
    public void setProviderSettings(CacheSettings newCacheSettings) {
        HazelcastServerSettings newHazelcastServerSettings = newCacheSettings
            .getHazelcastServerSettings();

        //todo

    }
}
