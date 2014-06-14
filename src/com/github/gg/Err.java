package com.github.gg;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;
import java_cup.runtime.Symbol;

public class Err {

    public TErr type;
    public String message;
    public Object val;
    public int line;
    public int column;

    public Err(TErr type, String message, Object info) {
        if (info instanceof Symbol) {
            Symbol sym = (Symbol) info;
            this.type = type;
            this.val = sym.value;
            this.message = message;
            this.line = sym.left + 1;
            this.column = sym.right + 1;
        }
    }

    public Err(TErr type, String message, Object val, int line, int column) {
        this.type = type;
        this.message = message;
        this.val = val;
        this.line = line;
        this.column = column;
    }

    public Object getVal() {
        return val;
    }

    public void setVal(Object val) {
        this.val = val;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public TErr getType() {
        if (type == null) {
            type = TErr.DESCONOCIDO;
        }
        return type;
    }

    public void setType(TErr type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    @Override
    public String toString() {
        return String.format("Error [%s] con el simbolo -> '%s' en linea -> %d y columna -> %d. Mensaje -> %s", getType().toString(), getVal().toString(), getLine(), getColumn(), getMessage());
    }

    public Object[] toArray() {
        final Field[] fields = Err.class.getFields();
        Object[] row = new Object[fields.length];

        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            try {
                f.setAccessible(true);
                row[i] = f.get(this);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(Sim.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(Sim.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return row;
    }

}
