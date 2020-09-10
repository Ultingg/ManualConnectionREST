package ru.isaykin.reader;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

@Slf4j
public class PropetiesRepo {
    private static final String PATH_TO_PROPERTIES = "src\\main\\resources\\config.properties";

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
            log.debug("Properties loaded");
        } catch (IOException e) {
            log.error("Properties doesn't loaded" + e.getMessage());
        }
    }

}
