package com.github.gg;

public class CC {

    private TabSim sims = new TabSim();

    public TabSim getSims() {
        return sims;
    }

    public void setSims(TabSim sims) {
        this.sims = sims;
    }
    
    public void clear(){
        sims.clear();
    }

}
