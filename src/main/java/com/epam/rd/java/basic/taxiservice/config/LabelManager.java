package com.epam.rd.java.basic.taxiservice.config;

import java.util.ResourceBundle;

public class LabelManager {
    private final static ResourceBundle resourceBundle = ResourceBundle.getBundle("labels"); // класс извлекает информацию из файла labels.properties

    private LabelManager() {
    }
    public static String getProperty(String key) {
        return resourceBundle.getString(key);
    }
}