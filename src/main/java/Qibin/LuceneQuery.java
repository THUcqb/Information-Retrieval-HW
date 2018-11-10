package Qibin;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.standard.*;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.*;

public class LuceneQuery {
    private static Analyzer analyzer = new StandardAnalyzer();

    public List<Document> search(String str) {
        System.out.println(str);

        List<Document> doc_list = new ArrayList<Document>();

        try {
            Directory directory = FSDirectory.open(Paths.get("./index"));
            IndexReader reader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(reader);

            QueryParser parser = new QueryParser("题名", analyzer);
            Query q = parser.parse(str);

            TopDocs top_docs = searcher.search(q, 200);
            ScoreDoc[] score_docs = top_docs.scoreDocs;
            for (ScoreDoc doc : score_docs) {
                Document d = searcher.doc(doc.doc);
                System.out.println(d.get("题名") + doc.score);
                doc_list.add(d);
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return doc_list;
    }

    public static void main(String[] args) {
        LuceneQuery query = new LuceneQuery();
        query.search("框架");
    }

}
