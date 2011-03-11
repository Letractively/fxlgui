/**
 * Copyright (c) 2010 Dangelmayr IT GmbH. All rights reserved.
 *  
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
 */
package co.fxl.gui.input.impl;

import java.util.Date;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.ITextField;
import co.fxl.gui.api.IUpdateable;
import co.fxl.gui.api.template.DateFormat;
import co.fxl.gui.input.api.IDateTextFieldWidget;

class DateTextFieldWidgetImpl implements IDateTextFieldWidget {

	private static DateFormat DATE_FORMAT = DateFormat.instance;
	private ITextField textField;

	DateTextFieldWidgetImpl(IContainer container) {
		textField = container.textField();
	}

	@Override
	public IUpdateable<Date> addUpdateListener(
			final co.fxl.gui.api.IUpdateable.IUpdateListener<Date> listener) {
		textField.addUpdateListener(new IUpdateListener<String>() {
			@Override
			public void onUpdate(String value) {
				Date date = date();
				if (date != null)
					listener.onUpdate(date);
			}
		});
		return this;
	}

	@Override
	public IDateTextFieldWidget date(Date date) {
		textField.text(DATE_FORMAT.format(date));
		return this;
	}

	@Override
	public Date date() {
		return DATE_FORMAT.parse(textField.text());
	}

}
