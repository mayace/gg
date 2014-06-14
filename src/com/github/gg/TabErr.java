package com.github.gg;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

public class TabErr extends LinkedList<Err> {

    public Object[][] toArray2D() {
        Object[][] rows = new Object[size()][];

        int i = 0;

        for (Err err : this) {
            rows[i] = err.toArray();
            i++;
        }

        return rows;
    }

    public Object[] getArrayHeader() {
        ArrayList meta = new ArrayList();
        for (Field f : Err.class.getDeclaredFields()) {
            meta.add(f.getName());
        }
        return meta.toArray();
    }
}
