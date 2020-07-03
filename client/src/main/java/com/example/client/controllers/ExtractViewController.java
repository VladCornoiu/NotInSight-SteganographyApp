package com.example.client.controllers;

import com.example.client.data.SteganographyDataModel;
import com.example.client.service.StegoRestApi;
import com.example.model.io.GetSecretFileResponse;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.dom.DomEvent;
import com.vaadin.flow.server.StreamResource;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ExtractViewController  {

    private static final Logger logger = LoggerFactory.getLogger(ExtractViewController.class);

    private String stegoFileName;

    @Getter
    private String secretFileName;

    @Getter
    protected boolean isReadyToUpload;

    private StegoRestApi stegoRestApi;
    private SteganographyDataModel steganographyDataModel;

    public ExtractViewController(StegoRestApi stegoRestApi, SteganographyDataModel steganographyDataModel) {
        this.stegoRestApi = stegoRestApi;
        this.steganographyDataModel = steganographyDataModel;
    }

    public void uploadStegoFile(SucceededEvent succeededStegoFileUploadEvent) {
        stegoFileName = succeededStegoFileUploadEvent.getFileName();
        isReadyToUpload = true;
    }

    public void removeStegoFile(DomEvent domEvent) {
        stegoFileName = null;
        isReadyToUpload = false;
    }

    public void processSubmit() {
        GetSecretFileResponse secretFileResponse = steganographyDataModel.getSecretFile(stegoFileName);
        try {
            Files.createDirectories(Paths.get("clientside/downloads/secretfile/").toAbsolutePath().normalize());
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file = new File("clientside/downloads/secretfile/" + secretFileResponse.getFileName());

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(secretFileResponse.getData());
        } catch (IOException e) {
            e.printStackTrace();
        }

        secretFileName = secretFileResponse.getFileName();
        Notification.show("Successfully recovered secret file. Click to download", 5000, Notification.Position.TOP_CENTER);
    }

    public InputStream createResource() {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File("clientside/downloads/secretfile/" + secretFileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return fis;
    }

    public StreamResource getStreamResource(String secretFileName) {
        return new StreamResource(secretFileName, () -> {
            try {
                return new BufferedInputStream(new FileInputStream(new File("clientside/downloads/secretfile/" + secretFileName)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            return null;
        });
    }
}
