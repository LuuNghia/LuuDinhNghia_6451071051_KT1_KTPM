package com.example.utils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * Utility doc file cau hinh config.properties
 */
public class ConfigReader {
    private static Properties properties = new Properties();

    static {
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                properties.load(new InputStreamReader(input, StandardCharsets.UTF_8));
            } else {
                System.err.println("Khong tim thay file config.properties trong classpath!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public static int getIntProperty(String key, int defaultValue) {
        String val = getProperty(key);
        if (val != null) {
            try {
                return Integer.parseInt(val.trim());
            } catch (NumberFormatException ignored) {}
        }
        return defaultValue;
    }

    public static boolean getBooleanProperty(String key, boolean defaultValue) {
        String val = getProperty(key);
        if (val != null) {
            return Boolean.parseBoolean(val.trim());
        }
        return defaultValue;
    }
}
