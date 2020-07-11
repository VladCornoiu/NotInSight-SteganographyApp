package com.example.client;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;

@Route
@Tag("main-view")
@JsModule("./src/views/main-view.js")
@PageTitle("NotInSight")
public class MainView extends PolymerTemplate<MainView.MainViewModel> {

    @Id("ExtractButton")
    private Button extractButton;
    @Id("EmbedButton")
    private Button embedButton;

    public MainView() {
        extractButton.addClickListener(e -> UI.getCurrent().navigate("ExtractView"));
        embedButton.addClickListener(e -> UI.getCurrent().navigate("EmbedView"));
    }

    public interface MainViewModel extends TemplateModel {
    }
}
