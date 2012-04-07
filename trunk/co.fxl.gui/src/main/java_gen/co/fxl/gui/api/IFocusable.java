package co.fxl.gui.api;

public interface IFocusable<T> {
    T focus(boolean focus);

    T addFocusListener(IUpdateable.IUpdateListener<java.lang.Boolean> hasFocus);
}
