package com.solar.config;

import java.util.ResourceBundle;

/**
 * @author hushaoge
 * @date 2018/7/4 15:19
 */
public class PropsConfig {

    /**
     * 根据key获取value
     * @param fileName
     * @param key
     * @return
     */
    public static String getValueByKey(String fileName, String key) {
        try {
            ResourceBundle rb = ResourceBundle.getBundle(fileName);
            String value = rb.getString(key);
            if(value == null || value.length() == 0) {
                return "";
            } else {
                return value;
            }
        } catch (Exception e) {
            return "";
        }
    }
}
