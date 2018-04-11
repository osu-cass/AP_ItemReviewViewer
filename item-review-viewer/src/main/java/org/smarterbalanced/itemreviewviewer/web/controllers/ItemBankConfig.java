package org.smarterbalanced.itemreviewviewer.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@PropertySource(value={"classpath:application.properties","classpath:application.config.properties"})
@Configuration
@Component
public class ItemBankConfig {
    @Value(value = "${content.source.dev}")
    private String contentSourceDev;

    @Value(value = "${content.source.prod}")
    private String contentSourceProd;

    @Autowired
    private Environment env;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    public String getContentSource() {

        if(env.getProperty("env") != null){
            if(env.getProperty("env").equals("dev")) {
                return contentSourceDev;
            } else {
                return contentSourceProd;
            }
        }
        return contentSourceProd;
    }
}
