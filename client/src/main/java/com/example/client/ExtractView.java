package com.example.client;

import com.example.client.controllers.ExtractViewController;
import com.example.client.data.SteganographyDataModel;
import com.example.client.service.StegoRestApi;
import com.example.client.utils.StegoFileReceiver;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
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

@Route(value = "ExtractView")
@Tag("extract-view")
@JsModule("./src/views/extract-view.js")
public class ExtractView extends PolymerTemplate<ExtractView.ExtractViewModel> {

    private static final Logger logger = LoggerFactory.getLogger(ExtractView.class);

    @Getter
    private final ExtractViewController extractViewController;

    @Id("stegoFileUpload")
    private Upload stegoFileUpload;
    @Id("submitButton")
    private Button submitButton;
    @Id("downloadSecretFileButton")
    private Button downloadSecretFileButton;
    @Id("downloadSecretFileAnchor")
    private Element anchor;
    @Id("backToHomePageButton")
    private Button backToHomePageButton;

    @Autowired
    public ExtractView(StegoRestApi stegoRestApi) {

        SteganographyDataModel steganographyDataModel = new SteganographyDataModel(stegoRestApi);
        extractViewController = new ExtractViewController(stegoRestApi, steganographyDataModel);

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

        anchor.setAttribute("download", true);
        anchor.setEnabled(false);

        downloadSecretFileButton.setVisible(false);

        submitButton.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> {
            extractViewController.processSubmit();
            anchor.setAttribute("href", extractViewController.getStreamResource(extractViewController.getSecretFileName()));
            anchor.setEnabled(true);
            downloadSecretFileButton.setVisible(true);
        });
        submitButton.setEnabled(false);

        backToHomePageButton.setIcon(new Icon(VaadinIcon.ARROW_BACKWARD));
        backToHomePageButton.setText("Back to Home Page");
        backToHomePageButton.addClickListener(e -> UI.getCurrent().navigate(""));
    }

    public interface ExtractViewModel extends TemplateModel {

    }
}
