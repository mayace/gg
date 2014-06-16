package com.github.gg;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import javafx.beans.binding.StringBinding;

public class TabSim extends HashMap<String, Sim> {

    public void addVariable(Sim method, String type, String name) {
        String key = getKey4parameter(method.name, type, name);

        if (containsKey(key)) {
            throw new UnsupportedOperationException("Ya existe la variable -> '" + name + "'");
        }
        Sim sim = new Sim(TRol.VARIABLE, method.name, method.size++, 1, new HashSet<TModifier>() {
        }, type, name, null);

        put(key, sim);
        
    }

    public void addParameter(Sim method, boolean ref, String type, String name) {
        String key = getKey4parameter(method.name, type, name);

        if (containsKey(key)) {
            throw new UnsupportedOperationException("Ya existe la variable -> '" + name + "'");
        }

//        String classkey = getKey(methodname);
//        if (!containsKey(classkey)) {
//            throw new UnsupportedOperationException("No existe la clase -> '" + classname + "'");
//        }
        Sim sim = new Sim(TRol.VARIABLE, method.name, method.size++, 1, new HashSet<TModifier>() {
        }, type, name, null);

        put(key, sim);
        
        if(ref){
        }
    }

    public Sim addMethod(String classname, HashSet<TModifier> modifiers, String type, String name, Object... params) {
        String key = getKey4method(classname, type, name, params);
        if (containsKey(key)) {
            throw new UnsupportedOperationException("Ya existe el metodo -> '" + name + "'");
        }

        String classkey = getKey4class(classname);
        if (!containsKey(classkey)) {
            throw new UnsupportedOperationException("No existe la clase -> '" + classname + "'");
        }

        Sim classsim = get(classkey);

        Sim sim = new Sim(TRol.METHOD, classname, -1, 0, modifiers, type, name, null);

        put(key, sim);

        //return y this
        return sim;
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

    public void addClass(String name, String parent, HashSet<TModifier> modifiers) throws UnsupportedOperationException, CloneNotSupportedException {
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

            //////
            Sim[] s = getFields(parent);
            for (int i = 0; i < s.length; i++) {
                Sim clon = (Sim) s[i].clone();
                clon.scope = name;
                sim.size++;
                put(getKey4field(name, clon.type.toString(), clon.name), clon);
            }
        }
    }

    public String getKey4class(String name) {
        return getKey(TRol.CLASS, null, null, name, new Object[]{});
    }

    public String getKey4field(String classname, String type, String name) {
        return getKey(TRol.FIELD, classname, type, name, new Object[]{});
    }

    public String getKey4method(String classname, String type, String name, Object... params) {
        return getKey(TRol.FIELD, classname, type, name, params);
    }

    private String getKey4parameter(String methodname, String type, String name) {
        return getKey(TRol.VARIABLE, methodname, type, name, new Object[]{});
    }

    private String key_delimiter = "->";

    public String getKey(TRol rol, String scope, String type, String name, Object... args) {
        return String.format("[%2$s]%1$s[%3$s]%1$s[%4$s]%1$s[%5$s]%1$s[%6$s]", key_delimiter, rol, scope, type, name, Arrays.toString(args));
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
        return super.toString();
    }

    private Sim[] getFields(String classname) {
        ArrayList<Sim> list = new ArrayList<>();

        String classfieldkey = getKeyPart(getKey4field(classname, "gg", "gg"), 2);

        for (Entry<String, Sim> entry : this.entrySet()) {
            String key = entry.getKey();
            Sim sim = entry.getValue();

            String temp = getKeyPart(key, 2);

            if (classfieldkey.equals(temp)) {
                list.add(sim);
            }

        }

        Sim[] ret = new Sim[list.size()];
        return list.toArray(ret);
    }

    /**
     *
     * @param key la llave a procesar
     * @param length la cantidad de partes que se quieran deveolver empieza en 1
     * @return
     */
    private String getKeyPart(String key, int length) {

        String[] parts = key.split(key_delimiter);
        StringBuilder builder = new StringBuilder();

        if (length <= parts.length) {
            for (int i = 0; i < parts.length; i++) {
                String part = parts[i];
                if (i < length) {
                    if (builder.length() > 0) {
                        builder.append(key_delimiter);
                    }
                    builder.append(part);
                }

            }
        }

        return builder.toString();
    }

}
