package co.fxl.gui.impl;

import co.fxl.gui.api.ICallback;
import co.fxl.gui.api.IWidgetProvider;


public abstract class AsyncWidgetProviderImpl<T> implements IWidgetProvider.IAsyncWidgetProvider<T> {
    protected Class<T> clazz;
    private String id = "widgets";

    public AsyncWidgetProviderImpl(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Class<T> widgetType() {
        return clazz;
    }

    @Override
    public void loadAsync(
        final ICallback<co.fxl.gui.api.IWidgetProvider<T>> callback) {
        co.fxl.gui.impl.StatusPanel.start(id);
        loadAsyncImpl(new CallbackTemplate<co.fxl.gui.api.IWidgetProvider<T>>(
                callback) {
                @Override
                public void onSuccess(IWidgetProvider<T> result) {
                    co.fxl.gui.impl.StatusPanel.stop(id);
                    callback.onSuccess(result);
                }
            });
    }

    protected abstract void loadAsyncImpl(
        ICallback<co.fxl.gui.api.IWidgetProvider<T>> callback);
}
