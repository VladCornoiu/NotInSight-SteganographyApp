package com.example.client;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;

@Route
@PreserveOnRefresh
@PWA(name = "NotInSight Application", shortName = "NotInSight", description = "Steganography Application", backgroundColor = "#227aef", themeColor = "#227aef")
@PageTitle("NotInSight")
public class MainView extends VerticalLayout {

    private HorizontalLayout layout;
    private VerticalLayout titleLayout;
    private VerticalLayout leftLayout;
    private VerticalLayout rightLayout;

    private final Button embedButton;
    private final Button extractButton;


    public MainView() {
        titleLayout = new VerticalLayout();

        Label titleLabel = new Label("NotInSight - Steganography App");
        titleLayout.add(titleLabel);
        titleLayout.setWidthFull();

        leftLayout = new VerticalLayout();
        rightLayout = new VerticalLayout();
        layout = new HorizontalLayout();

        embedButton = new Button("Embed Secret File", e -> UI.getCurrent().navigate("EmbedView"));
        embedButton.setThemeName("primary");
        embedButton.setWidth("50%");
        embedButton.setHeight("150px");
        extractButton = new Button("Extract Secret File", e -> UI.getCurrent().navigate("ExtractView"));
        extractButton.setThemeName("primary");
        extractButton.setWidth("50%");
        extractButton.setHeight("150px");

        leftLayout.add(embedButton);
        leftLayout.setHeightFull();
        leftLayout.setWidthFull();
        leftLayout.setAlignItems(Alignment.CENTER);
        leftLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        rightLayout.add(extractButton);
        rightLayout.setHeightFull();
        rightLayout.setWidthFull();
        rightLayout.setAlignItems(Alignment.CENTER);
        rightLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        layout.setWidthFull();
        layout.setHeightFull();
        layout.add(titleLayout);
        layout.add(leftLayout);
        layout.add(rightLayout);

        add(layout);
    }

}
