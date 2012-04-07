package co.fxl.gui.impl;

public interface INavigationListener {
    boolean hasNext();

    void next();

    boolean hasPrevious();

    void previous();
}
