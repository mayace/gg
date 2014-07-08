package com.github.gg;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;

public class Dict extends HashMap<String, Object> {

    public float getFloat(String k) {
        return Float.parseFloat(getString(k));
    }

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

    public TType getType(String k) {
        final Object val = (TType) get(k);
        return (val == null ? null : (TType) val);
    }

    public TagSet getTags(String k) {
        final Object val = (TagSet) get(k);
        return (val == null ? null : (TagSet) val);
    }

    public String getCodigo3D(String k) {
        final Object val = get(k);
        return (val == null ? null : val.toString());
    }

    public int getLenth(String k) {
        final Object val = (int) get(k);
        return (val == null ? null : (int) val);
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
        return (Sim) get(k);
    }

    @Override
    public String toString() {
        Iterator<Entry<String, Object>> i = entrySet().iterator();
        if (!i.hasNext()) {
            return "{}";
        }

        StringBuilder sb = new StringBuilder();
        sb.append('{');
        for (;;) {
            Entry<String, Object> e = i.next();
            String key = e.getKey();
            Object value = e.getValue();
//            sb.append(key == this ? "(this Map)" : key);
            sb.append(key);
            sb.append('=');
//            sb.append(value == this ? "(this Map)" : (value instanceof Object[] ? Arrays.toString(value) : value));

            //<editor-fold defaultstate="collapsed" desc="ARRAYS...">
            if (value instanceof Object[]) {
                sb.append(Arrays.toString((Object[]) value));
            } else if (value instanceof int[]) {
                sb.append(Arrays.toString((int[]) value));
            } else {
                sb.append(value);
            }
            //</editor-fold>
            if (!i.hasNext()) {
                return sb.append('}').toString();
            }
            sb.append(',').append(' ');
        }
    }

    public Path getPath(String key) {
        return (Path) get(key);
    }

    public int getInt(String k) {
        final String val = getString(k);
        return val == null ? null : Integer.parseInt(val);
    }

    public Integer getInteger(String k) {
        final String val = getString(k);
        return val == null ? null : new Integer(val);
    }

    public Object[] getObjArray(String k) {
        return (Object[]) get(k);
    }

    public int[] getIntArray(String k) {
        return (int[]) get(k);
    }

}
