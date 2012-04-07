package co.fxl.gui.api;

public interface IEditable<T> {
    T editable(boolean editable);

    boolean editable();
}
