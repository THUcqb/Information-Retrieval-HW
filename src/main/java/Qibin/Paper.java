package Qibin;

import java.util.HashMap;
import java.util.Set;

public class Paper {
    private HashMap<String, String> contents;

    public Paper() {
        contents = new HashMap<String, String>();
    }

    public String get(String key) {
        return contents.get(key);
    }

    public void put(String key, String value) {
        contents.put(key, value);
    }

    public void modify(String key, String value) {
        String old_value = contents.get(key);
        contents.put(key, old_value + " " + value);
    }

    public Set<String> getKeys() {
        return contents.keySet();
    }
}
