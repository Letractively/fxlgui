package co.fxl.gui.input.api;

import co.fxl.gui.api.IUpdateable;

public interface IMultiComboBoxWidget extends IUpdateable<String[]> {

	IMultiComboBoxWidget addText(String... texts);

	IMultiComboBoxWidget selection(String[] texts);

	String[] selection();
}
