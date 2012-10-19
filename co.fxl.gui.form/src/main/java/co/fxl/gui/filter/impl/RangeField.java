package co.fxl.gui.filter.impl;

import co.fxl.gui.api.IUpdateable.IUpdateListener;
import co.fxl.gui.form.impl.Validation;

public interface RangeField {

	String text();

	String upperBoundText();

	void upperBoundText(String text);

	void addUpdateListener(IUpdateListener<String> listener);

	void upperBoundAddUpdateListener(IUpdateListener<String> listener);

	void text(String text);

	void validation(Validation validation, Class<?> type);

}
