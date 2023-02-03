package com.elizarov;

import com.elizarov.service.MainViewService;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class JavaFxApplication extends Application {

  private ConfigurableApplicationContext applicationContext;
  private MainViewService service;

  @Override
  public void init() {
    String[] args = getParameters().getRaw().toArray(new String[0]);

    this.applicationContext = new SpringApplicationBuilder()
            .sources(IndexerApplication.class)
            .run(args);
    service = applicationContext.getBean(MainViewService.class);
  }

  @Override
  public void start(Stage stage) {
    service.doMainWindow(stage);
  }

  @Override
  public void stop() {
    this.applicationContext.close();
    Platform.exit();
  }
}
