package com.elizarov.service.lucene;

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
import java.util.List;

@Service
public class Indexer {

    private int numIndexed = 0;
    private Long startTime = 0L;
    private Long endTime = 0L;
    private IndexWriter writer;
    private final IndexFileChooser fileChooser;
    private File indexDirectoryPath =
            new File(com.elizarov.service.lucene.LuceneConstants.INDEX_DIR);

    @Autowired
    public Indexer(IndexFileChooser fileChooser) {
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
                new TextField(com.elizarov.service.lucene.LuceneConstants.CONTENTS, new String(Files.readAllBytes(file.toPath())), Field.Store.YES);
        //index file name
        Field fileNameField =
                new StringField(com.elizarov.service.lucene.LuceneConstants.FILE_NAME, file.getName(), Field.Store.YES);
        //index file path
        Field filePathField =
                new StringField(com.elizarov.service.lucene.LuceneConstants.FILE_PATH, file.getPath(), Field.Store.YES);
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
                if (!file.isDirectory()
                        && !file.isHidden()
                        && file.exists()
                        && file.canRead()) {
                    indexFile(file);
                    numDocs++;
                }
            }
        }
        numIndexed = numDocs;
        endTime = System.currentTimeMillis();

    }


    public String getTime() {
        return numIndexed + " File indexed, time taken: " +
                (endTime - startTime) + " ms";
    }


    public void selectIndexDirectory() {
        indexDirectoryPath = fileChooser.selectIndexDirectory();
    }

    public void createIndexer(File indexDirectoryPath) throws IOException {
        //this directory will contain the indexes
//        indexDirectoryPath = new File(LuceneConstants.INDEX_DIR);
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
}
