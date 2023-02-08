package com.elizarov.service.lucene;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class IndexFileChooser {

    private final FileChooser fileChooser;
    private final DirectoryChooser directoryChooser;
    private static final File CURRENT_DATA_DIRECTORY_PATH =  new File(LuceneConstants.CURRENT_DATA_DIRECTORY_PATH);
    private static final File INDEX_DIR = new File(LuceneConstants.INDEX_DIR);

    public IndexFileChooser() {
        fileChooser = new FileChooser();
        directoryChooser = new DirectoryChooser();
        fileChooser.setInitialDirectory(CURRENT_DATA_DIRECTORY_PATH);
        directoryChooser.setInitialDirectory(INDEX_DIR);
    }

    public List<File> selectFiles(){
        fileChooser.setTitle("Select files to create indexes");
        return fileChooser.showOpenMultipleDialog(new Stage());
    }

    public File selectIndexDirectory(){
        directoryChooser.setTitle("Select directory where index located or will be created");
        File directory = directoryChooser.showDialog(new Stage());
        if(directory != null){
            return directory;
        }
        return INDEX_DIR;
    }

    public File selectFile(){
        fileChooser.setTitle("Select text editor app");
        return fileChooser.showOpenDialog(new Stage());
    }
}
