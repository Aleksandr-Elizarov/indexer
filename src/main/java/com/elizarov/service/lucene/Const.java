package com.elizarov.service.lucene;

public class Const {
  public static final String CONTENTS = "contents";
  public static final String FILE_NAME = "filename";
  public static final String FILE_PATH = "path";
  public static final int MAX_SEARCH_FRAGMENTS = 100;
  public static final int MAX_SIZE_OF_NON_SPLIT_FRAGMENT = 200;
  public static final int MAX_DOC_CHARS_TO_ANALYZE = 10_000_000;
  public static final String INDEX_DIR = "src\\main\\resources\\indexes";
  public static final String PRE_TAG = "<font color=red>";
  public static final String POST_TAG = "</font>";
  public static final String APACHE_LUCENE_REF = "https://lucene.apache" +
          ".org/core/2_9_4/queryparsersyntax.html";
  public static final String LUCENE_SYNTAX = "<p>" +
          "Use syntax that you can see below or visit " +
          "<a href=\"" + Const.APACHE_LUCENE_REF + "\">apache lucene site</a> " +
          "<p>A Single Term is a single word such as \"test\" or \"hello\"" +
          ".</p>" +
          " " +
          "<p>To perform a single character wildcard search use the \"?\" " +
          "symbol.</p>" +
          "<p>To perform a multiple character wildcard search use the \"*\" " +
          "symbol.</p>" +
          "<p>Examples for term \"test\": te?t test* te*t </p>" +
          "<p>Note: You cannot use a * or ? symbol as the first character of a " +
          "search.</p>" +
          "<p>Lucene supports escaping special characters that are part of the query syntax. The current list special characters are</p>" +
          "<p>+ - && || ! ( ) { } [ ] ^ \" ~ * ? : \\</p>" +
          "<p>To escape these character use the \\ before the character. For example to search for (1+1):2 use the query:</p>" +
          "<p> Example: \\(1\\+1\\)\\:2</p>" +
          "</p>" +
          "<p>A Phrase is a group of words surrounded by double quotes such as \"hello dolly\".</p>";
}

