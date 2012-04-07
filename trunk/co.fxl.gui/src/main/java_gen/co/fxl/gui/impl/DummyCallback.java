package co.fxl.gui.impl;

import co.fxl.gui.api.ICallback;


public class DummyCallback<T> implements ICallback<T> {
    @Override
    public void onSuccess(T result) {
    }

    @Override
    public void onFail(Throwable throwable) {
        throw new RuntimeException(throwable);
    }

    public static ICallback<java.lang.Void> voidInstance() {
        return new DummyCallback<java.lang.Void>();
    }

    public static ICallback<java.lang.Boolean> booleanInstance() {
        return new DummyCallback<java.lang.Boolean>();
    }
}
