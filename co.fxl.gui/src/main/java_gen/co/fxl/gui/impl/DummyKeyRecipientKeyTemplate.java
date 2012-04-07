package co.fxl.gui.impl;

import co.fxl.gui.api.IKeyRecipient;


public class DummyKeyRecipientKeyTemplate<T> implements IKeyRecipient.IKey<T> {
    @Override
    public T enter() {
        return ((T) (this));
    }

    @Override
    public T tab() {
        return ((T) (this));
    }

    @Override
    public T up() {
        return ((T) (this));
    }

    @Override
    public T down() {
        return ((T) (this));
    }

    @Override
    public T left() {
        return ((T) (this));
    }

    @Override
    public T right() {
        return ((T) (this));
    }

    @Override
    public IKeyRecipient.IKey<T> ctrl() {
        return this;
    }

    @Override
    public T character(char c) {
        return ((T) (this));
    }
}
