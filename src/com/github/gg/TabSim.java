package com.github.gg;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;

public class TabSim extends HashMap<String, Sim> {

    public void addMethod(String classname, HashSet<TModifier> modifiers, String type, String name,Object ... params) {
        String key = getKey4method(classname, type, name,params);
        if (containsKey(key)) {
            throw new UnsupportedOperationException("Ya existe el metodo -> '" + name + "'");
        }

        String classkey = getKey4class(classname);
        if (!containsKey(classkey)) {
            throw new UnsupportedOperationException("No existe la clase -> '" + classname + "'");
        }

        Sim classsim = get(classkey);
        
        Sim sim = new Sim(TRol.METHOD, classname, classsim.size++, 1, modifiers, type, name, null);

        put(key, sim);
    }

    public void addField(String classname, HashSet<TModifier> modifiers, String type, String name) {
        String key = getKey4field(classname, type, name);
        if (containsKey(key)) {
            throw new UnsupportedOperationException("Ya existe el campo -> '" + name + "'");
        }

        String classkey = getKey4class(classname);
        if (!containsKey(classkey)) {
            throw new UnsupportedOperationException("No existe la clase -> '" + classname + "'");
        }

        Sim classsim = get(classkey);

        Sim sim = new Sim(TRol.FIELD, classname, classsim.size++, 1, modifiers, type, name, null);

        put(key, sim);

    }

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
    
    

    public String getKey4field(String classname, String type, String name) {
        return getKey(TRol.FIELD, classname, type, name, new Object[]{});
    }
    public String getKey4method(String classname, String type, String name,Object ... params) {
        return getKey(TRol.FIELD, classname, type, name, params);
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
