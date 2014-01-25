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

import co.fxl.gui.api.IColored;
import co.fxl.gui.api.IComboBox;
import co.fxl.gui.api.IPasswordField;
import co.fxl.gui.api.ISuggestField;
import co.fxl.gui.api.ITextArea;
import co.fxl.gui.api.ITextElement;
import co.fxl.gui.api.ITextField;
import co.fxl.gui.form.impl.Validation.Field;
import co.fxl.gui.impl.Display;
import co.fxl.gui.style.impl.Style;

class ValidationColors {

	private boolean isFilter;

	ValidationColors(boolean isFilter) {
		this.isFilter = isFilter;
	}

	void errorColor(boolean required, ITextArea textField, boolean hasError) {
		if (hasError) {
			errorColorAbsolute(required, textField);
		} else {
			removeErrorColor(required, textField);
		}
	}

	void removeErrorColor(boolean required, ITextElement<?> textField) {
		if (isFilter) {
			Style.instance().filter().removeErrorColor(textField);
		} else
			removeErrorColorAbsolute(required, textField);
	}

	void errorColor(final Field field, final ISuggestField textField,
			boolean hasError) {
		if (hasError) {
			Display.instance().invokeLater(new Runnable() {
				@Override
				public void run() {
					if (field.isNull)
						errorColorAbsolute(field.required(), textField);
				}
			}, 50);
		} else {
			removeErrorColor(field.required(), textField);
		}
	}

	void errorColor(boolean required, IComboBox textField, boolean hasError) {
		if (hasError) {
			errorColorAbsolute(required, textField);
		} else {
			removeErrorColor(required, textField);
		}
	}

	void errorColor(boolean required, ITextField textField, boolean hasError) {
		if (hasError) {
			errorColorAbsolute(required, textField);
		} else {
			removeErrorColor(required, textField);
		}
	}

	void errorColor(boolean required, final IPasswordField textField,
			boolean hasError) {
		if (hasError) {
			errorColorAbsolute(required, textField);
		} else {
			removeErrorColor(required, textField);
		}
	}

	void removeErrorColorAbsolute(boolean required, ITextElement<?> t) {
		if (t instanceof IColored) {
			IColored c = (IColored) t;
			if (!isEmptyColor(required, t))
				c.color().gray(253);
		}
	}

	private boolean isEmptyColor(boolean required, ITextElement<?> t) {
		IColored c = (IColored) t;
		String text = t.text();
		if (required && (text == null || text.equals(""))) {
			c.color().rgb(255, 255, 204);
			return true;
		}
		return false;
	}

	private void errorColorAbsolute(boolean required, IColored c) {
		if (!isEmptyColor(required, (ITextElement<?>) c))
			c.color().rgb(255, 200, 200);
	}

}
