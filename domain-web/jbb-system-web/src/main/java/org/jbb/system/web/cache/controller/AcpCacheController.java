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

import org.jbb.lib.mvc.SimpleErrorsBindingMapper;
import org.jbb.system.api.cache.CacheConfigException;
import org.jbb.system.api.cache.CacheSettings;
import org.jbb.system.api.cache.CacheSettingsService;
import org.jbb.system.web.cache.form.CacheSettingsForm;
import org.jbb.system.web.cache.logic.FormCacheTranslator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/acp/general/cache")
public class AcpCacheController {
    private static final String VIEW_NAME = "acp/general/cache";
    private static final String CACHE_SETTINGS_FORM = "cacheSettingsForm";
    private static final String FORM_SAVED_FLAG = "cacheSettingsFormSaved";

    private final CacheSettingsService cacheSettingsService;
    private final FormCacheTranslator formTranslator;
    private final SimpleErrorsBindingMapper errorBindingMapper;

    @RequestMapping(method = RequestMethod.GET)
    public String cacheSettingsGet(Model model) {
        CacheSettings cacheSettings = cacheSettingsService.getCacheSettings();

        if (!model.containsAttribute(CACHE_SETTINGS_FORM)) {
            CacheSettingsForm form = new CacheSettingsForm();
            formTranslator.fillCacheSettingsForm(cacheSettings, form);
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

        CacheSettings currentSettings = cacheSettingsService.getCacheSettings();
        try {
            cacheSettingsService
                    .setCacheSettings(formTranslator.buildCacheSettings(form, currentSettings));
        } catch (CacheConfigException e) {
            errorBindingMapper.map(e.getConstraintViolations(), bindingResult);
            redirectAttributes.addFlashAttribute(FORM_SAVED_FLAG, false);
            return VIEW_NAME;
        }


        redirectAttributes.addFlashAttribute(FORM_SAVED_FLAG, true);
        return "redirect:/" + VIEW_NAME;
    }
}
