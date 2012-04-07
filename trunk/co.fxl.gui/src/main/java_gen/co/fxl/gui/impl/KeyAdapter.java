package co.fxl.gui.impl;

import co.fxl.gui.api.IClickable;


public class KeyAdapter<T> extends KeyTemplate<T> {
    public KeyAdapter() {
        super(null);
    }

    public void forward(IClickable.IKey<T> key) {
        if ((buttonType) == (KeyTemplate.ButtonType.RIGHT)) {
            key.mouseRight();
        }

        if (pressedKeys.get(KeyTemplate.KeyType.ALT_KEY)) {
            key.altPressed();
        }

        if (pressedKeys.get(KeyTemplate.KeyType.CTRL_KEY)) {
            key.ctrlPressed();
        }

        if (pressedKeys.get(KeyTemplate.KeyType.SHIFT_KEY)) {
            key.shiftPressed();
        }

        if (isDoubleClick) {
            key.doubleClick();
        }
    }
}
