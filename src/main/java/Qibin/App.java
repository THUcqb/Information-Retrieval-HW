package Qibin;

import java.util.*;

import org.apache.lucene.document.Document;

public class App
{

    private static String[] SELECTED_KEYS = { "题名", "作者", "出版单位", "摘要", "年"};
    private static LuceneQuery query = new LuceneQuery();
    // private HashMap<String, WordSim> wordSimMap = new HashMap<String, WordSim>();

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        Scanner istream = new Scanner(System.in);
        String title = istream.nextLine();
        String author = istream.nextLine();
        String publish = istream.nextLine();
        String date = istream.nextLine();
        istream.close();

        String query_str = "";
        for (String term : title.split(" "))
        {
            query_str += "(\"" + term + "\") ";
        }

        if (!author.equals("")) {
            query_str += " AND 作者:\"" + author + "\"";
        }
        if (!publish.equals("")) {
            query_str += " AND 出版单位:\"" + publish + "\"";
        }
        if (!date.equals("")) {
            query_str += " AND 年:\"" + date + "\"";
        }

        List<Document> doc_list = query.search(query_str);

        String results = "";
        for (Document doc : doc_list) {
            results += genHtmlStr(doc);
        }
        results = "<html>" + results + "</html>";
        System.out.println(results);
    }

    private static String genHtmlStr(Document doc) {
        String str = "<table border=\"1\">";
        for (String key : SELECTED_KEYS) {
            String value = "无";
            if (doc.get(key) != null) {
                value = doc.get(key);
                if (value.startsWith("<")) {
                    value = "&lt" + value;
                }
            }
            String _key = key;
            if (_key.equals("年")) {
                _key = "出版年份";
            }
            str += "<tr><td>" + _key + "</td><td>" + value + "</td></tr>";
        }
        str += "<table/><br/>";
        return str;
    }
}
