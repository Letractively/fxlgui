package co.fxl.gui.input.api;

import co.fxl.gui.api.IUpdateable;
import co.fxl.gui.form.impl.Validation;

public interface IMultiComboBoxWidget extends IUpdateable<String[]> {

	IMultiComboBoxWidget width(int width);

	IMultiComboBoxWidget clear();

	IMultiComboBoxWidget addText(String... texts);

	IMultiComboBoxWidget selection(String[] texts);

	IMultiComboBoxWidget validation(Validation validation);

	String[] selection();

	IMultiComboBoxWidget visible(boolean visible);
}
