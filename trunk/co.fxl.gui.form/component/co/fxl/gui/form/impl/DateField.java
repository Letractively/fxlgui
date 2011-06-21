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

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.ITextField;
import co.fxl.gui.api.IUpdateable.IUpdateListener;
import co.fxl.gui.format.impl.Format;
import co.fxl.gui.input.api.ICalendarWidget;

public class DateField {

	private class PopUp implements IClickListener {

		@Override
		public void onClick() {
			final IPopUp popUp = button.display().showPopUp().autoHide(true);
			int height = button.height();
			popUp.offset(button.offsetX(), button.offsetY() + height);
			final ICalendarWidget calendar = (ICalendarWidget) popUp
					.container().widget(ICalendarWidget.class);
			calendar.addUpdateListener(new IUpdateListener<Date>() {
				@Override
				public void onUpdate(Date value) {
					tf.text(Format.date().format(value));
				}
			});
			tf.addUpdateListener(new IUpdateListener<String>() {
				@Override
				public void onUpdate(String value) {
					calendar.date(Format.date().parse(value));
				}
			});
			calendar.date(Format.date().parse(tf.text()));
			popUp.visible(true);
		}
	}

	private ITextField tf;
	private IImage button;

	public DateField(ITextField tf, IContainer c) {
		this.tf = tf;
		button = c.image().resource(Icons.CALENDAR).size(16, 16);
		button.addClickListener(new PopUp());
	}

	public DateField clickable(boolean clickable) {
		button.clickable(clickable);
		return this;
	}
}
