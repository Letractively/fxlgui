package co.fxl.gui.api.template;

import co.fxl.gui.api.IClickable.IKey;

public class KeyAdapter<T> extends KeyTemplate<T> {

	public KeyAdapter() {
		super(null);
	}

	public void forward(IKey<T> key) {
		if (buttonType == ButtonType.RIGHT)
			key.mouseRight();
		if (pressedKeys.get(KeyType.ALT_KEY))
			key.altPressed();
		if (pressedKeys.get(KeyType.CTRL_KEY))
			key.ctrlPressed();
		if (pressedKeys.get(KeyType.SHIFT_KEY))
			key.shiftPressed();
	}

}
