package com.github.gg;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Sim implements Cloneable {

    public TRol rol;
    public String scope;
    public int position;
    public int size;

    public HashSet<TModifier> modifiers;
    public Object type;
    public String name;

    public Object parent;
    public Object others;

    public Sim(TRol rol, String scope, int position, int size, HashSet<TModifier> modifiers, Object type, String name, Object parent) {
        this.rol = rol;
        this.scope = scope;
        this.position = position;
        this.size = size;
        this.modifiers = modifiers;
        this.type = type;
        this.name = name;
        this.parent = parent;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public Dict getDictOthers() {
        return this.others == null ? null : (Dict) this.others;
    }

    public Object[] toArray() {

        final Field[] fields = Sim.class.getFields();
        Object[] row = new Object[fields.length];

        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            try {
                f.setAccessible(true);
                row[i] = f.get(this);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(Sim.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(Sim.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return row;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone(); //To change body of generated methods, choose Tools | Templates.
    }

}
