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

import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IComboBox;
import co.fxl.gui.api.ITextElement;
import co.fxl.gui.api.ITextField;
import co.fxl.gui.api.IUpdateable.IUpdateListener;

public class Validation {

	private class Field implements IUpdateListener<String> {

		private ITextElement<?> textElement;
		private String originalValue;
		boolean isSpecified = false;
		boolean isError = false;

		Field(ITextElement<?> textElement) {
			this.textElement = textElement;
			fields.add(this);
			update();
		}

		void update() {
			originalValue = textElement.text();
			isSpecified = false;
			isError = false;
		}

		@Override
		public void onUpdate(String value) {
			isSpecified = !value.equals(originalValue);
			updateClickables();
		}
	}

	private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat();
	private List<IClickable<?>> clickables = new LinkedList<IClickable<?>>();
	private List<Field> fields = new LinkedList<Field>();

	private void updateClickables() {
		boolean error = false;
		boolean isSpecified = false;
		for (Field field : fields) {
			if (field.isSpecified)
				isSpecified = true;
			if (field.isError)
				error = true;
		}
		for (IClickable<?> c : clickables) {
			c.clickable(isSpecified && !error);
		}
	}

	public void upate() {
		for (Field f : fields)
			f.update();
	}

	public Validation linkClickable(IClickable<?> clickable) {
		clickables.add(clickable);
		return this;
	}

	public Validation validateDate(final ITextField textField) {
		final Field field = new Field(textField);
		textField.addUpdateListener(new IUpdateListener<String>() {
			@Override
			public void onUpdate(String value) {
				field.isError = false;
				if (value.length() > 0) {
					try {
						DATE_FORMAT.parse(value);
					} catch (Exception e) {
						field.isError = true;
					}
				}
				if (field.isError) {
					textField.color().mix().red().white();
				} else {
					textField.color().white();
				}
				field.onUpdate(value);
			}
		});
		return this;
	}

	public Validation linkInput(ITextField textField) {
		Field field = new Field(textField);
		textField.addUpdateListener(field);
		return this;
	}

	public Validation linkInput(final IComboBox comboBox) {
		Field field = new Field(comboBox);
		comboBox.addUpdateListener(field);
		return this;
	}
}
