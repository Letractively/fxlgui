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

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IWidgetProvider;
import co.fxl.gui.gwt.GWTContainer;
import co.fxl.gui.input.api.ICalendarWidget;

import com.google.gwt.user.datepicker.client.DateBox;

public class GWTCalendarWidgetProvider implements
		IWidgetProvider<ICalendarWidget> {

	@Override
	public Class<ICalendarWidget> widgetType() {
		return ICalendarWidget.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ICalendarWidget createWidget(IContainer container) {
		GWTContainer<DateBox> c = (GWTContainer<DateBox>) container;
		c.setComponent(new DateBox());
		return new GWTCalendarWidget(c);
	}
}
