package com.github.gg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class Dict extends HashMap<String, Object> {

    public Dict() {

    }

    public Dict(Object... args) {

        String k = null;
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];

            if (i % 2 == 0) {
                k = arg.toString();
            } else {
                put(k, arg);
            }
        }
    }

    public Object set(String k, Object v) {
        return this.put(k, v);
    }

    public Node getNode(String k) {
        return (Node) get(k);
    }

    public ArrayList<Dict> getDictArrayList(String k) {
        return (ArrayList<Dict>) get(k);
    }

    public String getString(String k) {
        final Object val = get(k);
        return (val == null ? null : val.toString());
    }

    public Dict getDict(String k) {
        return (Dict) get(k);
    }

    public Boolean getBoolean(String k) {
        return (Boolean) get(k);
    }

    public Stack getStack(String k) {
        return (Stack) get(k);
    }

    @Override
    public boolean containsKey(Object key) {

        return super.containsKey(key);
    }

    public Sim getSim(String k) {
        return (Sim)get(k);
    }



}
