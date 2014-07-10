package com.github.gg;

public class OptMemory {

    public Object memory;
    public Object position;

    public OptMemory(Object memory, Object position) {
        this.memory = memory;
        this.position = position;
    }

    @Override
    public String toString() {
        return String.format("%s[%s]", memory, position);
    }

}
