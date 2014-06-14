package com.github.gg;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class TabSim extends HashMap<String, Sim> {

    
    public void addClass(String name, String parent, HashSet<TModifier> modifiers) throws UnsupportedOperationException {
        String key = getKey4class(name);

        if (containsKey(key)) {
            throw new UnsupportedOperationException("Ya existe la clase -> '" + name + "'");
        } else {

            Sim parent_sim = null;
            if (parent != null && !parent.trim().isEmpty()) {
                String pkey = getKey4class(parent);
                if (containsKey(pkey)) {
                    parent_sim = get(pkey);
                } else {
                    throw new UnsupportedOperationException("No existe la clase padre -> '" + parent + "'");
                }
            }

            Sim sim = new Sim(TRol.CLASS, null, -1, 0, modifiers, null, name, parent_sim);
            put(key, sim);
        }
    }

    public String getKey4class(String name) {
        return getKey(TRol.CLASS, null, null, name, new Object[]{});
    }

    public String getKey(TRol rol, String scope, String type, String name, Object... args) {
        return String.format("[%s]->[%s]->[%s]->[%s]->[%s]", rol, scope, type, name, Arrays.toString(args));
    }

    public Object[][] toArray2D() {
        Object[][] rows = new Object[size()][];

        int i = 0;
        for (Entry<String, Sim> entry : this.entrySet()) {
            String string = entry.getKey();
            Sim sim = entry.getValue();
            rows[i] = sim.toArray();
            i++;
        }

        return rows;
    }

    public Object[] getArrayHeader() {
        ArrayList meta = new ArrayList();
        for (Field f : Sim.class.getDeclaredFields()) {
            meta.add(f.getName());
        }
        return meta.toArray();
    }

    @Override
    public String toString() {
        return super.toString(); //To change body of generated methods, choose Tools | Templates.
    }

}
