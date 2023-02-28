package com.elizarov.controller;

import com.elizarov.service.lucene.IndexFileChooser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;


@Component
@FxmlView("textEditor.fxml")
public class TextEditorController {

  @FXML
  Button buttonTextEditor;
  @FXML
  Button buttonExit;
  @FXML
  Label labelTextEditor;
  @FXML
  Button buttonOpenEditor;

  IndexFileChooser chooser;
  private String editor;
  private String fileToOpen;

  @Autowired
  public TextEditorController(IndexFileChooser chooser) {
    this.chooser = chooser;
  }

  public void setFileToOpen(String fileToOpen) {
    this.fileToOpen = fileToOpen;
  }

  @FXML
  public void initialize() {
    Properties properties = new Properties();
    try (FileReader input = new FileReader("app.properties")) {
      properties.load(input);
      editor = properties.getProperty("pathToEditor");
      if (editor == null) {
        labelTextEditor.setText("Please chose any editor! Throw button below!");
      } else {
        String editorName = new File(editor).getName();
        labelTextEditor.setText("Open document with " + editorName);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  public void exit(ActionEvent event) {
    closeWindow(event);
  }

  public void openAndExit(ActionEvent event) {
    try {
      new ProcessBuilder(editor, fileToOpen).start();
      closeWindow(event);
    } catch (IOException ex) {
      labelTextEditor.setText("Please chose any editor! Throw button below!");
      ex.printStackTrace();
    }
  }

  public void chooseEditor(ActionEvent event) {
    Properties properties = new Properties();
    editor = chooser.selectFile().getAbsolutePath();
    labelTextEditor.setText("Open with " + editor);
    properties.setProperty("pathToEditor", editor);
    try (FileWriter output = new FileWriter("app.properties")) {
      properties.store(output, "Properties");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void closeWindow(ActionEvent event) {
    ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
  }


}
