package com.ice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ice.upload")
public class UploadProperties {

    private String localDir = "./data/uploads";
    private String publicBasePath = "/api/v1/uploads/files";
    private int maxSizeMb = 5;

    public String getLocalDir() {
        return localDir;
    }

    public void setLocalDir(String localDir) {
        this.localDir = localDir;
    }

    public String getPublicBasePath() {
        return publicBasePath;
    }

    public void setPublicBasePath(String publicBasePath) {
        this.publicBasePath = publicBasePath;
    }

    public int getMaxSizeMb() {
        return maxSizeMb;
    }

    public void setMaxSizeMb(int maxSizeMb) {
        this.maxSizeMb = maxSizeMb;
    }
}
