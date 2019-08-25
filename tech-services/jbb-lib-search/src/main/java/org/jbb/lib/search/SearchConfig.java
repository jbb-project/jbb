/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.search;

import org.apache.solr.client.solrj.SolrClient;
import org.jbb.lib.commons.CommonsConfig;
import org.jbb.lib.properties.PropertiesConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;
import org.springframework.data.solr.server.support.EmbeddedSolrServerFactory;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

@Configuration
@ComponentScan
@EnableSolrRepositories("org.jbb")
@Import({CommonsConfig.class, PropertiesConfig.class})
public class SearchConfig {

    @Bean
    public SolrClient solrClient(SolrBootstrapper solrBootstrapper)
            throws IOException, SAXException, ParserConfigurationException {
        File solrDirectory = new File(solrBootstrapper.getSolrPath());
        EmbeddedSolrServerFactory factory = new EmbeddedSolrServerFactory(
                solrDirectory.getAbsolutePath());
        return factory.getSolrClient();
    }

    @Bean
    public SolrOperations solrTemplate(SolrBootstrapper solrBootstrapper)
            throws ParserConfigurationException, SAXException, IOException {
        return new SolrTemplate(solrClient(solrBootstrapper));
    }


}
