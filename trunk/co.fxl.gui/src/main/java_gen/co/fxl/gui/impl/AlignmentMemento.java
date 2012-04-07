package co.fxl.gui.impl;

import co.fxl.gui.api.IAlignment;


public class AlignmentMemento<T> implements IAlignment<T> {
    private T t;
    public Type type = Type.BEGIN;
    private boolean isSpecified = false;

    public AlignmentMemento(T t) {
        this.t = t;
    }

    @Override
    public T begin() {
        return set(Type.BEGIN);
    }

    protected T set(Type type) {
        isSpecified = true;
        this.type = type;

        return t;
    }

    @Override
    public T center() {
        return set(Type.CENTER);
    }

    @Override
    public T end() {
        return set(Type.END);
    }

    public void forward(IAlignment<?> align) {
        if ((type) == (Type.BEGIN)) {
            align.begin();
        } else if ((type) == (Type.CENTER)) {
            align.center();
        } else if ((type) == (Type.END)) {
            align.end();
        }
    }

    public boolean isSpecified() {
        return isSpecified;
    }
    public enum Type {BEGIN,
        CENTER,
        END;
    }
}
