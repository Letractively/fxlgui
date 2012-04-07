package co.fxl.gui.api;

public interface IWidgetProvider<T> {
    Class<T> widgetType();

    T createWidget(IContainer container);

    public interface IAsyncWidgetProvider<T> {
        Class<T> widgetType();

        void loadAsync(ICallback<co.fxl.gui.api.IWidgetProvider<T>> callback);
    }
}
