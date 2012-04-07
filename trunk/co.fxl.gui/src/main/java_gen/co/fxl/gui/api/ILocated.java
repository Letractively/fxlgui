package co.fxl.gui.api;

public interface ILocated<T> extends IPoint {
    int width();

    int height();

    T offset(int x, int y);

    T width(int width);

    T height(int height);

    T size(int width, int height);
}
