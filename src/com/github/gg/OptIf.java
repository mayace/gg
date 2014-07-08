package com.github.gg;

public class OptIf {

    public Object expr;
    public Object goto_true;

    public OptIf(Object expr, Object goto_true) {
        this.expr = expr;
        this.goto_true = goto_true;
    }

    @Override
    public String toString() {
        return String.format("if %s then %s", expr, goto_true);
    }

}
