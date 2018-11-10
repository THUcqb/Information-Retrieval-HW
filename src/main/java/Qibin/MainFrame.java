package Qibin;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.lucene.document.Document;
import javax.swing.JLabel;

public class MainFrame extends JFrame {

    // private static String[] KEYS = { "题名", "英文篇名", "作者", "英文作者", "第一责任人", "单位", "来源", "出版单位", "关键词", "英文关键词", "摘要",
            // "英文摘要", "年", "期", "专辑代码", "专题代码", "专题子栏目代码", "专题名称", "分类号", "分类名称", "文件名", "语种", "引证文献", "共引文献", "二级引证文献",
            // "表名", "出版日期", "引证文献数量", "二级参考文献数量", "二级引证文献数量", "共引文献数量", "同被引文献数量", "英文刊名", "ISSN", "CN" };
    private static String[] SELECTED_KEYS = { "题名", "作者", "出版单位", "摘要", "年" };
    private JPanel contentPane, northPane;
    private JTextField titleField, authorField, publishField, dateField;
    private static LuceneQuery query = new LuceneQuery();
    private HashMap<String, WordSim> wordSimMap = new HashMap<String, WordSim>();

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainFrame frame = new MainFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static String genHtmlStr(Document doc) {
        String str = "<html>";
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
            str += "<font size=\"4\" color=\"blue\">" + _key + ":</font> <font size=\"4\">" + value + "</font><br/>";
        }
        str += "<br/> </html>";
        return str;
    }

    private void readWordSim() {
        BufferedReader br;
        String str = null;
        WordSim ws = null;
        try {
            br = new BufferedReader(new FileReader(new File("./word_similarity.txt")));
            while ((str = br.readLine()) != null) {
                String[] terms = str.split(" ");
                double len = Double.valueOf(terms[1]);
                String[] words = new String[5];
                double[] sims = new double[5];

                for (int i = 2; i <= 10; i += 2) {
                    words[i / 2 - 1] = terms[i];
                }
                for (int i = 3; i <= 11; i += 2) {
                    sims[i / 2 - 1] = Double.valueOf(terms[i]);
                }

                ws = new WordSim(len, words, sims);
                wordSimMap.put(terms[0], ws);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the frame.
     */
    public MainFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 850, 700);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        northPane = new JPanel();
        northPane.setLayout(new BoxLayout(northPane, BoxLayout.Y_AXIS));
        contentPane.add(northPane, BorderLayout.NORTH);

        JPanel Pane1 = new JPanel();
        JPanel Pane2 = new JPanel();
        JPanel Pane3 = new JPanel();
        JPanel Pane4 = new JPanel();

        northPane.add(Pane1);
        northPane.add(Pane2);
        northPane.add(Pane3);
        northPane.add(Pane4);

        Pane1.setLayout(new FlowLayout());
        Pane2.setLayout(new FlowLayout());
        Pane3.setLayout(new FlowLayout());
        Pane4.setLayout(new FlowLayout());

        titleField = new JTextField();
        Pane1.add(titleField);
        titleField.setColumns(32);

        JLabel label = new JLabel("作者");
        Pane2.add(label);
        authorField = new JTextField();
        authorField.setColumns(20);
        Pane2.add(authorField);

        Pane3.add(new JLabel("出版单位"));
        publishField = new JTextField();
        publishField.setColumns(18);
        Pane3.add(publishField);

        Pane4.add(new JLabel("出版年份"));
        dateField = new JTextField();
        dateField.setColumns(18);
        Pane4.add(dateField);

        JScrollPane scrollPane = new JScrollPane();
        contentPane.add(scrollPane);

        DefaultListModel<String> listModel = new DefaultListModel<String>();
        JList<String> list = new JList<String>(listModel);
        scrollPane.setViewportView(list);

        readWordSim();
        // for (String key: wordSimMap.keySet()) {
        // System.out.println(key);
        // WordSim ws = wordSimMap.get(key);
        // System.out.println(ws.len);
        // for (int i = 0; i < 5; ++i) {
        // System.out.println(ws.words[i] + " " + String.valueOf(ws.sims[i]));
        // }
        // break;
        // }

        JButton btnSearch = new JButton("Search");
        Pane1.add(btnSearch);
        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String title = titleField.getText();
                String[] title_list = title.split(" ");
                String title_query = "";
                boolean firstTerm = true;
                for (String term : title_list) {
                    if (firstTerm) {
                        title_query += "(\"" + term + "\"";
                        firstTerm = false;
                    } else {
                        title_query += " (\"" + term + "\"";
                    }
                    if (wordSimMap.containsKey(term)) {
                        WordSim ws = wordSimMap.get(term);
                        for (int i = 0; i < 5; ++i) {
                            title_query += " \"" + ws.words[i] + "\"^" + String.valueOf(ws.sims[i]);
                        }
                    }
                    title_query += ")";
                }
                String author = authorField.getText();
                String publish = publishField.getText();
                String date = dateField.getText();
                String str = title_query;
                if (!author.equals("")) {
                    str += " AND 作者:\"" + author + "\"";
                }
                if (!publish.equals("")) {
                    str += " AND 出版单位:\"" + publish + "\"";
                }
                if (!date.equals("")) {
                    str += " AND 年:\"" + date + "\"";
                }
                List<Document> doc_list = query.search(str);
                listModel.removeAllElements();
                for (Document doc : doc_list) {
                    listModel.addElement(genHtmlStr(doc));
                }
            }
        });

    }

}
