package com.github.gg;

import java.util.HashMap;

public class CC {

    public float[] getHeap() {
        return heap;
    }

    public HashMap<String, Node> getMethods() {
        return methods;
    }

    public float[] getStack() {
        return stack;
    }
    
    public HashMap<String, Float> getTemps() {
        return temps;
    }

    private final HashMap<String, Node> methods = new HashMap<>();
    private final HashMap<String, Float> temps = new HashMap<>();
    private final float[] stack = new float[10000];
    private final float[] heap = new float[10000];

    private TabSim sims = new TabSim();
    private TabErr errs = new TabErr();

    public TabSim getSims() {
        return sims;
    }

    public void setSims(TabSim sims) {
        this.sims = sims;
    }

    public void clear() {
        sims.clear();
        errs.clear();
    }

    public TabErr getErrs() {
        return errs;
    }

    public void setErrs(TabErr errs) {
        this.errs = errs;
    }

}
