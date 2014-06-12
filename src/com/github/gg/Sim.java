package com.github.gg;

import java.util.HashSet;

public class Sim {

    TRol rol;
    String scope;
    int position;

    HashSet<TModifier> modifiers;
    TType type;
    String name;

    Sim parent;

    public Sim(TRol rol, String scope, int position, HashSet<TModifier> modifiers, TType type, String name, Sim parent) {
        this.rol = rol;
        this.scope = scope;
        this.position = position;
        this.modifiers = modifiers;
        this.type = type;
        this.name = name;
        this.parent = parent;
    }

}
