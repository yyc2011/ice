package com.ice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ice.auth")
public class AuthProperties {

    private String jwtSecret = "dev-jwt-secret-change-in-production";
    private long jwtExpireSeconds = 604800L;
    private String wxAppId = "";
    private String wxAppSecret = "";
    private String adminSeedPassword = "admin123";
    private boolean devLoginEnabled = true;

    public String getJwtSecret() {
        return jwtSecret;
    }

    public void setJwtSecret(String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    public long getJwtExpireSeconds() {
        return jwtExpireSeconds;
    }

    public void setJwtExpireSeconds(long jwtExpireSeconds) {
        this.jwtExpireSeconds = jwtExpireSeconds;
    }

    public String getWxAppId() {
        return wxAppId;
    }

    public void setWxAppId(String wxAppId) {
        this.wxAppId = wxAppId;
    }

    public String getWxAppSecret() {
        return wxAppSecret;
    }

    public void setWxAppSecret(String wxAppSecret) {
        this.wxAppSecret = wxAppSecret;
    }

    public String getAdminSeedPassword() {
        return adminSeedPassword;
    }

    public void setAdminSeedPassword(String adminSeedPassword) {
        this.adminSeedPassword = adminSeedPassword;
    }

    public boolean isDevLoginEnabled() {
        return devLoginEnabled;
    }

    public void setDevLoginEnabled(boolean devLoginEnabled) {
        this.devLoginEnabled = devLoginEnabled;
    }
}
