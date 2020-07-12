package com.example.client.utils;

import com.example.client.service.ClientStegoFileService;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.upload.Receiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class CoverFileReceiver implements Receiver {

    private static final Logger logger = LoggerFactory.getLogger(CoverFileReceiver.class);

    ClientStegoFileService clientStegoFileService;

    public CoverFileReceiver(ClientStegoFileService clientStegoFileService) {
        this.clientStegoFileService = clientStegoFileService;
    }

    @Override
    public OutputStream receiveUpload(String fileName, String MIMEType) {

        FileOutputStream fos = null;

        if (!MIMEType.contains("image")) {
            Notification.show("The cover file should be an Image File", 5000, Notification.Position.TOP_CENTER);
            return null;
        }

        File file = new File(clientStegoFileService.getCoverFileUploadStorageLocation().resolve(fileName).toString());

        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            new Notification("Could not open file<br/>" + e.getMessage());
            return null;
        }

        return fos;
    }
}
