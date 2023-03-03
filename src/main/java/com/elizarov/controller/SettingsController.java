package com.elizarov.controller;

import com.elizarov.service.AppFileChooser;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

@Component
@FxmlView("settings.fxml")
public class SettingsController {

  final Properties properties = new Properties();
  @FXML
  Label settingsLabel1;
  @FXML
  Label settingsLabel2;
  @FXML
  Label settingsLabel3;
  @FXML
  TextField settingsProperty1;
  @FXML
  TextField settingsProperty2;
  @FXML
  TextField settingsProperty3;
  @FXML
  Button saveButton1;
  @FXML
  Button saveButton2;
  @FXML
  Button saveButton3;

  AppFileChooser fileChooser;

  @Autowired
  public SettingsController(AppFileChooser fileChooser) {
    this.fileChooser = fileChooser;
  }

  @FXML
  public void initialize() {
    try (FileReader input = new FileReader("app.properties")) {
      properties.load(input);
      settingsLabel1.setText("pathToEditor");
      settingsLabel2.setText("defaultQuery");
      settingsLabel3.setText("defaultLogPath");

      settingsProperty1.setText(properties.getProperty(settingsLabel1.getText()));
      settingsProperty1.setEditable(false);
      settingsProperty2.setText(properties.getProperty(settingsLabel2.getText()));
      settingsProperty3.setText(properties.getProperty(settingsLabel3.getText()));
      settingsProperty3.setEditable(false);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void save1() {
    try {
      String text = fileChooser.selectFile().getAbsolutePath();
      saveProperty("pathToEditor", text);
      settingsProperty1.setText(text);
    } catch (Exception ignored) {
    }
  }

  public void save2() {
    String text = settingsProperty2.getText();
    if (text == null || text.isEmpty()) {
      text = "id";
    }
    saveProperty("defaultQuery", text);
    settingsProperty2.setText(text);
  }

  public void save3() {
    String text;
    try {
      text = fileChooser.selectDirectory().getAbsolutePath();
      saveProperty("defaultLogPath", text);
      settingsProperty3.setText(text);
    } catch (Exception ignored) {
    }
  }

  private void saveProperty(String propertyName, String text) {
    properties.setProperty(propertyName, text);
    try (FileWriter output = new FileWriter("app.properties")) {
      properties.store(output, "Property " + propertyName + " saved");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
