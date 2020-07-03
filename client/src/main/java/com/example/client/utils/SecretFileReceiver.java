package com.example.client.utils;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.upload.Receiver;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SecretFileReceiver implements Receiver{

    @Override
    public OutputStream receiveUpload(String fileName, String MIMEType) {

        FileOutputStream fos;
        File file = new File("clientside/uploads/secretfile/" + fileName);
        try {
            Files.createDirectories(Paths.get("clientside/uploads/secretfile/").toAbsolutePath().normalize());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fos = new FileOutputStream(file);
            return fos;
        } catch (FileNotFoundException e) {
            new Notification("Could not open file<br/>" + e.getMessage());
            return null;
        }
    }
}
