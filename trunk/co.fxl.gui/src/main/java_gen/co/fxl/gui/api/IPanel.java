package co.fxl.gui.api;

public interface IPanel<T> extends IBordered, IClickable<T>, IColored,
    IElement<T>, IMouseOverElement<T> {
    IContainer add();

    ILayout layout();

    T clear();
}
