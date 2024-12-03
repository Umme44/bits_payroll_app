package com.bits.hr.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.spel.standard.SpelExpressionParser;

@Configuration
public class SpringElConfig {

    @Bean
    public SpelExpressionParser spelExpressionParser() {
        return new SpelExpressionParser();
    }
}
