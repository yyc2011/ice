package com.ice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ice.ai")
public class AiProperties {

    private String baseUrl = "http://localhost:8000";
    private String internalToken = "dev-internal-token";
    private int timeoutMs = 15_000;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getInternalToken() {
        return internalToken;
    }

    public void setInternalToken(String internalToken) {
        this.internalToken = internalToken;
    }

    public int getTimeoutMs() {
        return timeoutMs;
    }

    public void setTimeoutMs(int timeoutMs) {
        this.timeoutMs = timeoutMs;
    }
}
