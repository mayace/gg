package com.github.gg;

import java.util.HashMap;

public class CC {

    public int getH() {
        return h;
    }

    public int getP() {
        return p;
    }

    public void setH(int h) {
        this.h = h;
    }

    public void setP(int p) {
        this.p = p;
    }

    public float[] getHeap() {
        return heap;
    }

    public HashMap<String, Dict> getMethods() {
        return methods;
    }

    public float[] getStack() {
        return stack;
    }

    public HashMap<String, Float> getTemps() {
        return temps;
    }

    private final HashMap<String, Dict> methods = new HashMap<>();
    private final HashMap<String, Float> temps = new HashMap<>();
    private final float[] stack = new float[10000];
    private final float[] heap = new float[10000];
    private int p = 0;
    private int h = 0;

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
        methods.clear();
        temps.clear();
        setP(0);
        setH(0);
    }

    public TabErr getErrs() {
        return errs;
    }

    public void setErrs(TabErr errs) {
        this.errs = errs;
    }

}
