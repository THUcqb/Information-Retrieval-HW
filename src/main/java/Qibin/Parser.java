package Qibin;

import java.io.*;
import java.util.*;

public class Parser {

    public List<Paper> parseFile(File file) {
        List<Paper> paper_list = new ArrayList<Paper>();
        Paper paper = null;
        String str = null;
        String last_key = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            while ((str = br.readLine()) != null) {
                if (str.startsWith("<REC>")) {
                    if (paper != null) {
                        paper_list.add(paper);
                    }
                    paper = new Paper();
                } else if (str.startsWith("<")) {
                    int index_0 = str.indexOf(">");
                    int index_1 = str.indexOf("=");
                    paper.put(str.substring(1, index_0), str.substring(index_1 + 1));
                    last_key = str.substring(1, index_0);
                } else {
                    paper.modify(last_key, str);
                }
            }
            paper_list.add(paper);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return paper_list;
    }

    public static void main(String[] args) {
        Parser parser = new Parser();
        List<Paper> paper_list = parser.parseFile(new File("./CNKI_journal_v2.txt"));
        for (Paper paper : paper_list) {
            if (paper.get("题名").equals("纬度对内陆低海拔地区一些冬型小麦品种生育天数的影响")) {
                System.out.println(paper.get("英文作者"));
            }
        }
    }
}
