package com.elizarov.service;

import com.elizarov.controller.IndexOkController;
import com.elizarov.controller.MainViewController;
import com.elizarov.controller.TextEditorController;
import com.elizarov.service.lucene.Indexer;
import com.elizarov.service.lucene.Searcher;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class MainViewService {

  private final Indexer indexer;
  private ConfigurableApplicationContext applicationContext;
  private Searcher searcher;
  private TextEditorController editorController;

  @Autowired
  public MainViewService(ConfigurableApplicationContext applicationContext,
                         Indexer indexer,
                         Searcher searcher,
                         TextEditorController editorController) {
    this.applicationContext = applicationContext;
    this.indexer = indexer;
    this.searcher = searcher;
    this.editorController = editorController;

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

  public void search(String query, int docsNum) throws IOException, InvalidTokenOffsetsException, ParseException {
    searcher.highlighter(query, docsNum);
  }

  public List<String> getFiles() {
    return searcher.getFiles();
  }

  public Searcher getSearcher() {
    return searcher;
  }

  public void setFileToOpen(String fileName) {
    editorController.setFileToOpen(fileName);
  }

  public void doTextEditorWindow(Stage stage) {
    FxWeaver weaver = applicationContext.getBean(FxWeaver.class);
    Pane root = weaver.loadView(TextEditorController.class);
    show(root, stage);
  }

  public void selectIndexDirectory() {
    indexer.selectIndexDirectory();
  }

}
