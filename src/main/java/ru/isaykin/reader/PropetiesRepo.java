package ru.isaykin.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;


public class PropetiesRepo {
    private static final String PATH_TO_PROPERTIES = "src\\main\\resources\\config.properties";
    private static final Logger LOGGER = LoggerFactory.getLogger("ru.isaykin.PropertiesRepo");

    private static final Properties prop = new Properties();

    public static String getUrl() {
        return prop.getProperty("url");
    }

    public static String getUsername() {
        return prop.getProperty("username");
    }

    public static String getPassword() {
        return prop.getProperty("password");
    }

    public static String getExclePath() {
        return prop.getProperty("excelPath");
    }

    public static String getCsvPath() {
        return prop.getProperty("csvPath");
    }

    public static void getDataForPropRepo() {
        File file = new File(PATH_TO_PROPERTIES);
        try {
            prop.load(new FileReader(file));
            LOGGER.debug("Properties loaded");
        } catch (IOException e) {
            LOGGER.debug("Properties doesn't loaded" + e.getMessage());
        }
    }

}
