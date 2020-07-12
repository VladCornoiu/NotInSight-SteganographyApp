package com.example.client.controllers;

import com.example.client.data.SteganographyDataModel;
import com.example.client.service.ClientStegoFileService;
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

public class ExtractViewController  {

    private static final Logger logger = LoggerFactory.getLogger(ExtractViewController.class);

    ClientStegoFileService clientStegoFileService;

    private String stegoFileName;

    @Getter
    private String secretFileName;

    @Getter
    protected boolean isReadyToUpload;

    private StegoRestApi stegoRestApi;
    private SteganographyDataModel steganographyDataModel;

    public ExtractViewController(StegoRestApi stegoRestApi, SteganographyDataModel steganographyDataModel,
                                 ClientStegoFileService clientStegoFileService) {
        this.stegoRestApi = stegoRestApi;
        this.steganographyDataModel = steganographyDataModel;
        this.clientStegoFileService = clientStegoFileService;
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
        File file = new File(clientStegoFileService.getSecretFileDownloadStorageLocation().resolve(secretFileResponse.getFileName()).toString());

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(secretFileResponse.getData());
        } catch (IOException e) {
            e.printStackTrace();
        }

        secretFileName = secretFileResponse.getFileName();
        Notification.show("Successfully recovered secret file. Click to download", 5000, Notification.Position.TOP_CENTER);
    }

    public StreamResource getStreamResource(String secretFileName) {
        return new StreamResource(secretFileName, () -> {
            try {
                return new BufferedInputStream(new FileInputStream(new File(clientStegoFileService.getSecretFileDownloadStorageLocation().resolve(secretFileName).toString())));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            return null;
        });
    }
}
