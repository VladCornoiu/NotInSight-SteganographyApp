package com.example.client.module;

import com.example.client.controllers.EmbedViewController;
import com.example.client.controllers.ExtractViewController;
import com.example.client.data.SteganographyDataModel;
import com.example.client.service.StegoRestApi;
import com.example.client.utils.CoverFileReceiver;
import com.example.client.utils.StegoFileReceiver;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.FileRejectedEvent;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.dom.DomEventListener;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "ExtractView")
public class ExtractView extends VerticalLayout {

    private static final Logger logger = LoggerFactory.getLogger(ExtractView.class);

    @Getter
    private final ExtractViewController extractViewController;

    @Getter
    private final Button submitButton;

    @Autowired
    public ExtractView(StegoRestApi stegoRestApi) {
        super();

        SteganographyDataModel steganographyDataModel = new SteganographyDataModel(stegoRestApi);
        extractViewController = new ExtractViewController(stegoRestApi, steganographyDataModel);

        submitButton = new Button("Submit");

        Upload stegoFileUpload = new Upload();

        Button stegoFileUploadButton = new Button("Upload Stego File");
        stegoFileUpload.setUploadButton(stegoFileUploadButton);
        stegoFileUploadButton.setId("stegoFileUpload");

        StegoFileReceiver stegoFileReceiver = new StegoFileReceiver();
        stegoFileUpload.setReceiver(stegoFileReceiver);
        stegoFileUpload.addSucceededListener((ComponentEventListener<SucceededEvent>) succeededEvent -> { // needs rework
            extractViewController.uploadStegoFile(succeededEvent);
            if (extractViewController.isReadyToUpload()) {
                submitButton.setEnabled(true); //
            }
        });
        stegoFileUpload.getElement().addEventListener("upload-abort", (DomEventListener) domEvent -> { // needs rework
            extractViewController.removeStegoFile(domEvent);
            submitButton.setEnabled(false);
        });
        stegoFileUpload.addFileRejectedListener((ComponentEventListener<FileRejectedEvent>) rejectedEvent -> {
            logger.info(rejectedEvent.toString());
        });

        Anchor anchor = new Anchor(new StreamResource("default.txt", () -> extractViewController.createResource()), ""); // create method to change name
        anchor.getElement().setAttribute("download", true);

        Button downloadSecretFileButton = new Button("Download Secret File", new Icon(VaadinIcon.DOWNLOAD_ALT));
        anchor.add(downloadSecretFileButton);
        anchor.removeHref();
        anchor.setEnabled(false);

        submitButton.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> {
            extractViewController.processSubmit();
            anchor.setHref(extractViewController.getStreamResource(extractViewController.getSecretFileName()));
            anchor.setEnabled(true);
        });
        submitButton.setEnabled(false);

        add(stegoFileUpload);
        add(submitButton);
        add(anchor);
    }
}
