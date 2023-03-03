package com.elizarov.service;

import com.elizarov.service.lucene.Const;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

@Service
public class AppFileChooser {

  private static final File INDEX_DIR = new File(Const.INDEX_DIR);
  private static File currentDataDirectoryPath;
  private final FileChooser fileChooser;
  private final DirectoryChooser directoryChooser;

  public AppFileChooser() {
    try (FileReader input = new FileReader("app.properties")) {
      Properties properties = new Properties();
      properties.load(input);
      currentDataDirectoryPath = new File(properties.getProperty(
              "defaultLogPath", Const.INDEX_DIR));
    } catch (IOException e) {
      e.printStackTrace();
    }
    fileChooser = new FileChooser();
    directoryChooser = new DirectoryChooser();
    fileChooser.setInitialDirectory(currentDataDirectoryPath);
    directoryChooser.setInitialDirectory(INDEX_DIR);
  }

  public List<File> selectFiles() {
    fileChooser.setTitle("Select files to create indexes");
    return fileChooser.showOpenMultipleDialog(new Stage());
  }

  public File selectIndexDirectory() {
    directoryChooser.setTitle("Select directory where index located or will be created");
    File directory = directoryChooser.showDialog(new Stage());
    if (directory != null) {
      return directory;
    }
    return INDEX_DIR;
  }

  public File selectFile() {
    fileChooser.setTitle("Select text editor app");
    return fileChooser.showOpenDialog(new Stage());
  }

  public File selectDirectory() {
    directoryChooser.setTitle("Select directory");
    return directoryChooser.showDialog(new Stage());
  }
}
