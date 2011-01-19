/**
 * This file is part of FXL GUI API.
 *  
 * FXL GUI API is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *  
 * FXL GUI API is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *  
 * You should have received a copy of the GNU General Public License
 * along with FXL GUI API.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (c) 2010 Dangelmayr IT GmbH. All rights reserved.
 */
package co.fxl.gui.api.template;

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.ICheckBox;
import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IComboBox;
import co.fxl.gui.api.IPasswordField;
import co.fxl.gui.api.ITextArea;
import co.fxl.gui.api.ITextElement;
import co.fxl.gui.api.ITextField;
import co.fxl.gui.api.IUpdateable.IUpdateListener;

public class Validation implements IPageListener {

	class CheckBoxField implements IField, IUpdateListener<Boolean> {

		private ICheckBox valueElement;
		private boolean originalValue;

		CheckBoxField(ICheckBox valueElement) {
			this.valueElement = valueElement;
			originalValue = valueElement.checked();
			fields.add(this);
			update();
		}

		@Override
		public boolean isError() {
			return false;
		}

		@Override
		public boolean isNull() {
			return false;
		}

		@Override
		public boolean required() {
			return false;
		}

		@Override
		public boolean isSpecified() {
			return originalValue != valueElement.checked();
		}

		@Override
		public void update() {
			originalValue = valueElement.checked();
		}

		@Override
		public void onUpdate(Boolean value) {
			updateClickables();
		}

		@Override
		public void notifyChange() {
			onUpdate(valueElement.checked());
		}

	}

	public interface IField {

		boolean isError();

		boolean isNull();

		boolean required();

		boolean isSpecified();

		void update();

		void notifyChange();

	}

	private class Field implements IField, IUpdateListener<String> {

		private ITextElement<?> textElement;
		private String originalValue;
		boolean isSpecified = false;
		boolean isError = false;
		private boolean required = false;
		private boolean isNull = false;

		Field(ITextElement<?> textElement, boolean required) {
			this.textElement = textElement;
			this.required = required;
			fields.add(this);
			update();
		}

		@Override
		public void update() {
			originalValue = textElement.text();
			isSpecified = false;
			isError = false;
			isNull = originalValue.equals("");
		}

		@Override
		public void onUpdate(String value) {
			update(value, true);
		}

		protected void update(String value, boolean wColors) {
			isSpecified = !value.equals(originalValue);
			isNull = value.equals("");
			updateClickables();
			if (required) {
				if (textElement instanceof ITextField) {
					ITextField tf = (ITextField) textElement;
					if (wColors)
						errorColor(tf, isNull);
				} else if (textElement instanceof IPasswordField) {
					IPasswordField tf = (IPasswordField) textElement;
					if (wColors)
						errorColor(tf, isNull);
				} else
					throw new MethodNotImplementedException();
			}
		}

		@Override
		public void notifyChange() {
			update(textElement.text(), false);
		}

		@Override
		public boolean isError() {
			return isError;
		}

		@Override
		public boolean isNull() {
			return isNull;
		}

		@Override
		public boolean required() {
			return required;
		}

		@Override
		public boolean isSpecified() {
			return isSpecified;
		}
	}

	private final static DateFormat DATE_FORMAT = new DateFormat();
	private List<IClickable<?>> clickables = new LinkedList<IClickable<?>>();
	private List<IField> fields = new LinkedList<IField>();
	private boolean clickable;
	private boolean isSpecified = false;

	private void updateClickables() {
		boolean error = false;
		isSpecified = false;
		boolean allRequiredSpecified = true;
		for (IField field : fields) {
			if (field.isSpecified())
				isSpecified = true;
			if (allRequiredSpecified)
				if (field.required())
					allRequiredSpecified = !field.isNull();
			if (field.isError())
				error = true;
		}
		clickable = isSpecified && !error && allRequiredSpecified;
		for (IClickable<?> c : clickables) {
			c.clickable(clickable);
		}
	}

	public void update() {
		for (IField f : fields) {
			f.update();
		}
	}

	public Validation linkClickable(IClickable<?> clickable) {
		clickables.add(clickable);
		return this;
	}

	public Validation validateDate(final ITextField textField) {
		return validateDate(textField, false);
	}

	public Validation validateDate(final ITextField textField, boolean required) {
		final Field field = new Field(textField, required);
		textField.addUpdateListener(new IUpdateListener<String>() {
			@Override
			public void onUpdate(String value) {
				field.isError = false;
				if (value.trim().length() > 0) {
					try {
						DATE_FORMAT.parse(value.trim());
					} catch (Exception e) {
						field.isError = true;
					}
				} else {
					field.isError = field.required;
				}
				field.onUpdate(value);
				errorColor(textField, field.isError);
			}
		});
		return this;
	}

	public Validation validateInteger(final ITextField textField,
			boolean required) {
		final Field field = new Field(textField, required);
		textField.addUpdateListener(new IUpdateListener<String>() {
			@Override
			public void onUpdate(String value) {
				field.isError = false;
				if (value.length() > 0) {
					try {
						Integer.valueOf(value);
					} catch (Exception e) {
						field.isError = true;
					}
				} else {
					field.isError = field.required;
				}
				field.onUpdate(value);
				errorColor(textField, field.isError);
			}
		});
		return this;
	}

	public Validation linkInput(IPasswordField textField) {
		return linkInput(textField, false);
	}

	public Validation linkInput(IPasswordField textField, boolean required) {
		Field field = new Field(textField, required);
		textField.addUpdateListener(field);
		return this;
	}

	public Validation linkInput(ITextField textField) {
		return linkInput(textField, false);
	}

	public Validation linkInput(ITextField textField, boolean required) {
		Field field = new Field(textField, required);
		textField.addUpdateListener(field);
		return this;
	}

	public Validation linkInput(ITextArea textField) {
		return linkInput(textField, false);
	}

	public Validation linkInput(ITextArea textField, boolean required) {
		Field field = new Field(textField, required);
		textField.addUpdateListener(field);
		return this;
	}

	public Validation linkInput(final IComboBox comboBox) {
		Field field = new Field(comboBox, false);
		comboBox.addUpdateListener(field);
		return this;
	}

	private void errorColor(final ITextField textField, boolean hasError) {
		if (hasError) {
			textField.color().mix().red().white().white();
		} else {
			textField.color().white();
		}
	}

	private void errorColor(final IPasswordField textField, boolean hasError) {
		if (hasError) {
			textField.color().mix().red().white().white();
		} else {
			textField.color().white();
		}
	}

	public void reset() {
	}

	public Validation linkInput(ICheckBox valueElement) {
		CheckBoxField field = new CheckBoxField(valueElement);
		valueElement.addUpdateListener(field);
		return this;
	}

	@Override
	public boolean notifyChange() {
		for (IField f : fields) {
			f.notifyChange();
		}
		updateClickables();
		return !isSpecified;
	}
}
