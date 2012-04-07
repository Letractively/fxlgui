package co.fxl.gui.api;

public interface IUpdateable<T> {
    IUpdateable<T> addUpdateListener(IUpdateListener<T> listener);

    public interface IUpdateListener<T> {
        void onUpdate(T value);
    }
}
