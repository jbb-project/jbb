package org.jbb.system.impl.cache.logic.provider;

import org.jbb.system.api.cache.CacheProvider;
import org.jbb.system.api.cache.CacheProviderSettings;
import org.jbb.system.api.cache.CacheSettings;

public abstract class CacheProviderManager<T extends CacheProviderSettings> {

    public abstract CacheProvider getProviderName();

    public abstract T getCurrentProviderSettings();

    public abstract void setProviderSettings(CacheSettings newCacheSettings);

    public void setAsCurrentProvider(CacheSettings newCacheSettings) {
        newCacheSettings.setCurrentCacheProvider(getProviderName());
    }

}
