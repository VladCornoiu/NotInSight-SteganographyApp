package com.example.client;

import com.example.client.controllers.EmbedViewController;
import com.example.client.data.SteganographyDataModel;
import com.example.client.service.ClientStegoFileService;
import com.example.client.service.StegoRestApi;
import com.example.client.utils.CoverFileReceiver;
import com.example.client.utils.SecretFileReceiver;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.upload.FileRejectedEvent;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.dom.DomEventListener;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "EmbedView")
@Tag("embed-view")
@JsModule("./src/views/embed-view.js")
public class EmbedView extends PolymerTemplate<EmbedView.EmbedViewModel> {

    private static final Logger logger = LoggerFactory.getLogger(EmbedView.class);

    @Getter
    private final EmbedViewController embedViewController;
    @Id("coverFileUpload")
    private Upload coverFileUpload;
    @Id("secretFileUpload")
    private Upload secretFileUpload;
    @Id("submitButton")
    private Button submitButton;
    @Id("downloadStegoFileAnchor")
    private Element downloadStegoFileAnchor;
    @Id("downloadStegoFileButton")
    private Button downloadStegoFileButton;
    @Id("backToHomePageButton")
    private Button backToHomePageButton;
    @Id("recommendationLabel")
    private Label recommendationLabel;
    @Id("retryButton")
    private Button retryButton;

    @Autowired
    public EmbedView(StegoRestApi stegoRestApi, ClientStegoFileService clientStegoFileService) {
        SteganographyDataModel steganographyDataModel = new SteganographyDataModel(stegoRestApi, clientStegoFileService);
        embedViewController = new EmbedViewController(stegoRestApi, steganographyDataModel, clientStegoFileService);

        // Cover File Upload Data
        CoverFileReceiver coverFileReceiver = new CoverFileReceiver(clientStegoFileService);
        coverFileUpload.setReceiver(coverFileReceiver);

        Button coverImageUploadButton = new Button("Upload Cover Image");
        coverFileUpload.setUploadButton(coverImageUploadButton);

        coverFileUpload.addSucceededListener((ComponentEventListener<SucceededEvent>) succeededEvent -> {
            embedViewController.uploadCoverFile(succeededEvent);
            if (embedViewController.isReadyToUpload()) {
                submitButton.setEnabled(true);
            }
            String recText = embedViewController.getRecommendationText();
            if (recText != "") {
                recommendationLabel.setText(recText);
                recommendationLabel.setVisible(true);
            }
        });
        coverFileUpload.getElement().addEventListener("upload-abort", (DomEventListener) domEvent -> {
            embedViewController.removeCoverFile(domEvent);
            submitButton.setEnabled(false);
            recommendationLabel.setVisible(false);
        });
        coverFileUpload.addFileRejectedListener((ComponentEventListener<FileRejectedEvent>) rejectedEvent -> {
            logger.info(rejectedEvent.toString());
        });

        //Secret File Upload Data
        SecretFileReceiver secretFileReceiver = new SecretFileReceiver(clientStegoFileService);
        secretFileUpload.setReceiver(secretFileReceiver);

        Button secretFileUploadButton = new Button("Upload Secret File");
        secretFileUpload.setUploadButton(secretFileUploadButton);

        secretFileUpload.addSucceededListener((ComponentEventListener<SucceededEvent>) succeededEvent -> {
            embedViewController.uploadSecretFile(succeededEvent);
            if (embedViewController.isReadyToUpload()) {
                submitButton.setEnabled(true);
            } else {
                submitButton.setEnabled(false);
            }
        });
        secretFileUpload.getElement().addEventListener("upload-abort", (DomEventListener) domEvent -> {
            embedViewController.removeSecretFile(domEvent);
            if (embedViewController.isReadyToUpload()) {
                submitButton.setEnabled(true);
            } else {
                submitButton.setEnabled(false);
            }
        });

        downloadStegoFileAnchor.setAttribute("download", true);
        downloadStegoFileAnchor.setEnabled(false);

        downloadStegoFileButton.setVisible(false);
        recommendationLabel.setVisible(false);
        retryButton.setVisible(false);

        submitButton.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> {
            submitButton.setEnabled(false);
            retryButton.setVisible(true);
            embedViewController.processSubmit();
            downloadStegoFileAnchor.setAttribute("href", embedViewController.getStreamResource(embedViewController.getStegoFileName()));
            downloadStegoFileAnchor.setEnabled(true);
            downloadStegoFileButton.setVisible(true);
        });
        submitButton.setEnabled(false);

        retryButton.setText("Retry Stego process");
        retryButton.addClickListener(e -> UI.getCurrent().getPage().reload());

        backToHomePageButton.setIcon(new Icon(VaadinIcon.ARROW_BACKWARD));
        backToHomePageButton.setText("Back to Home Page");
        backToHomePageButton.addClickListener(e -> UI.getCurrent().navigate(""));
    }

    public interface EmbedViewModel extends TemplateModel {

    }
}
