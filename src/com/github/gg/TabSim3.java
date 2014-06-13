package com.github.gg;

import java.util.HashMap;
import java.util.HashSet;

public abstract class TabSim3<T> extends HashMap<T, Sim> {

    public void addClass(String name) {
        T key = getKey(name);

        if (containsKey(key)) {
            throw new UnsupportedOperationException("Ya existe la un simbolo con el nombre -> '" + name + "'");
        } else {
            Sim sim = new Sim(TRol.CLASS, null, -1, -1, new HashSet<>(), null, name, null);
            put(key, sim);
        }
    }

    public abstract T getKey(Object... args);
}
