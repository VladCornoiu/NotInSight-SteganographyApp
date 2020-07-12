package com.example.client.service;

import com.example.client.properties.ClientFileStorageProperties;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ClientStegoFileService {

    @Getter
    private Path coverFileUploadStorageLocation;
    @Getter
    private Path secretFileUploadStorageLocation;
    @Getter
    private Path stegoFileUploadStorageLocation;
    @Getter
    private Path stegoFileDownloadStorageLocation;
    @Getter
    private Path secretFileDownloadStorageLocation;

    @Autowired
    public ClientStegoFileService(ClientFileStorageProperties clientFileStorageProperties) {
        this.coverFileUploadStorageLocation = Paths.get(clientFileStorageProperties.getCoverFileUploadDir()).toAbsolutePath().normalize();
        this.secretFileUploadStorageLocation = Paths.get(clientFileStorageProperties.getSecretFileUploadDir()).toAbsolutePath().normalize();
        this.stegoFileUploadStorageLocation = Paths.get(clientFileStorageProperties.getStegoFileUploadDir()).toAbsolutePath().normalize();
        this.stegoFileDownloadStorageLocation = Paths.get(clientFileStorageProperties.getStegoFileDownloadDir()).toAbsolutePath().normalize();
        this.secretFileDownloadStorageLocation = Paths.get(clientFileStorageProperties.getSecretFileDownloadDir()).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.coverFileUploadStorageLocation);
            Files.createDirectories(this.secretFileUploadStorageLocation);
            Files.createDirectories(this.stegoFileDownloadStorageLocation);
            Files.createDirectories(this.secretFileDownloadStorageLocation);
            Files.createDirectories(this.stegoFileUploadStorageLocation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
