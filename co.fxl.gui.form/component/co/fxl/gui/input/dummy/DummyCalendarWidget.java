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
package co.fxl.gui.input.dummy;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IUpdateable;
import co.fxl.gui.format.impl.Format;
import co.fxl.gui.input.api.ICalendarWidget;

class DummyCalendarWidget implements ICalendarWidget {

	private ILabel label;
	private List<IUpdateListener<Date>> listeners = new LinkedList<IUpdateListener<Date>>();

	DummyCalendarWidget(IContainer container) {
		this.label = container.label();
	}

	@Override
	public IUpdateable<Date> addUpdateListener(
			co.fxl.gui.api.IUpdateable.IUpdateListener<Date> listener) {
		listeners.add(listener);
		return this;
	}

	@Override
	public ICalendarWidget date(Date date) {
		label.text(Format.date().format(date));
		return this;
	}

	@Override
	public Date date() {
		return Format.date().parse(label.text());
	}
}
