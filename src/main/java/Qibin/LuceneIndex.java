package Qibin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.*;
import org.apache.lucene.document.*;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.*;
import org.apache.lucene.store.*;
import org.apache.lucene.analysis.standard.*;

public class LuceneIndex {

    private static List<String> INDEX_KEYS = Arrays.asList("题名", "作者", "出版单位", "摘要", "年");

    private static Analyzer analyzer = new StandardAnalyzer();

    public void genIndex() {
        Parser parser = new Parser();
        List<Paper> paper_list = parser.parseFile(new File("./CNKI_journal_v2.txt"));

        IndexWriter writer = null;
        try {
            Directory directory = FSDirectory.open(Paths.get("./index"));
            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
            writer = new IndexWriter(directory, iwc);
            Document doc = null;
            Set<String> keys = null;
            for (Paper paper : paper_list) {
                doc = new Document();
                keys = paper.getKeys();
                for (String key : keys) {
                    if (INDEX_KEYS.contains(key)) {
                        doc.add(new TextField(key, paper.get(key), Store.YES));
                    }
                }
                writer.addDocument(doc);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        new LuceneIndex().genIndex();
        System.out.println("Finished");
    }
}
