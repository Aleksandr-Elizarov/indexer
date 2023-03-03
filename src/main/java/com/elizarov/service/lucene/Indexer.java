package com.elizarov.service.lucene;

import com.elizarov.service.AppFileChooser;
import net.lingala.zip4j.ZipFile;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class Indexer {

  private final AppFileChooser fileChooser;
  private int numIndexed = 0;
  private Long startTime = 0L;
  private Long endTime = 0L;
  private IndexWriter writer;
  private File indexDirectoryPath =
          new File(Const.INDEX_DIR);

  @Autowired
  public Indexer(AppFileChooser fileChooser) {
    this.fileChooser = fileChooser;

  }

  public void close() throws IOException {
    writer.close();
  }

  private Document getDocument(File file) throws IOException {
    //create lucene document for indexes
    Document document = new Document();
    //index file content
    Field contentField =
            new TextField(Const.CONTENTS, new String(Files.readAllBytes(file.toPath())), Field.Store.YES);
    //index file name
    Field fileNameField =
            new StringField(Const.FILE_NAME, file.getName(), Field.Store.YES);
    //index file path
    Field filePathField =
            new StringField(Const.FILE_PATH, file.getPath(), Field.Store.YES);
    //adds field indexes to document
    document.add(contentField);
    document.add(fileNameField);
    document.add(filePathField);
    return document;
  }

  private void indexFile(File file) throws IOException {

    Document document = getDocument(file);
    writer.addDocument(document);
  }

  public void createIndex() throws IOException {

    //create Indexer to create indexes
    createIndexer(indexDirectoryPath);
    //get all files in the data directory
    List<File> files = fileChooser.selectFiles();

    startTime = System.currentTimeMillis();
    int numDocs = 0;
    if (files != null) {
      for (File file : files) {
        if (checkZip(file) || file.isDirectory()) {
          numDocs += indexZipFileOrDirectory(file);
        } else if (checkFileProperties(file)) {
          indexFile(file);
          numDocs++;
        }
      }

      numIndexed = numDocs;
      endTime = System.currentTimeMillis();

    }
  }

  private boolean checkFileProperties(File file) {
    return !file.isDirectory()
            && !file.isHidden()
            && file.exists()
            && file.canRead();
  }

  public String getTime() {
    return (endTime - startTime) >= 0 ? (numIndexed + " File indexed, time taken: " +
            (endTime - startTime) + " ms") : "You did not choose files!";
  }

  public void selectIndexDirectory() {
    indexDirectoryPath = fileChooser.selectIndexDirectory();
  }

  public void createIndexer(File indexDirectoryPath) throws IOException {
    //this directory will contain the indexes
    Directory indexDirectory = FSDirectory.open(indexDirectoryPath.toPath());
    //create analyzer with the default stop words
    Analyzer analyzer = new StandardAnalyzer();
    //create config from current analyzer
    IndexWriterConfig config = new IndexWriterConfig(analyzer);
    //set to writer don't use old indexes
    config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
    //create the indexer
    writer = new IndexWriter(indexDirectory, config);
  }

  public File getIndexDirectoryPath() {
    return indexDirectoryPath;
  }

  private void unZip(File file) {
    String destination =
            getPathToUnzippedDirectory(file).replace("\\", "/");
    try {
      ZipFile zipFile = new ZipFile(file);
      zipFile.extractAll(destination);
      Files.deleteIfExists(file.toPath());

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private Boolean checkZip(File file) {
    return file.getName().endsWith(".zip");
  }

  private String getPathToUnzippedDirectory(File file) {
    return file.getParent() + "\\" + file.getName().replace(".zip", "") +
            "\\";
  }

  private int indexZipFileOrDirectory(File file) throws IOException {
    int numDocs = 0;
    String pathToDirectory;
    if (checkZip(file)) {
      unZip(file);
      pathToDirectory = getPathToUnzippedDirectory(file);
    } else {
      pathToDirectory = file.getPath();
    }
    List<File> filesInFolder = Files.walk(Paths.get(pathToDirectory))
            .filter(Files::isRegularFile)
            .map(Path::toFile)
            .collect(Collectors.toList());
    for (File fileInFolder : filesInFolder) {
      if (checkZip(fileInFolder) || file.isDirectory()) {
        numDocs += indexZipFileOrDirectory(fileInFolder);
      } else if (checkFileProperties(fileInFolder)) {
        indexFile(fileInFolder);
        numDocs++;
      }
    }
    return numDocs;
  }

}
