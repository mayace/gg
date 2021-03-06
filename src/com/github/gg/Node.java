package com.github.gg;

import java.util.HashMap;

public class Node {

    private Object operation;
    private Node left;
    private Node right;
    private Object ref;
    private Object val;
    private boolean autoexec = true;

    public Node(Object operation, Node left, Node right, Object ref, Object val) {
        this.operation = operation;
        this.left = left;
        this.right = right;
        this.ref = ref;
        this.val = val;
    }

    public Node(Object operation, Node left, Node right, Object ref) {
        this.operation = operation;
        this.left = left;
        this.right = right;
        this.ref = ref;
        this.val = null;
    }

    public void exec(Object actions) {
        if (left != null && autoexec) {
            left.exec(actions);
        }
        if (right != null && autoexec) {
            right.exec(actions);
        }

        if (actions instanceof Dict) {
            Dict cations = (Dict) actions;

            HashMap<Object, Operation> o = (HashMap<Object, Operation>) cations.get("operations");
            if (o.containsKey(getOperation())) {
                setVal(o.get(getOperation()).exec(this, actions));
            } else {
                System.err.println("No existe operacion registrada para procesar el nodo con opercion - >  " + getOperation());
            }
        }
    }

    public Dict getDictRef() {
        final Object r = getRef();
        return (r instanceof Dict ? (Dict) r : null);
    }

    public Dict getDictVal() {
        final Object v = getVal();
        return (v instanceof Dict ? (Dict) v : null);
    }

    public Object getOperation() {
        return operation;
    }

    public void setOperation(Object operation) {
        this.operation = operation;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public Object getRef() {
        return ref;
    }

    public void setRef(Object ref) {
        this.ref = ref;
    }

    public Object getVal() {
        return val;
    }

    public void setVal(Object val) {
        this.val = val;
    }

    public boolean isAutoexec() {
        return autoexec;
    }

    public void setAutoexec(boolean autoexec) {
        this.autoexec = autoexec;
    }

}
