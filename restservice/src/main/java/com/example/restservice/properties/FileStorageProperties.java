package com.example.restservice.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="stego")
public class FileStorageProperties {

    private String coverFileUploadDir;
    private String secretFileUploadDir;
    private String stegoFileUploadDir;
    private String stegoFileDownloadDir;
    private String secretFileDownloadDir;

    public String getCoverFileUploadDir() {
        return coverFileUploadDir;
    }

    public void setCoverFileUploadDir(String coverFileUploadDir) {
        this.coverFileUploadDir = coverFileUploadDir;
    }

    public String getSecretFileUploadDir() {
        return secretFileUploadDir;
    }

    public void setSecretFileUploadDir(String secretFileUploadDir) {
        this.secretFileUploadDir = secretFileUploadDir;
    }

    public String getStegoFileUploadDir() {
        return stegoFileUploadDir;
    }

    public void setStegoFileUploadDir(String stegoFileUploadDir) {
        this.stegoFileUploadDir = stegoFileUploadDir;
    }

    public String getStegoFileDownloadDir() {
        return stegoFileDownloadDir;
    }

    public void setStegoFileDownloadDir(String stegoFileDownloadDir) {
        this.stegoFileDownloadDir = stegoFileDownloadDir;
    }

    public String getSecretFileDownloadDir() {
        return secretFileDownloadDir;
    }

    public void setSecretFileDownloadDir(String secretFileDownloadDir) {
        this.secretFileDownloadDir = secretFileDownloadDir;
    }
}
