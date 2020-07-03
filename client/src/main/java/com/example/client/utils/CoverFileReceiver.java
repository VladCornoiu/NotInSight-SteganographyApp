package com.example.client.utils;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.upload.Receiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CoverFileReceiver implements Receiver {

    private static final Logger logger = LoggerFactory.getLogger(CoverFileReceiver.class);

    @Override
    public OutputStream receiveUpload(String fileName, String MIMEType) {

        File file = null;
        FileOutputStream fos = null;

        if (!MIMEType.contains("image")) {
            Notification.show("The cover file should be an Image File", 5000, Notification.Position.TOP_CENTER);
            return null;
        }

        try {
            Files.createDirectories(Paths.get("clientside/uploads/coverfile/").toAbsolutePath().normalize());
            file = new File("clientside/uploads/coverfile/" + fileName);
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            new Notification("Could not open file<br/>" + e.getMessage());
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fos;
    }
}
