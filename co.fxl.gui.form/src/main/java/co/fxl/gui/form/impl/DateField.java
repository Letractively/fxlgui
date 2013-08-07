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

import co.fxl.data.format.api.IFormat;
import co.fxl.data.format.impl.Format;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IGridPanel.IGridCell;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.ITextField;
import co.fxl.gui.impl.Env;
import co.fxl.gui.impl.Heights;
import co.fxl.gui.impl.RuntimeConstants;
import co.fxl.gui.impl.TextFieldAdp;
import co.fxl.gui.input.api.ICalendarWidget;

public class DateField extends TextFieldAdp implements RuntimeConstants {

	private class PopUp implements IClickListener {

		private boolean isUpdating;
		private ICalendarWidget calendar;
		private IPopUp popUp;

		private PopUp() {
			element().addUpdateListener(new IUpdateListener<String>() {
				@Override
				public void onUpdate(String value) {
					if (!isUpdating && calendar != null && popUp != null
							&& popUp.visible()) {
						Date parse = format.parse(value);
						calendar.date(parse);
					}
				}
			});
		}

		@Override
		public void onClick() {
			popUp = co.fxl.gui.impl.PopUp.showPopUp(true).autoHide(true);
			int height = button.height();
			popUp.offset(button.offsetX() - 128 + button.width(),
					button.offsetY() + height);
			calendar = (ICalendarWidget) popUp.container().widget(
					ICalendarWidget.class);
			calendar.addUpdateListener(new IUpdateListener<Date>() {
				@Override
				public void onUpdate(Date value) {
					isUpdating = true;
					String f = format.format(value);
					element().text(f);
					popUp.visible(false);
					isUpdating = false;
				}
			});

			// TODO testen: Usability: GWT: this seems to not work (shows always
			// the
			// current date):
			calendar.date(format.parse(element().text()));

			popUp.visible(true);
		}
	}

	private IImage button;
	private IFormat<Date> format = Format.date();
	private PopUp popUp;

	public DateField(IContainer c) {
		this(c, true, 2);
	}

	public DateField(IContainer c, int cellSpacing) {
		this(c, true, cellSpacing);
	}

	// private DateField(IContainer c, boolean addC) {
	// this(c, addC, 2);
	// }

	public DateField(IContainer c, boolean addC, int cellSpacing) {
		IGridPanel g = c.panel().grid();
		ITextField tf = g.cell(0, 0).textField();
		g.column(0).expand();
		IContainer c1 = null;
		if (addC) {
			IGridCell cell2 = g.cell(1, 0).align().center().valign().center();
			if (!Env.runtime().geq(Env.IE, 10))
				g.column(1).width(24);
			// Heights.INSTANCE.decorate(cell2);
			IHorizontalPanel spacing = cell2.panel().horizontal()
					.spacing(cellSpacing);
			Heights.INSTANCE.styleColor(spacing);
			IBorder border = spacing.border();
			border.color().rgb(211, 211, 211);
			if (IE_LEQ_8)
				border.width(1);
			else
				border.style().bottom().style().top().style().right();
			c1 = spacing.add();
		}
		setUp(tf, c1, addC);
		decorate(g);
	}

	private void setUp(ITextField tf, IContainer c1, boolean addCalendar) {
		element(tf);
		if (!addCalendar)
			return;
		button = c1.image().resource(Icons.CALENDAR).size(16, 16);
		popUp = new PopUp();
		button.addClickListener(popUp);
	}

	protected void decorate(IGridPanel g) {
	}

	public DateField(ITextField tf, IContainer c) {
		setUp(tf, c, true);
	}

	public DateField(ITextField tf, IContainer c, boolean addCalendar) {
		setUp(tf, c, addCalendar);
	}

	public DateField clickable(boolean clickable) {
		if (button != null)
			button.clickable(clickable);
		return this;
	}

	public DateField format(IFormat<Date> format) {
		this.format = format;
		return this;
	}

	@Override
	public DateField editable(boolean editable) {
		if (button != null)
			button.clickable(editable);
		return (DateField) super.editable(editable);
	}
}
