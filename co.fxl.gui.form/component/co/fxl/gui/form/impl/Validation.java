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
package co.fxl.gui.form.impl;

import java.util.Date;
import java.util.Iterator;
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
import co.fxl.gui.format.api.IFormat;
import co.fxl.gui.format.impl.Format;
import co.fxl.gui.impl.DiscardChangesDialog;
import co.fxl.gui.impl.ICallback;

public class Validation {

	// TODO extract validation to component

	public interface IValidation<T> {

		boolean validate(String trim);

	}

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
		public boolean matches(Object valueElement) {
			return this.valueElement == valueElement;
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

		@Override
		public void reset() {
			valueElement.checked(originalValue);
		}

		@Override
		public void required(boolean status) {
		}
	}

	public interface IField {

		boolean isError();

		boolean isNull();

		boolean required();

		boolean isSpecified();

		void update();

		void notifyChange();

		void reset();

		boolean matches(Object valueElement);

		void required(boolean status);

	}

	private class Field implements IField, IUpdateListener<String> {

		private ITextElement<?> textElement;
		private String originalValue;
		boolean isSpecified = false;
		boolean isError = false;
		private boolean required = false;
		private boolean isNull = false;

		Field(ITextElement<?> textElement, boolean required) {
			assert textElement != null : "Illegal call to Validation.Field.new";
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
			isNull = originalValue == null || originalValue.equals("");
		}

		@Override
		public void onUpdate(String value) {
			update(value, true);
		}

		protected void update(String value, boolean wColors) {
			isSpecified = value != null && !value.equals(originalValue);
			isNull = value == null || value.equals("");
			updateClickables();
			if (required) {
				if (textElement instanceof ITextField) {
					ITextField tf = (ITextField) textElement;
					if (wColors)
						errorColor(tf, isNull);
				} else if (textElement instanceof ITextArea) {
					ITextArea tf = (ITextArea) textElement;
					if (wColors)
						errorColor(tf, isNull);
				} else if (textElement instanceof IPasswordField) {
					IPasswordField tf = (IPasswordField) textElement;
					if (wColors)
						errorColor(tf, isNull);
				} else if (textElement instanceof IComboBox) {
					IComboBox tf = (IComboBox) textElement;
					if (wColors)
						errorColor(tf, isNull);
				} else
					throw new MethodNotImplementedException(textElement
							.getClass().getName()
							+ " not supported by validation");
			}
		}

		@Override
		public void notifyChange() {
			String text = textElement.text();
			update(text, false);
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

		@Override
		public void reset() {
			textElement.text(originalValue);
		}

		@Override
		public boolean matches(Object valueElement) {
			return textElement == valueElement;
		}

		@Override
		public void required(boolean status) {
			required = status;
		}
	}

	private final static IFormat<Date> DATE_FORMAT = Format.date();
	private List<IClickable<?>> clickables = new LinkedList<IClickable<?>>();
	private List<IClickable<?>> reset = new LinkedList<IClickable<?>>();
	private List<IField> fields = new LinkedList<IField>();
	private boolean clickable;
	private boolean isSpecified = false;
	private boolean showDiscardChanges = false;
	private boolean isNew = false;

	private void updateClickables() {
		boolean error = false;
		isSpecified = isNew;
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
		clickable = isSpecified;
		for (IClickable<?> c : reset) {
			c.clickable(clickable);
		}
		if (showDiscardChanges) {
			DiscardChangesDialog.active(isSpecified);
		}
	}

	public void update() {
		for (IField f : fields) {
			f.update();
		}
		isNew = false;
		updateClickables();
	}

	public Validation linkClickable(IClickable<?> clickable) {
		clickables.add(clickable);
		return this;
	}

	public Validation linkReset(IClickable<?> clickable) {
		reset.add(clickable);
		return this;
	}

	public Validation validateDate(final ITextField textField) {
		return validateDate(textField, DATE_FORMAT, false);
	}

