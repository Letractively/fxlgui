package co.fxl.gui.impl;

import co.fxl.gui.api.ICallback;
import co.fxl.gui.api.IComboBox;
import co.fxl.gui.api.IUpdateable;


public abstract class LazyUpdateListener<T> extends CallbackTemplate<java.lang.Boolean>
    implements IUpdateable.IUpdateListener<T> {
    private T value;
    private IComboBox cb;
    private boolean active = true;
    private T allowedValue;

    public LazyUpdateListener() {
    }

    @SuppressWarnings(value = "unchecked")
    public LazyUpdateListener(IComboBox cb) {
        this.cb = cb;

        if (cb != null) {
            allowedValue = ((T) (cb.text()));
        }
    }

    @Override
    public void onUpdate(T value) {
        if (!(active)) {
            return;
        }

        this.value = value;
        co.fxl.gui.impl.DiscardChangesDialog.show(this);
    }

    @Override
    public void onSuccess(Boolean result) {
        if (result) {
            allowedValue = value;
            onAllowedUpdate(value);
        } else {
            onCancelledUpdate(value);

            if ((cb) != null) {
                active = false;
                cb.text(((String) (allowedValue)));
                active = true;
            }
        }
    }

    protected abstract void onAllowedUpdate(T value);

    protected void onCancelledUpdate(T value) {
    }
}
