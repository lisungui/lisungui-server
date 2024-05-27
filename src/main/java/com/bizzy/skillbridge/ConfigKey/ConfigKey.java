package com.bizzy.skillbridge.ConfigKey;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigKey {

    private Properties properties;

    public ConfigKey(String filePath) {
        properties = new Properties();
        try (FileInputStream input = new FileInputStream(filePath)) {
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String getEnvValue(String key) {
        return properties.getProperty(key);
    }
    
}