	public Validation validateDate(ITextField textField, boolean required) {
		return validateDate(textField, DATE_FORMAT, required);
	}

	public Validation validateDate(final ITextField textField,
			IFormat<Date> format) {
		return validateDate(textField, false);
	}

	public Validation validateDate(final ITextField textField,
			final IFormat<Date> format, boolean required) {
		final Field field = new Field(textField, required);
		textField.addUpdateListener(new IUpdateListener<String>() {
			@Override
			public void onUpdate(String value) {
				field.isError = false;
				if (value.trim().length() > 0) {
					try {
						Date d = format.parse(value.trim());
						if (d == null)
							field.isError = true;
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

	public Validation validateLong(final ITextField textField, boolean required) {
		final Field field = new Field(textField, required);
		textField.addUpdateListener(new IUpdateListener<String>() {
			@Override
			public void onUpdate(String value) {
				field.isError = false;
				if (value.length() > 0) {
					try {
						Long.valueOf(value);
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

	public Validation linkInput(IComboBox comboBox) {
		return linkInput(comboBox, false);
	}

	public Validation linkInput(IComboBox comboBox, boolean required) {
		Field field = new Field(comboBox, required);
		comboBox.addUpdateListener(field);
		return this;
	}

	private void errorColor(ITextArea textField, boolean hasError) {
		if (hasError) {
			textField.color().mix().red().white().white();
		} else {
			textField.color().rgb(249, 249, 249);
		}
	}

	private void errorColor(IComboBox textField, boolean hasError) {
		if (hasError) {
			textField.color().mix().red().white().white();
		} else {
			textField.color().rgb(249, 249, 249);
		}
	}

	private void errorColor(ITextField textField, boolean hasError) {
		if (hasError) {
			textField.color().mix().red().white().white();
		} else {
			textField.color().rgb(249, 249, 249);
		}
	}

	private void errorColor(final IPasswordField textField, boolean hasError) {
		if (hasError) {
			textField.color().mix().red().white().white();
		} else {
			textField.color().rgb(249, 249, 249);
		}
	}

	public void reset() {
		for (IField f : fields) {
			f.reset();
		}
		updateClickables();
		DiscardChangesDialog.active(false);
	}

	public Validation linkInput(ICheckBox valueElement) {
		CheckBoxField field = new CheckBoxField(valueElement);
		valueElement.addUpdateListener(field);
		return this;
	}

	public void notifyChange(ICallback<Boolean> callback) {
		for (IField f : fields) {
			f.notifyChange();
		}
		updateClickables();
		if (callback != null)
			callback.onSuccess(!isSpecified);
	}

	public void showDiscardChanges() {
		showDiscardChanges = true;
	}

	public void isNew() {
		isNew = true;
		for (IClickable<?> c : reset) {
			c.clickable(true);
		}
	}

	public void validate(final ITextField textField, final IValidation<String> v) {
		final Field field = new Field(textField, false);
		textField.addUpdateListener(new IUpdateListener<String>() {
			@Override
			public void onUpdate(String value) {
				field.isError = false;
				if (value.trim().length() > 0) {
					try {
						field.isError = !v.validate(value.trim());
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
	}

	public void removeInput(Object valueElement) {
		Iterator<IField> it = fields.iterator();
		while (it.hasNext()) {
			if (it.next().matches(valueElement)) {
				it.remove();
			}
		}
	}

	public void updateInput(Object valueElement, boolean status) {
		Iterator<IField> it = fields.iterator();
		boolean update = false;
		while (it.hasNext()) {
			IField field = it.next();
			if (field.matches(valueElement)) {
				update = true;
				field.required(status);
			}
		}
		if (!update)
			linkInput(valueElement, status);
		updateClickables();
	}

	private void linkInput(Object valueElement, boolean status) {
		if (valueElement instanceof IComboBox)
			linkInput((IComboBox) valueElement, status);
		else
			throw new MethodNotImplementedException();
	}

}
