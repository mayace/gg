package com.github.gg;

public class OptExpr {

    public String l;
    public String o;
    public String r;

    @Override
    public String toString() {
        if (o == null || o.trim().isEmpty()) {
            return l;
        }

        return String.format("%s %s %s", l, o, r);
    }

}
