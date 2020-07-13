package com.example.client.controllers;

import com.example.client.data.SteganographyDataModel;
import com.example.client.exception.CouldNotPerformStegoOperationException;
import com.example.client.service.ClientStegoFileService;
import com.example.client.service.StegoRestApi;
import com.example.model.io.GetStegoFileResponse;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.dom.DomEvent;
import com.vaadin.flow.server.StreamResource;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class EmbedViewController {

    private static final Logger logger = LoggerFactory.getLogger(EmbedViewController.class);

    ClientStegoFileService clientStegoFileService;

    @Getter
    private String recommendationText;

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

    public EmbedViewController(StegoRestApi stegoRestApi, SteganographyDataModel steganographyDataModel,
                               ClientStegoFileService clientStegoFileService) {
        this.stegoRestApi = stegoRestApi;
        this.steganographyDataModel = steganographyDataModel;
        this.clientStegoFileService = clientStegoFileService;
        this.recommendationText = "";
    }

    public void uploadCoverFile(SucceededEvent succeededCoverFileUploadEvent) {
        coverFileName = succeededCoverFileUploadEvent.getFileName();
        if (secretFileName != null) {
            File coverFile = new File(clientStegoFileService.getCoverFileUploadStorageLocation().resolve(coverFileName).toString());
            File secretFile = new File(clientStegoFileService.getSecretFileUploadStorageLocation().resolve(secretFileName).toString());

            try {
                BufferedImage bufferedImage = ImageIO.read(coverFile);
                long fileRgbSize = bufferedImage.getHeight() * bufferedImage.getWidth();

                if (secretFile.length() > fileRgbSize / 64) {// 8-ratio and 8 byte
                    Notification.show("Secret File size might be too big or Cover File size might be too small. The secret file might not fit.", 8000, Notification.Position.TOP_CENTER);
                }
            } catch (IOException e) {
                Notification.show("Something happened when reading cover Image. Please try again!", 8000, Notification.Position.TOP_CENTER);
            }
            isReadyToUpload = true;
        } else {
            File coverFile = new File(clientStegoFileService.getCoverFileUploadStorageLocation().resolve(coverFileName).toString());
            try {
                BufferedImage bufferedImage = ImageIO.read(coverFile);
                recommendationText = "Recommended Secret File size up to " + (bufferedImage.getWidth() * bufferedImage.getHeight() / 64) + " bytes";
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public void uploadSecretFile(SucceededEvent succeededSecretFileUploadEvent) {
        secretFileName = succeededSecretFileUploadEvent.getFileName();
        if (coverFileName != null) {

            File coverFile = new File(clientStegoFileService.getCoverFileUploadStorageLocation().resolve(coverFileName).toString());
            File secretFile = new File(clientStegoFileService.getSecretFileUploadStorageLocation().resolve(secretFileName).toString());

            try {
                BufferedImage bufferedImage = ImageIO.read(coverFile);
                long fileRgbSize = bufferedImage.getHeight() * bufferedImage.getWidth();

                if (secretFile.length() > fileRgbSize / 64) {// 8-ratio and 8 byte
                    Notification.show("Secret File size might be too big or Cover File size might be too small. The secret file might not fit.", 8000, Notification.Position.TOP_CENTER);
                }
            } catch (IOException e) {
                Notification.show("Something happened when reading cover Image. Please try again!", 8000, Notification.Position.TOP_CENTER);
            }
            isReadyToUpload = true;
        }
    }

    public void removeCoverFile(DomEvent domEvent) {
        coverFileName = null;
        isReadyToUpload = false;
        recommendationText = "";
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
            Notification.show("Sorry! The operation could not be performed. Not enough space in cover file to encode the secret!", 5000, Notification.Position.TOP_CENTER);
            return;
        }

        File file = new File(clientStegoFileService.getStegoFileDownloadStorageLocation().resolve(stegoFileResponse.getFileName()).toString());
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(stegoFileResponse.getData());
        } catch (IOException e) {
            e.printStackTrace();
        }

        stegoFileName = stegoFileResponse.getFileName();
        Notification.show("Successfully transformed to a StegoFile. Click to download", 5000, Notification.Position.TOP_CENTER);
    }

    public StreamResource getStreamResource(String stegoFileName) {
        return new StreamResource(stegoFileName, () -> {
            try {
                return new BufferedInputStream(new FileInputStream(new File(clientStegoFileService.getStegoFileDownloadStorageLocation().resolve(stegoFileName).toString())));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            return null;
        });
    }
}
