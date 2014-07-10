package com.github.gg;

public class OptMCall {

    public final Object name;
    public final boolean reserved = false;

    public OptMCall(Object name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("call %s();", name);
    }

}
