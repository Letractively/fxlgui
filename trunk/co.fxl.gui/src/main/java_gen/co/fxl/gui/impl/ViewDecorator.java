package co.fxl.gui.impl;

import co.fxl.gui.api.ICallback;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IVerticalPanel;


public interface ViewDecorator {
    void decorate(IContainer container, IVerticalPanel sidePanel,
        ICallback<java.lang.Void> cb);
}
