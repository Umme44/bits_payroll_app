package com.bits.hr.config;

import com.bits.hr.domain.AitConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.IOException;
import org.springframework.core.io.ClassPathResource;

/**
 * @author lemon
 */

public class YamlConfig {

    public static <T> T read(String classPathFileName, Class<T> clazz) {
        try {
            return yamlParser().readValue(new ClassPathResource(classPathFileName).getInputStream(), clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ObjectMapper yamlParser() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.KEBAB_CASE);
        return mapper;
    }

    public static <T> T readFromDatabase(AitConfig aitConfig, Class<T> clazz) {
        try {
            return yamlParser().readValue(aitConfig.getTaxConfig(), clazz);
            //                readValue(new ClassPathResource(classPathFileName).getInputStream(), clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
