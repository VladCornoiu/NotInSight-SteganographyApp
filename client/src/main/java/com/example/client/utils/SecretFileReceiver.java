package com.example.client.utils;

import com.example.client.service.ClientStegoFileService;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.upload.Receiver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class SecretFileReceiver implements Receiver{

    ClientStegoFileService clientStegoFileService;

    public SecretFileReceiver(ClientStegoFileService clientStegoFileService) {
        this.clientStegoFileService = clientStegoFileService;
    }

    @Override
    public OutputStream receiveUpload(String fileName, String MIMEType) {

        FileOutputStream fos;
        File file = new File(clientStegoFileService.getSecretFileUploadStorageLocation().resolve(fileName).toString());

        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            new Notification("Could not open file<br/>" + e.getMessage());
            return null;
        }
        return fos;
    }
}
