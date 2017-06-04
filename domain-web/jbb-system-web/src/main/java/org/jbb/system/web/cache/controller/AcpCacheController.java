/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.web.cache.controller;

import org.jbb.system.api.model.cache.CacheSettings;
import org.jbb.system.api.service.CacheSettingsService;
import org.jbb.system.web.cache.data.FormCacheSettings;
import org.jbb.system.web.cache.form.CacheSettingsForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/acp/general/cache")
public class AcpCacheController {
    private static final String VIEW_NAME = "acp/general/cache";
    private static final String CACHE_SETTINGS_FORM = "cacheSettingsForm";
    private static final String FORM_SAVED_FLAG = "cacheSettingsFormSaved";

    private final CacheSettingsService cacheSettingsService;

    @Autowired
    public AcpCacheController(CacheSettingsService cacheSettingsService) {
        this.cacheSettingsService = cacheSettingsService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String cacheSettingsGet(Model model) {
        CacheSettings cacheSettings = cacheSettingsService.getCacheSettings();

        if (!model.containsAttribute(CACHE_SETTINGS_FORM)) {
            CacheSettingsForm form = new CacheSettingsForm();
            form.setApplicationCacheEnabled(cacheSettings.isApplicationCacheEnabled());
            form.setSecondLevelCacheEnabled(cacheSettings.isSecondLevelCacheEnabled());
            form.setQueryCacheEnabled(cacheSettings.isQueryCacheEnabled());
            model.addAttribute(CACHE_SETTINGS_FORM, form);
        }

        return VIEW_NAME;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String cacheSettingsPost(Model model,
                                    @ModelAttribute(CACHE_SETTINGS_FORM) CacheSettingsForm form,
                                    BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            log.debug("Cache settings form error detected: {}", bindingResult.getAllErrors());
            model.addAttribute(FORM_SAVED_FLAG, false);
            return VIEW_NAME;
        }

        cacheSettingsService.setCacheSettings(new FormCacheSettings(form));

        redirectAttributes.addFlashAttribute(FORM_SAVED_FLAG, true);
        return "redirect:/" + VIEW_NAME;
    }
}
