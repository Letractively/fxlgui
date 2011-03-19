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
package co.fxl.gui.input.swing;

import java.util.Date;

import javax.swing.JTextField;

import co.fxl.gui.api.IUpdateable;
import co.fxl.gui.api.template.DateFormat;
import co.fxl.gui.input.api.IDateTextFieldWidget;
import co.fxl.gui.swing.SwingContainer;
import co.fxl.gui.swing.SwingTextInput;

class SwingDateTextFieldWidget extends
		SwingTextInput<JTextField, IDateTextFieldWidget> implements
		IDateTextFieldWidget {

	private static final DateFormat DATE_FORMAT = DateFormat.instance;

	SwingDateTextFieldWidget(SwingContainer<JTextField> container) {
		super(container);
	}

	@Override
	public IDateTextFieldWidget height(int height) {
		return super.height(height + 1);
	}

	@Override
	public int height() {
		return super.height() - 1;
	}

	@Override
	public IDateTextFieldWidget text(String text) {
		container.component.setText(text);
		return this;
	}

	@Override
	public IUpdateable<Date> addUpdateListener(
			final co.fxl.gui.api.IUpdateable.IUpdateListener<Date> listener) {
		return super.addStringUpdateListener(new IUpdateListener<String>() {

			@Override
			public void onUpdate(String value) {
				listener.onUpdate(date());
			}
		});
	}

	@Override
	public IDateTextFieldWidget date(Date date) {
		return text(DATE_FORMAT.format(date));
	}

	@Override
	public Date date() {
		return DATE_FORMAT.parse(text());
	}
}
