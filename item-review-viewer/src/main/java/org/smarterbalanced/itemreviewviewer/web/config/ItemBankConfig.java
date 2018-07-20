package org.smarterbalanced.itemreviewviewer.web.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ItemBankConfig {
    private static final Logger logger = LoggerFactory.getLogger(ItemBankConfig.class);

    public static String get(String key) {
        String configLocation = ItemBankConfig.class.getResource("/application.properties").getPath();
        Properties properties = new Properties();
        FileInputStream configInput;
        try {
            configInput = new FileInputStream(configLocation);
            properties.load(configInput);
            configInput.close();
        } catch (IOException exception) {
            logger.warn("Unable to load config file");
            //TODO: handle exception
        }
        return properties.getProperty(key);
    }
}
