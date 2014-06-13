package com.github.gg;

import java.util.ArrayList;
import java.util.HashMap;

public class Dict<T> extends HashMap<String, T> {

    public Dict() {

    }

    public Dict(Object... args) {

        String k = null;
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];

            if (i % 2 == 0) {
                k = arg.toString();
            } else {
                put(k, (T) arg);
            }
        }
    }

    public T set(String k, T v) {
        return this.put(k, v);
    }

    public Node getNode(String k) {
        return (Node) get(k);
    }

    public ArrayList<Dict> getDictArrayList(String k) {
        return (ArrayList<Dict>) get(k);
    }

    public String getString(String k) {
        return (String) get(k);
    }

    public Dict getDict(String k) {
        return (Dict) get(k);
    }

    public Boolean getBoolean(String k) {
        return (Boolean) get(k);
    }

    @Override
    public boolean containsKey(Object key) {

        return super.containsKey(key);
    }

}
