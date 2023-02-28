package com.elizarov.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

@Component
@FxmlView("settings.fxml")
public class SettingsController {

  @FXML
  Label settingsLabel1;
  @FXML
  Label settingsLabel2;
  @FXML
  TextField settingsProperty1;
  @FXML
  TextField settingsProperty2;
  @FXML
  Button saveButton1;
  @FXML
  Button saveButton2;

  final Properties properties = new Properties();

  @FXML
  public void initialize() {
    try (FileReader input = new FileReader("app.properties")) {
      properties.load(input);
      settingsLabel1.setText("pathToEditor");
      settingsLabel2.setText("defaultQuery");
      settingsProperty1.setText(properties.getProperty(settingsLabel1.getText()));
      settingsProperty2.setText(properties.getProperty(settingsLabel2.getText()));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void save1() {
    String text = settingsProperty1.getText();
    properties.setProperty("pathToEditor", text);
    try (FileWriter output = new FileWriter("app.properties")) {
      properties.store(output, "Properties1 saved");
    } catch (IOException e) {
      e.printStackTrace();
    }
    settingsProperty1.setText(text);
  }

  public void save2() {
    String text = settingsProperty2.getText();
    if(text==null||text.isEmpty()){
      text="id";
    }
    properties.setProperty("defaultQuery", text);
    try (FileWriter output = new FileWriter("app.properties")) {

      properties.store(output, "Properties1 saved");
    } catch (IOException e) {
      e.printStackTrace();
    }
    settingsProperty2.setText(text);
  }
}
