package com.example.client.module;

import com.example.client.controllers.EmbedViewController;
import com.example.client.data.SteganographyDataModel;
import com.example.client.service.StegoRestApi;
import com.example.client.utils.CoverFileReceiver;
import com.example.client.utils.SecretFileReceiver;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.FileRejectedEvent;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.dom.DomEventListener;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


@Route(value = "EmbedView")
@PreserveOnRefresh
public class EmbedView extends VerticalLayout {
    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(EmbedView.class);

    @Getter
    private final EmbedViewController embedViewController;

    @Getter
    private final Button submitButton;

    @Autowired
    public EmbedView(StegoRestApi stegoRestApi) {
        super();

        HorizontalLayout secretDataLayout = new HorizontalLayout();

        SteganographyDataModel steganographyDataModel = new SteganographyDataModel(stegoRestApi);
        embedViewController = new EmbedViewController(stegoRestApi, steganographyDataModel);
        submitButton = new Button("Submit");

        //cover data
        Upload coverFileupload = new Upload(); // TODO: review refresh forbidden upload

        Button coverFileUploadButton = new Button("Upload Cover Image");
        coverFileupload.setUploadButton(coverFileUploadButton);
        coverFileUploadButton.setId("coverFileUpload");

        CoverFileReceiver coverFileReceiver = new CoverFileReceiver();
        coverFileupload.setReceiver(coverFileReceiver);
        coverFileupload.addSucceededListener((ComponentEventListener<SucceededEvent>) succeededEvent -> { // needs rework
            embedViewController.uploadCoverFile(succeededEvent);
            if (embedViewController.isReadyToUpload()) {
                submitButton.setEnabled(true); //
            }
        });
        coverFileupload.getElement().addEventListener("upload-abort", (DomEventListener) domEvent -> { // needs rework
            embedViewController.removeCoverFile(domEvent);
            submitButton.setEnabled(false);
        });
        coverFileupload.addFileRejectedListener((ComponentEventListener<FileRejectedEvent>) rejectedEvent -> {
            logger.info(rejectedEvent.toString());
        });

        //secret data
        Upload secretFileUpload = new Upload();

        Button secretFileUploadButton = new Button("Upload Secret File");
        secretFileUpload.setUploadButton(secretFileUploadButton);

        SecretFileReceiver secretFileReceiver = new SecretFileReceiver();
        secretFileUpload.setReceiver(secretFileReceiver);
        secretFileUpload.addSucceededListener((ComponentEventListener<SucceededEvent>) succeededEvent -> { // needs rework
            embedViewController.uploadSecretFile(succeededEvent);
            if (embedViewController.isReadyToUpload()) {
                submitButton.setEnabled(true);
            } else {
                submitButton.setEnabled(false);
            }
        });
        secretFileUpload.getElement().addEventListener("upload-abort", (DomEventListener) domEvent -> { // needs rework
            embedViewController.removeSecretFile(domEvent);
            if (embedViewController.isReadyToUpload()) {
                submitButton.setEnabled(true);
            } else {
                submitButton.setEnabled(false);
            }
        });

        secretDataLayout.add(secretFileUpload);

        Anchor anchor = new Anchor(new StreamResource("stegoFile.jpg", () -> embedViewController.createResource()), ""); // create method to change name
        anchor.getElement().setAttribute("download", true);

        Button downloadStegoFileButton = new Button("Download Stego File", new Icon(VaadinIcon.DOWNLOAD_ALT));
        anchor.add(downloadStegoFileButton);

        anchor.removeHref();
        anchor.setEnabled(false);

        submitButton.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> {
            embedViewController.processSubmit();
            anchor.setHref(embedViewController.getStreamResource(embedViewController.getStegoFileName()));
            anchor.setEnabled(true);
        });
        submitButton.setEnabled(false);

        add(coverFileupload);
        add(secretDataLayout);
        add(submitButton);

        add(anchor);
    }
}
