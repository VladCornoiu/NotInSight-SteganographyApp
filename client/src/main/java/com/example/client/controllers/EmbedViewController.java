package com.example.client.controllers;

import com.example.client.data.SteganographyDataModel;
import com.example.client.exception.CouldNotPerformStegoOperationException;
import com.example.client.service.StegoRestApi;
import com.example.model.io.GetStegoFileResponse;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.dom.DomEvent;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.frontend.installer.DefaultFileDownloader;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class EmbedViewController {

    private static final Logger logger = LoggerFactory.getLogger(EmbedViewController.class);

    @Getter
    protected boolean isReadyToUpload;

    @Getter
    protected boolean isReadyToDownload;

    private StegoRestApi stegoRestApi;
    private SteganographyDataModel steganographyDataModel;

    private String coverFileName;
    private String secretFileName;

    @Getter
    private String stegoFileName;

    public EmbedViewController(StegoRestApi stegoRestApi, SteganographyDataModel steganographyDataModel) {
        this.stegoRestApi = stegoRestApi;
        this.steganographyDataModel = steganographyDataModel;
    }

    public void uploadCoverFile(SucceededEvent succeededCoverFileUploadEvent) {
        coverFileName = succeededCoverFileUploadEvent.getFileName();
        if (secretFileName != null) {
            File coverFile = new File("clientside/uploads/coverfile/" + coverFileName);
            File secretFile = new File("clientside/uploads/secretFile/" + secretFileName);

            if (isJpgType(coverFileName) && secretFile.length() > coverFile.length() / 8) {
                Notification.show("Secret File size is too big or Cover File size is too small. Please readjust!", 8000, Notification.Position.TOP_CENTER);
                return;
            } else if (!isJpgType(coverFileName) && secretFile.length() > coverFile.length() / 30) {
                Notification.show("Secret File size is too big or Cover File size is too small. Please readjust!", 8000, Notification.Position.TOP_CENTER);
                return;
            }
            isReadyToUpload = true;
        }

    }

    public void uploadSecretFile(SucceededEvent succeededSecretFileUploadEvent) {
        secretFileName = succeededSecretFileUploadEvent.getFileName();
        if (coverFileName != null) {

            File coverFile = new File("clientside/uploads/coverfile/" + coverFileName);
            File secretFile = new File("clientside/uploads/secretFile/" + secretFileName);

            if (secretFile.length() > coverFile.length() / 8) {
                Notification.show("Secret File size is too big or Cover File size is too small. Please readjust!", 8000, Notification.Position.TOP_CENTER);
                return;
            }
            isReadyToUpload = true;
        }
    }

    public void removeCoverFile(DomEvent domEvent) {
        coverFileName = null;
        isReadyToUpload = false;
    }

    public void removeSecretFile(DomEvent domEvent) {
        secretFileName = null;
        isReadyToUpload = false;
    }

    public void processSubmit() {
        GetStegoFileResponse stegoFileResponse = null;
        try {
            stegoFileResponse = steganographyDataModel.getStegoFile(coverFileName, secretFileName);
        } catch (CouldNotPerformStegoOperationException ex) {
            Notification.show("Sorry! The operation could not be performed. Probably due to file sizes", 5000, Notification.Position.TOP_CENTER);
            return;
        }

        try {
            Files.createDirectories(Paths.get("clientside/downloads/stegofile/").toAbsolutePath().normalize());
        } catch (IOException e) {
            e.printStackTrace();
        }

        File file = new File("clientside/downloads/stegofile/" + stegoFileResponse.getFileName());
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(stegoFileResponse.getData());
        } catch (IOException e) {
            e.printStackTrace();
        }

        stegoFileName = stegoFileResponse.getFileName();
        Notification.show("Successfully transformed to a StegoFile. Click to download", 5000, Notification.Position.TOP_CENTER);
    }

    public InputStream createResource() {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File("clientside/downloads/stegofile/" + stegoFileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return fis;
    }

    public StreamResource getStreamResource(String stegoFileName) {
        return new StreamResource(stegoFileName, () -> {
            try {
                return new BufferedInputStream(new FileInputStream(new File("clientside/downloads/stegofile/" + stegoFileName)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            return null;
        });
    }

    private boolean isJpgType(String fileName) {
        return fileName.contains("jpg") || fileName.contains("jfif") || fileName.contains("exif");
    }
}
