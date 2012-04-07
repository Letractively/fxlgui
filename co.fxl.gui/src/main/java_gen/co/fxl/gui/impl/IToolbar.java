package co.fxl.gui.impl;

import co.fxl.gui.api.IAlignment;
import co.fxl.gui.api.IContainer;


public interface IToolbar {
    IContainer add();

    IToolbar addGroup();

    void clear();

    IToolbar visible(boolean visible);

    IToolbar adjustHeights();

    IAlignment<co.fxl.gui.impl.IToolbar> align();
}
