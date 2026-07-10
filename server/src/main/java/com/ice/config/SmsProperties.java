package com.ice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ice.sms")
public class SmsProperties {

    private boolean mockEnabled = true;
    private String mockCode = "123456";
    private String secretId = "";
    private String secretKey = "";
    private String signName = "";
    private String templateId = "";
    private int codeExpireSeconds = 300;
    private int sendCooldownSeconds = 60;
    private int dailyLimit = 10;

    public boolean isMockEnabled() {
        return mockEnabled;
    }

    public void setMockEnabled(boolean mockEnabled) {
        this.mockEnabled = mockEnabled;
    }

    public String getMockCode() {
        return mockCode;
    }

    public void setMockCode(String mockCode) {
        this.mockCode = mockCode;
    }

    public String getSecretId() {
        return secretId;
    }

    public void setSecretId(String secretId) {
        this.secretId = secretId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public int getCodeExpireSeconds() {
        return codeExpireSeconds;
    }

    public void setCodeExpireSeconds(int codeExpireSeconds) {
        this.codeExpireSeconds = codeExpireSeconds;
    }

    public int getSendCooldownSeconds() {
        return sendCooldownSeconds;
    }

    public void setSendCooldownSeconds(int sendCooldownSeconds) {
        this.sendCooldownSeconds = sendCooldownSeconds;
    }

    public int getDailyLimit() {
        return dailyLimit;
    }

    public void setDailyLimit(int dailyLimit) {
        this.dailyLimit = dailyLimit;
    }
}
