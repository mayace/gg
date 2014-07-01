package com.github.gg;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class TabSim extends HashMap<String, Sim> {

    public Sim getPublicMethod(Object classname, Object name, Object... params) {
        Sim sim = getMethod(classname.toString(), name.toString(), params);
        if (!sim.modifiers.contains(TModifier.PUBLIC)) {
            throw new UnsupportedOperationException("El metodo -> " + name + Arrays.toString(params) + " no es -> " + TModifier.PUBLIC);
        }

        return sim;
    }

    public Sim getPublicClass(Object name) {
        String key = getKey4class(name.toString());

        Sim sim = getClass(name);

        if (!sim.modifiers.contains(TModifier.PUBLIC)) {
            throw new UnsupportedOperationException("La clase -> " + name + " no es -> " + TModifier.PUBLIC);
        }

        return sim;
    }

    public Sim getPublicField(Object classname, Object name) {
        Sim field_sim = getField(classname, name);

        if (!field_sim.modifiers.contains(TModifier.PUBLIC)) {
            throw new UnsupportedOperationException("El campo -> " + name + " no es -> " + TModifier.PUBLIC);
        }
        return field_sim;
    }

    public Sim getField(Object classname, Object name) {
        String key = getKey4field(classname.toString(), name.toString());
        if (!containsKey(key)) {
            throw new UnsupportedOperationException("No existe el campo -> " + name);
        }

        return get(key);
    }

    public Sim getClass(Object name) {
        String key = getKey4class(name.toString());
        if (!containsKey(key)) {
            throw new UnsupportedOperationException("No existe la clase -> " + name);
        }

        return get(key);
    }

    public Sim getLocalvar(Object scope, String name, Object... params) {

        String key = getKey4parameter(scope.toString(), name, params);

        if (!containsKey(key)) {
            throw new UnsupportedOperationException("No existe la variabla -> " + name);
        }

        return get(key);
    }

    public Sim getMethod(String classname, String name, Object... params) {
        String key = getKey4method(classname, name, params);

        if (!containsKey(key)) {
            throw new UnsupportedOperationException("No existe el metodo -> " + name + Arrays.toString(params));
        }

        return get(key);
    }

    public Sim getConstructor(String classname, Object... params) {
        String key = getKey4constructor(classname, params);

        if (!containsKey(key)) {
            throw new UnsupportedOperationException("No existe el constructor -> " + classname + Arrays.toString(params));
        }

        return get(key);
    }

    public Sim addVariable(Sim method_sim, String type, String name, Dict others) {
        final Dict method_others = (Dict) method_sim.others;
        String key = getKey4parameter(method_sim.name, name, method_others.getObjArray("overload"));

        if (containsKey(key)) {
            throw new UnsupportedOperationException("Ya existe la variable -> '" + name + "'");
        }
        Sim sim = new Sim(TRol.LOCALVAR, method_sim.name, method_sim.size++, 1, new HashSet<TModifier>() {
        }, type, name, null);

        sim.others = others;
        others.put("overload", method_others.get("overload"));

        put(key, sim);

        return sim;

    }

    public void addParameter(Sim method_sim, boolean ref, String type, String name, Dict others) {
        final Dict method_others = (Dict) method_sim.others;
        String key = getKey4parameter(method_sim.name, name, method_others.get("overload"));

        if (containsKey(key)) {
            throw new UnsupportedOperationException("Ya existe la variable -> '" + name + "'");
        }

//        String classkey = getKey(methodname);
//        if (!containsKey(classkey)) {
//            throw new UnsupportedOperationException("No existe la clase -> '" + classname + "'");
//        }
        Sim sim = new Sim(TRol.LOCALVAR, method_sim.name, method_sim.size++, 1, new HashSet<TModifier>() {
        }, type, name, null);
        sim.others = others;
        others.put("overload", method_others.get("overload"));
        others.put("ref", ref);

        put(key, sim);

        if (ref) {
        }
    }

    public Sim addConstructor(String classname, HashSet<TModifier> modifiers, String name, Object... params) {
        String key = getKey4constructor(classname, params);

        if (!name.equals(classname)) {
            throw new UnsupportedOperationException("El nombre de contructor no coincide con el nombre de la clase -> '" + classname + "'");
        }

        if (containsKey(key)) {
            throw new UnsupportedOperationException("Ya existe el constructor -> '" + name + "' " + Arrays.toString(params));
        }

        String classkey = getKey4class(classname);
        if (!containsKey(classkey)) {
            throw new UnsupportedOperationException("No existe la clase -> '" + classname + "'");
        }

        Sim classsim = get(classkey);

        Sim sim = new Sim(TRol.METHOD, classname, -1, 0, modifiers, classname, name, null);
        sim.others = new Dict("overload", params);

        put(key, sim);

        // classim
        classsim.getDictOthers().put("constructor", classsim.getDictOthers().getInt("constructor") + 1);

        return sim;
    }

    public Sim addMethod(String classname, HashSet<TModifier> modifiers, String type, String name, Object... params) {
        String key = getKey4method(classname, name, params);
        if (containsKey(key)) {
            throw new UnsupportedOperationException("Ya existe el metodo -> " + name + Arrays.toString(params));
        }

        String classkey = getKey4class(classname);
        if (!containsKey(classkey)) {
            throw new UnsupportedOperationException("No existe la clase -> '" + classname + "'");
        }

        Sim classsim = get(classkey);

        Sim sim = new Sim(TRol.METHOD, classname, -1, 0, modifiers, type, name, null);
        sim.others = new Dict("overload", params);

        put(key, sim);

        //return y this
        return sim;
    }

    public void addField(String classname, HashSet<TModifier> modifiers, String type, String name, Object otros) {
        String key = getKey4field(classname, name);
        if (containsKey(key)) {
            throw new UnsupportedOperationException("Ya existe el campo -> '" + name + "'");
        }

        String classkey = getKey4class(classname);
        if (!containsKey(classkey)) {
            throw new UnsupportedOperationException("No existe la clase -> '" + classname + "'");
        }

        Sim classsim = get(classkey);
        Sim sim = new Sim(TRol.FIELD, classname, classsim.size++, 1, modifiers, type, name, null);
        sim.others = otros;

        put(key, sim);

        // DEFAULTS...
        if (type.equals(TType.FLOAT.toString())) {
            sim.size = 2;
            classsim.size++;
        }
        if (modifiers.isEmpty()) {
            sim.modifiers.add(TModifier.PUBLIC);
        }
    }

    public Sim addClass(String name, String parent, HashSet<TModifier> modifiers) throws UnsupportedOperationException, CloneNotSupportedException {
        String key = getKey4class(name);

        if (containsKey(key)) {
            throw new UnsupportedOperationException("Ya existe la clase -> '" + name + "'");
        }

        Sim parent_sim = null;
        if (parent != null && !parent.trim().isEmpty()) {
            String pkey = getKey4class(parent);
            if (containsKey(pkey)) {
                parent_sim = get(pkey);
            } else {
                throw new UnsupportedOperationException("No existe la clase padre -> '" + parent + "'");
            }
        }

        Sim sim = new Sim(TRol.CLASS, null, -1, 0, modifiers, null, name, (parent_sim == null ? null : parent_sim.name));
        sim.others = new Dict("constructor", 0);

        put(key, sim);

        // heredar????????
        Sim[] s = getFields(parent);
        for (int i = 0; i < s.length; i++) {
            Sim clon = (Sim) s[i].clone();
            clon.scope = name;
            sim.size++;
            put(getKey4field(name, clon.name), clon);
        }

        if (modifiers.isEmpty()) {
            sim.modifiers.add(TModifier.PUBLIC);
        }

        return sim;
    }

    public String getKey4class(String name) {
        return getKey(TRol.CLASS, null, null, name, new Object[]{});
    }

    public String getKey4field(String classname, String name) {
        return getKey(TRol.FIELD, classname, null, name, new Object[]{});
    }

    public String getKey4method(String classname, String name, Object... params) {
        return getKey(TRol.METHOD, classname, null, name, params);
    }

    public String getKey4constructor(String classname, Object... params) {
        return getKey(TRol.METHOD, classname, classname, classname, params);
    }

    private String getKey4parameter(String methodname, String name, Object... params) {
        return getKey(TRol.LOCALVAR, methodname, null, name, params);
    }

    private final String key_delimiter = "->";

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

        String classfieldkey = getKeyPart(getKey4field(classname, "gg"), 2);

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
