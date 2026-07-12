package com.ice.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        AiProperties.class,
        AuthProperties.class,
        SmsProperties.class,
        UploadProperties.class
})
public class PropertiesConfig {
}
