package co.fxl.gui.api;

public interface IClickable<T> {
    T clickable(boolean clickable);

    boolean clickable();

    IKey<T> addClickListener(IClickListener clickListener);

    public interface IClickListener {
        void onClick();
    }

    public interface IKey<T> {
        T mouseLeft();

        T mouseRight();

        T shiftPressed();

        T altPressed();

        T ctrlPressed();

        T doubleClick();
    }
}
