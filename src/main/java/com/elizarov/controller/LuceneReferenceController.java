package com.elizarov.controller;

import com.elizarov.service.lucene.Const;
import javafx.fxml.FXML;
import javafx.scene.web.WebView;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

@Component
@FxmlView("luceneReference.fxml")
public class LuceneReferenceController {

  @FXML
  WebView luceneReference;

  @FXML
  public void initialize() {
    luceneReference.getEngine().load(Const.APACHE_LUCENE_REF);
    luceneReference.getEngine().reload();
  }
}
