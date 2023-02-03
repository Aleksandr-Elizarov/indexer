package com.elizarov.controller;

import com.elizarov.service.MainViewService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@FxmlView("main.fxml")
public class MainViewController {

  // Main view elements
  @FXML
  TextField search;
  @FXML
  WebView result;
  @FXML
  ScrollPane scrollPane;
  @FXML
  Button newMainTab;
  @FXML
  Button buttonCreateIndexes;
  @FXML
  Button buttonChooseIndexFolder;
  private MainViewService service;


  @Autowired
  public MainViewController(MainViewService service) {
    this.service = service;
  }

  public void createNewMainTab(ActionEvent event) {
    service.doMainWindow(new Stage());
  }


}
