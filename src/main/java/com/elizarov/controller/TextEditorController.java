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

import java.io.IOException;


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
  private String editor = "notepad++";
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
    labelTextEditor.setText("Open document from Notepad++");
  }

  public void exit(ActionEvent event) {
    closeWindow(event);
  }

  public void openAndExit(ActionEvent event) throws IOException {
    new ProcessBuilder(editor, fileToOpen).start();
    closeWindow(event);
  }

  public void chooseEditor(ActionEvent event) {
    editor = chooser.selectFile().getAbsolutePath();
    labelTextEditor.setText("Open with" + editor);
  }

  private void closeWindow(ActionEvent event) {
    ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
  }


}
