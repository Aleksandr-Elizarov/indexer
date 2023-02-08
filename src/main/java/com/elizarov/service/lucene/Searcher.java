package com.elizarov.service.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class Searcher {

  private final Indexer indexer;
  private StringBuffer searchResult;
  private List<String> files;

  private IndexSearcher searcher;
  private QueryParser queryParser;
  private Query query;
  private TopDocs hits;

  @Autowired
  public Searcher(Indexer indexer) {
    this.indexer = indexer;
  }


  public TopDocs getHits() {
    return hits;
  }

  public void highlighter(String text, int docsNum) throws IOException, ParseException, InvalidTokenOffsetsException {
    createSearcher();
    //Create the query
    query = queryParser.parse(text);
    //Search the lucene documents
    hits = searcher.search(query, Const.MAX_SEARCH_FRAGMENTS);

    //Uses HTML &lt;B&gt;&lt;/B&gt; tag to highlight the searched terms
    Formatter formatter = new SimpleHTMLFormatter(Const.PRE_TAG,
            Const.POST_TAG);

    //It scores text fragments by the number of unique query terms found
    //Basically the matching score in layman terms
    QueryScorer scorer = new QueryScorer(query);

    //used to markup highlighted terms found in the best sections of a text
    Highlighter highlighter = new Highlighter(formatter, scorer);
    highlighter.setMaxDocCharsToAnalyze(Const.MAX_DOC_CHARS_TO_ANALYZE);

    //It breaks text up into same-size texts but does not split up spans
    Fragmenter fragmenter = new SimpleSpanFragmenter(scorer,
            Const.MAX_SIZE_OF_NON_SPLIT_FRAGMENT);

    //set fragmenter to highlighter
    highlighter.setTextFragmenter(fragmenter);
    files = new ArrayList<>(getDocuments(hits, searcher));
    Document doc = searcher.doc(docsNum);
    getDocumentsResult(doc, highlighter);

  }


  public String getSearchResult() {
    return searchResult.toString();
  }

  public List<String> getFiles() {
    return files;
  }

  private List<String> getDocuments(TopDocs hits, IndexSearcher searcher) throws IOException {
    List<String> list = new ArrayList<>();
    for (int i = 0; i < hits.scoreDocs.length; i++) {
      int docid = hits.scoreDocs[i].doc;
      Document doc = searcher.doc(docid);
      String title = doc.get(Const.FILE_PATH);
      list.add(title);
    }
    return list;
  }

  private void getDocumentsResult(Document document, Highlighter highlighter) throws InvalidTokenOffsetsException, IOException {
    String content = document.get(Const.CONTENTS);
    //Create token stream
    TokenStream stream = new StandardAnalyzer().tokenStream(Const.CONTENTS, new StringReader(content));
    //Get highlighted text fragments
    String[] frags = highlighter.getBestFragments(stream, content,
            Const.MAX_SEARCH_FRAGMENTS);

    for (String frag : frags) {
      searchResult.append("<p>")
              .append(frag)
              .append("</p>");
    }
  }

  public void createSearcher() throws IOException {
    //Get directory reference
    Directory indexDirectory = FSDirectory.open(indexer.getIndexDirectoryPath().toPath());
    //Index reader - an interface for accessing a point-in-time view of a lucene index
    IndexReader reader = DirectoryReader.open(indexDirectory);
    //Create lucene searcher. It searches over a single IndexReader.
    searcher = new IndexSearcher(reader);
    //analyzer with the default stop words
    Analyzer analyzer = new StandardAnalyzer();
    //Query parser to be used for creating TermQuery
    queryParser = new QueryParser(Const.CONTENTS, analyzer);
    //initialize result field
    searchResult = new StringBuffer();
    files = new ArrayList<>();
  }

  public IndexSearcher getSearcher() {
    return searcher;
  }

}
