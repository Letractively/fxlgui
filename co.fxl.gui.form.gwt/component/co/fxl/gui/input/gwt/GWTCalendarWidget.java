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
package co.fxl.gui.input.gwt;

import java.util.Date;

import co.fxl.gui.api.IUpdateable;
import co.fxl.gui.gwt.GWTContainer;
import co.fxl.gui.gwt.GWTElement;
import co.fxl.gui.input.api.ICalendarWidget;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.datepicker.client.DatePicker;

class GWTCalendarWidget extends GWTElement<DatePicker, ICalendarWidget>
		implements ICalendarWidget {

	GWTCalendarWidget(GWTContainer<DatePicker> container) {
		super(container);
	}

	@Override
	public IUpdateable<Date> addUpdateListener(
			final IUpdateListener<Date> listener) {
		container.widget.addValueChangeHandler(new ValueChangeHandler<Date>() {
			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				listener.onUpdate(event.getValue());
			}
		});
		return this;
	}

	@Override
	public ICalendarWidget date(Date date) {
		container.widget.setValue(date);
		return this;
	}

	@Override
	public Date date() {
		return container.widget.getValue();
	}
}
