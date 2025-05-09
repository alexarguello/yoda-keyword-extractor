package com.example.yoda_keyword_extractor.ui;

import com.example.yoda_keyword_extractor.service.KeywordService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Route("")
public class MainView extends VerticalLayout {
    private static final Logger logger = LoggerFactory.getLogger(MainView.class);

    public MainView(KeywordService keywordService) {

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.START);

        H1 title = new H1("Yoda Keyword Extractor");

        TextField directoryField = new TextField("Directory Path");
        directoryField.setWidth("500px");
        directoryField.setPlaceholder("Enter path to directory with markdown files (optional)");

        TextArea promptField = new TextArea("Your Request");
        promptField.setWidth("500px");
        promptField.setPlaceholder("Example: Give me a quote full of yoda wisdom");

        TextArea outputArea = new TextArea("Yoda's Response");
        outputArea.setWidth("500px");
        outputArea.setHeight("300px");
        outputArea.setReadOnly(true);

        Button askButton = new Button("Ask Yoda", e -> {
            String directoryPath = directoryField.getValue();
            String userRequest = promptField.getValue();

            if ((directoryPath == null || directoryPath.isBlank()) && (userRequest == null || userRequest.isBlank())) {
                Notification.show("Provide at least a directory path or a request, you must!");
                return;
            }

            outputArea.setPlaceholder("Searching for wisdom, I am...");

            try {
                String response = keywordService.getYodaResponse(directoryPath, userRequest);
                logger.debug("Received response from ChatClient: {}", response);
                outputArea.setValue(response);
            } catch (Exception ex) {
                outputArea.setValue("Failed, I have. Error: " + ex.getMessage());
            }
        });

        add(title, directoryField, promptField, askButton, outputArea);
    }
}