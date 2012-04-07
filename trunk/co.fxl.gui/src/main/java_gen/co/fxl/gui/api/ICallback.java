package co.fxl.gui.api;

public interface ICallback<T> {
    void onSuccess(T result);

    void onFail(Throwable throwable);
}
