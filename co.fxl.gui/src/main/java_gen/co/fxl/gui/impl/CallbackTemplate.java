package co.fxl.gui.impl;

import co.fxl.gui.api.ICallback;


public abstract class CallbackTemplate<T> implements ICallback<T> {
    private ICallback<?> cb;

    public CallbackTemplate() {
    }

    public CallbackTemplate(ICallback<?> cb) {
        this.cb = cb;
    }

    @Override
    public void onFail(Throwable throwable) {
        if ((cb) == null) {
            throwable.printStackTrace();
            throw new RuntimeException(throwable);
        } else {
            cb.onFail(throwable);
        }
    }

    public static ICallback<java.lang.Boolean> adapterVoid(
        final ICallback<java.lang.Void> cb2) {
        return new CallbackTemplate<java.lang.Boolean>(cb2) {
                @Override
                public void onSuccess(Boolean result) {
                    cb2.onSuccess(null);
                }
            };
    }

    public static ICallback<java.lang.Void> adapterBoolean(
        final ICallback<java.lang.Boolean> cb2) {
        return new CallbackTemplate<java.lang.Void>(cb2) {
                @Override
                public void onSuccess(Void result) {
                    cb2.onSuccess(false);
                }
            };
    }

    public static ICallback<java.lang.Void> adapterBooleanTrue(
        final ICallback<java.lang.Boolean> cb2) {
        return new CallbackTemplate<java.lang.Void>(cb2) {
                @Override
                public void onSuccess(Void result) {
                    cb2.onSuccess(true);
                }
            };
    }
}
