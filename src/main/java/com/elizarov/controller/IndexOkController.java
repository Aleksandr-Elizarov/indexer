package com.elizarov.controller;

import com.elizarov.service.lucene.Indexer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@FxmlView("indexOk.fxml")
public class IndexOkController {

  @FXML
  Button buttonOk1;
  @FXML
  Label labelOk1;
  private Indexer indexer;
  @Autowired
  public IndexOkController(Indexer indexer) {
    this.indexer = indexer;
  }

  @FXML
  public void initialize() {
    labelOk1.setText(indexer.getTime());
  }

  public void exit(ActionEvent event) {
    ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
  }

}
