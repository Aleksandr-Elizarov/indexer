package com.elizarov.service;

import com.elizarov.controller.MainViewController;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class MainViewService {

  private ConfigurableApplicationContext applicationContext;

  @Autowired
  public MainViewService(ConfigurableApplicationContext applicationContext) {
    this.applicationContext = applicationContext;

  }

  public void doMainWindow(Stage stage) {
    FxWeaver weaver = applicationContext.getBean(FxWeaver.class);
    Parent root = weaver.loadView(MainViewController.class);

    show(root, stage);
  }

  private void show(Node root, Stage stage) {
    Scene scene = new Scene((Parent) root);
    stage.setScene(scene);
    stage.show();
  }

}
