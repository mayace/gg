package com.github.gg;

public class OptGoto {

    public Object label;

    public OptGoto(Object label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return String.format("goto %s;", label);
    }

}
