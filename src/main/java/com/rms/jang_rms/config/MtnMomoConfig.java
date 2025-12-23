package com.rms.jang_rms.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix="mtn.momo")
public class MtnMomoConfig {
    private String baseUrl;
    private String apiUser;
    private String apiKey;
    private String subscriptionKey;
    private String callbackUrl;
}
