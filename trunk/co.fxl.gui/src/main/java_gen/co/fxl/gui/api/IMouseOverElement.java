package co.fxl.gui.api;

public interface IMouseOverElement<T> {
    T addMouseOverListener(IMouseOverListener l);

    public interface IMouseOverListener {
        void onMouseOver();

        void onMouseOut();
    }
}
