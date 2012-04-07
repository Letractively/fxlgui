package co.fxl.gui.api;

public interface IRegistry<R> {
    R register(IPanelProvider<?>... layoutProvider);

    R register(IWidgetProvider<?>... widgetProvider);

    R register(IWidgetProvider.IAsyncWidgetProvider<?>... widgetProvider);

    R register(IAsyncServiceProvider<?>... serviceProvider);

    R register(IServiceProvider<?>... service);

    boolean supports(Class<?> widgetClass);

    R ensure(ICallback<java.lang.Void> callback, Class<?>... widgetClass);

    <T> T service(Class<T> clazz);

    public interface IAsyncServiceProvider<T> {
        Class<T> serviceType();

        void loadAsync(
            ICallback<co.fxl.gui.api.IRegistry.IServiceProvider<T>> callback);
    }

    public interface IServiceProvider<T> {
        Class<T> serviceType();

        T getService();
    }
}
