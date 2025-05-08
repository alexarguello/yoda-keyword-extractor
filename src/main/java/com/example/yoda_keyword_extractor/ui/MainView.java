package com.example.yoda_keyword_extractor.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.ai.chat.client.ChatClient;

@Route("")
public class MainView extends VerticalLayout {

  private final ChatClient chatClient;

  public MainView(ChatClient chatClient) {
    this.chatClient = chatClient;

    setSizeFull();
    setAlignItems(Alignment.CENTER);
    setJustifyContentMode(JustifyContentMode.START);

    H1 title = new H1("Yoda Keyword Extractor");

    TextField directoryField = new TextField("Directory Path");
    directoryField.setWidth("500px");
    directoryField.setPlaceholder("Enter path to directory with markdown files");

    TextArea promptField = new TextArea("Your Request");
    promptField.setWidth("500px");
    promptField.setPlaceholder("Example: Find keywords in markdown files from the directory above");
    promptField.setValue("Extract keywords from markdown files in the directory");

    TextArea outputArea = new TextArea("Yoda's Response");
    outputArea.setWidth("500px");
    outputArea.setHeight("300px");
    outputArea.setReadOnly(true);

    Button askButton = new Button("Ask Yoda");
    askButton.addClickListener(e -> {
      String directoryPath = directoryField.getValue();
      String userPrompt = promptField.getValue();

      if (directoryPath == null || directoryPath.isBlank()) {
        Notification.show("A directory path, you must provide!");
        return;
      }

      outputArea.setValue("Searching for wisdom, I am...");

      try {
        // Create a user message that includes the directory path
        String fullPrompt = userPrompt + " in the directory: " + directoryPath;

        // Let the AI decide which tools to use based on the prompt
        var response = chatClient
            .prompt()
            .user(fullPrompt)
            .call();

        // Display the result
        outputArea.setValue(response.content());
      } catch (Exception ex) {
        outputArea.setValue("Failed, I have. Error: " + ex.getMessage());
      }
    });

    add(title, directoryField, promptField, askButton, outputArea);
  }
}