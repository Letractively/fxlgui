package co.fxl.gui.layout.api;

import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IPanel;


public interface IDialogLayout {
    IDialogLayout decorator(IDecorator decorator);

    IClickTarget addClickListener(IClickable.IClickListener clickListener);

    IPanel<?> createButton(IPanel<?> panel);

    public interface IClickTarget {
        void onOK();

        void onCancel();
    }

    public interface IDecorator {
        void decorate(IContainer container);
    }
}
