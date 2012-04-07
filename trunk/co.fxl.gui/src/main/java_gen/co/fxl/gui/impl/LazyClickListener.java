package co.fxl.gui.impl;

import co.fxl.gui.api.ICallback;
import co.fxl.gui.api.IClickable;


public abstract class LazyClickListener extends CallbackTemplate<java.lang.Boolean>
    implements IClickable.IClickListener {
    @Override
    public void onClick() {
        co.fxl.gui.impl.DiscardChangesDialog.show(this);
    }

    @Override
    public void onSuccess(Boolean result) {
        if (result) {
            onAllowedClick();
        }
    }

    protected abstract void onAllowedClick();
}
