package com.bits.hr.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UrlUtil {

    @Value("${spring.application.base-url}")
    private static String baseUrl;

    public UrlUtil(@Value("${spring.application.base-url}") String base) {
        baseUrl = base;
    }

    public static String getServerUrl(String relativePath) {
        return baseUrl + relativePath;
    }
}
