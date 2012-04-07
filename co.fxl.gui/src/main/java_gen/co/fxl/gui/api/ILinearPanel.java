package co.fxl.gui.api;

public interface ILinearPanel<T extends co.fxl.gui.api.ILinearPanel<T>>
    extends IPanel<T>, ISpaced<T> {
    T addSpace(int pixel);

    IAlignment<T> align();
}
