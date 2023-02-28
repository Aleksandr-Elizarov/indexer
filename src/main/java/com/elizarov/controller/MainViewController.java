package com.elizarov.controller;

import com.elizarov.service.MainViewService;
import com.elizarov.service.lucene.Const;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;


@Component
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
  Button buttonCreateIndexes;
  @FXML
  Button buttonChooseIndexFolder;
  @FXML
  Button buttonSettings;
  @FXML
  Button buttonLucene;
  private MainViewService service;
  private List<String> links;

  @Autowired
  public MainViewController(MainViewService service) {
    this.service = service;
  }

  @FXML
  public void initialize() {
    result.getEngine().loadContent(Const.LUCENE_SYNTAX);
    result.getEngine().reload();
  }

  public void createIndexes(ActionEvent event) throws IOException {

    service.doIndexOkWindow(new Stage());
  }

  public void setSearchQuery(ActionEvent event) throws InvalidTokenOffsetsException, ParseException, IOException {
    WebEngine webEngine = result.getEngine();
    String searchText = search.getText();

    if (searchText == null || searchText.isEmpty()) {
      Properties properties = new Properties();
      try (FileReader input = new FileReader("app.properties")) {
        properties.load(input);
        searchText = properties.getProperty("defaultQuery");
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    service.search(searchText, 0);
    links = service.getFiles();

    VBox vbox = new VBox();
    for (int i = 0; i < links.size(); i++) {
      Hyperlink hyperlink = new Hyperlink(links.get(i));
      int docid = service.getSearcher().getHits().scoreDocs[i].doc;
      String fileToOpen = links.get(i);
      String finalSearchText = searchText;
      hyperlink.setOnMouseClicked(event1 -> {
        if (event1.getButton() == MouseButton.SECONDARY) {
          service.setFileToOpen(fileToOpen);
          service.doTextEditorWindow(new Stage());
        }
        try {
          service.search(finalSearchText, docid);
          webEngine.loadContent(service.getSearcher().getSearchResult());
        } catch (InvalidTokenOffsetsException | IOException | ParseException e) {
          e.printStackTrace();
        }
      });

      vbox.getChildren().add(hyperlink);
    }
    scrollPane.setContent(vbox);
    webEngine.loadContent(service.getSearcher().getSearchResult());
  }

  public void selectDirectory() {
    service.selectIndexDirectory();
  }

  public void openSettings(ActionEvent event) {
    service.openSettingsWindow(new Stage());
  }

  public void openLuceneReference(ActionEvent event) {
    service.createLuceneWindow(new Stage());
  }


}
