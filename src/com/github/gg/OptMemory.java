package com.github.gg;

public class OptMemory {

    final public Object memory;
    final public Object position;

    public OptMemory(Object memory, Object position) {
        this.memory = memory;
        this.position = position;
    }

    @Override
    public String toString() {
        return String.format("%s[%s]", memory, position);
    }

}
