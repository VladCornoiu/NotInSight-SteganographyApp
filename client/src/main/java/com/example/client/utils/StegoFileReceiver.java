package com.example.client.utils;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.upload.Receiver;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class StegoFileReceiver implements Receiver {

    @Override
    public OutputStream receiveUpload(String fileName, String MIMEType) {

        File file = null;
        FileOutputStream fos = null;

        if (!MIMEType.contains("image/jpeg")) {
            Notification.show("The cover file should be an Image File", 5000, Notification.Position.TOP_CENTER);
            return null;
        }

        try {
            Files.createDirectories(Paths.get("clientside/uploads/stegofile/").toAbsolutePath().normalize());
        } catch (IOException e) {
            e.printStackTrace();
        }

        file = new File("clientside/uploads/stegofile/" + fileName);

        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            new Notification("Could not open file<br/>" + e.getMessage());
            return null;
        }

        return fos;
    }
}
