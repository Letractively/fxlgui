package co.fxl.gui.impl;

import co.fxl.gui.api.ICallback;
import co.fxl.gui.api.IRegistry;


public abstract class AsyncServiceProviderImpl<T> implements IRegistry.IAsyncServiceProvider<T> {
    protected Class<T> clazz;
    private String id = "services";

    public AsyncServiceProviderImpl(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Class<T> serviceType() {
        return clazz;
    }

    @Override
    public void loadAsync(
        final ICallback<co.fxl.gui.api.IRegistry.IServiceProvider<T>> callback) {
        co.fxl.gui.impl.StatusPanel.start(id);
        loadAsyncImpl(new CallbackTemplate<co.fxl.gui.api.IRegistry.IServiceProvider<T>>(
                callback) {
                @Override
                public void onSuccess(IRegistry.IServiceProvider<T> result) {
                    co.fxl.gui.impl.StatusPanel.stop(id);
                    callback.onSuccess(result);
                }
            });
    }

    protected abstract void loadAsyncImpl(
        ICallback<co.fxl.gui.api.IRegistry.IServiceProvider<T>> callback);
}
