package org.jbb.system.web.cache.logic;

import org.jbb.system.api.cache.CacheProvider;
import org.jbb.system.api.cache.CacheSettings;
import org.jbb.system.web.cache.form.CacheSettingsForm;
import org.springframework.stereotype.Component;

@Component
public class FormCacheTranslator {

    public CacheSettingsForm fillCacheSettingsForm(CacheSettings cacheSettings,
        CacheSettingsForm form) {

        form.setApplicationCacheEnabled(cacheSettings.isApplicationCacheEnabled());
        form.setSecondLevelCacheEnabled(cacheSettings.isSecondLevelCacheEnabled());
        form.setQueryCacheEnabled(cacheSettings.isQueryCacheEnabled());
        form.setProviderName(cacheSettings.getCurrentCacheProvider().toString());

        return form;
    }

    public CacheSettings buildcacheSettings(CacheSettingsForm form) {
        return CacheSettings.builder()
            .applicationCacheEnabled(form.isApplicationCacheEnabled())
            .secondLevelCacheEnabled(form.isSecondLevelCacheEnabled())
            .queryCacheEnabled(form.isQueryCacheEnabled())
            .currentCacheProvider(CacheProvider.valueOf(form.getProviderName().toUpperCase()))
            .build();
    }

}
