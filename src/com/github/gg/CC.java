package com.github.gg;

public class CC {

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
