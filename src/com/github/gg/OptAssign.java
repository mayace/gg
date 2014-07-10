package com.github.gg;

public class OptAssign {

    final public Object name;
    final public Object val;

    public OptAssign(Object name, Object val) {
        this.name = name;
        this.val = val;
    }

    @Override
    public String toString() {
        return String.format("%s = %s;", name, val);
    }

}
