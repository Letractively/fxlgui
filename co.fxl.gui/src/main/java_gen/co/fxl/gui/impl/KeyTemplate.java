package co.fxl.gui.impl;

import co.fxl.gui.api.IClickable;

import java.util.HashMap;
import java.util.Map;


public abstract class KeyTemplate<T> implements IClickable.IKey<T> {
    protected final T element;
    protected ButtonType buttonType = ButtonType.LEFT;
    protected Map<co.fxl.gui.impl.KeyTemplate.KeyType, java.lang.Boolean> pressedKeys =
        new HashMap<co.fxl.gui.impl.KeyTemplate.KeyType, java.lang.Boolean>();
    protected boolean isDoubleClick = false;

    protected KeyTemplate(T element) {
        this.element = element;
        pressedKeys.put(KeyType.ALT_KEY, false);
        pressedKeys.put(KeyType.SHIFT_KEY, false);
        pressedKeys.put(KeyType.CTRL_KEY, false);
    }

    @Override
    public T altPressed() {
        pressedKeys.put(KeyType.ALT_KEY, true);

        return element;
    }

    @Override
    public T ctrlPressed() {
        pressedKeys.put(KeyType.CTRL_KEY, true);

        return element;
    }

    @Override
    public T mouseLeft() {
        buttonType = ButtonType.LEFT;

        return element;
    }

    @Override
    public T mouseRight() {
        buttonType = ButtonType.RIGHT;

        return element;
    }

    @Override
    public T shiftPressed() {
        pressedKeys.put(KeyType.SHIFT_KEY, true);

        return element;
    }

    @Override
    public T doubleClick() {
        isDoubleClick = true;

        return element;
    }
    protected enum ButtonType {LEFT,
        RIGHT;
    }
    protected enum KeyType {CTRL_KEY,
        ALT_KEY,
        SHIFT_KEY;
    }
}
