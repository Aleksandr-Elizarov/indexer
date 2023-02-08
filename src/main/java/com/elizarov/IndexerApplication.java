package com.elizarov;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IndexerApplication {

  public static void main(String[] args) {
//		SpringApplication.run(DemoApplication.class, args);
    Application.launch(JavaFxApplication.class, args);
  }

}
