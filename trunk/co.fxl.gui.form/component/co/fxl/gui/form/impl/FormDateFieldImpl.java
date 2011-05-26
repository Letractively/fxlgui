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
package co.fxl.gui.form.impl;

import java.util.Date;

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.ITextField;
import co.fxl.gui.form.api.IFormField;
import co.fxl.gui.format.impl.Format;
import co.fxl.gui.input.api.ICalendarWidget;

class FormDateFieldImpl extends FormTextFieldImpl<Date> {

	private class PopUp implements IClickListener, IUpdateListener<Boolean> {

		private IElement<?> e;

		PopUp(IElement<?> e) {
			this.e = e;
		}

		@Override
		public void onClick() {
			final IPopUp popUp = widget.gridPanel.display().showPopUp()
					.autoHide(true);
			int height = e.height();
			popUp.offset(e.offsetX(), e.offsetY() + height);
			final ICalendarWidget calendar = (ICalendarWidget) popUp
					.container().widget(ICalendarWidget.class);
			calendar.addUpdateListener(new IUpdateListener<Date>() {
				@Override
				public void onUpdate(Date value) {
					FormDateFieldImpl.this.valueElement().text(
							Format.date().format(value));
				}
			});
			valueElement().addUpdateListener(new IUpdateListener<String>() {
				@Override
				public void onUpdate(String value) {
					calendar.date(Format.date().parse(value));
				}
			});
			popUp.visible(true);
		}

		@Override
		public void onUpdate(Boolean value) {
			if (value)
				new PopUp(valueElement()).onClick();
		}
	}

	private IImage button;

	FormDateFieldImpl(final FormWidgetImpl widget, int index, String name) {
		super(widget, index, name);
		button = addContainer().image().resource(Icons.CALENDAR).size(16, 16);
		button.addClickListener(new PopUp(button));
		editable(widget.saveListener != null);
		valueElement().addFocusListener(new PopUp(valueElement()));
	}

	@Override
	public IFormField<ITextField, Date> editable(boolean editable) {
		if (button != null)
			button.clickable(editable);
		return super.editable(editable);
	}
}
