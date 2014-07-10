package com.github.gg;

public class OptExpr {

    final public Object o;
    final public Object l;
    final public Object r;

   

    public OptExpr(Object o, Object l, Object r) {
        this.o = o;
        this.l = l;
        this.r = r;
    }

    

    
  
    @Override
    public String toString() {
        if (o == null || o.toString().trim().isEmpty()) {
            return l.toString();
        }

        return String.format("%s %s %s", l, o, r);
    }

}
