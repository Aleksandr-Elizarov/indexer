package com.elizarov.service;

import com.elizarov.controller.IndexOkController;
import com.elizarov.controller.MainViewController;
import com.elizarov.service.lucene.Indexer;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MainViewService {

  private ConfigurableApplicationContext applicationContext;
  private Indexer indexer;

  @Autowired
  public MainViewService(ConfigurableApplicationContext applicationContext,
                         Indexer indexer) {
    this.applicationContext = applicationContext;
    this.indexer = indexer;

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

  public void doIndexOkWindow(Stage stage) throws IOException {
    indexer.createIndex();
    FxWeaver weaver = applicationContext.getBean(FxWeaver.class);
    Pane root = weaver.loadView(IndexOkController.class);
    show(root, stage);
    indexer.close();
  }

}
