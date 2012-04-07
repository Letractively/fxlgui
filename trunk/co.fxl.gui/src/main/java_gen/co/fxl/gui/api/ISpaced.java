package co.fxl.gui.api;

public interface ISpaced<T> {
    T spacing(int pixel);

    ISpacing spacing();

    public interface ISpacing {
        ISpacing left(int pixel);

        ISpacing right(int pixel);

        ISpacing top(int pixel);

        ISpacing bottom(int pixel);

        ISpacing inner(int pixel);
    }
}
