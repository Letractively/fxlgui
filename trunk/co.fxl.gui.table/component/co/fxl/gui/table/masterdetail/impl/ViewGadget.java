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
package co.fxl.gui.table.masterdetail.impl;

import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.template.WidgetTitle;

class ViewGadget implements IClickListener {

	private WidgetTitle widgetTitle;
	private IVerticalPanel panel;
	private MasterDetailTableWidgetImpl widget;

	ViewGadget(MasterDetailTableWidgetImpl widget, ILayout layout) {
		this.widget = widget;
		widgetTitle = new WidgetTitle(layout);
		widgetTitle.addTitle("Views");
		panel = widgetTitle.content().panel().vertical().spacing(2);
	}

	void addView(final DetailViewImpl view, boolean active) {
		IHorizontalPanel panel = this.panel.add().panel().horizontal().add()
				.panel().horizontal().spacing(2);
		if (active)
			panel.addSpace(4);
		ILabel textLabel = panel.add().label().text(view.name);
		textLabel.font().pixel(13);
		if (active) {
			panel.color().red();
			textLabel.font().weight().bold().color().white();
			panel.addSpace(4);
		} else
			textLabel.hyperlink().addClickListener(new IClickListener() {

				@Override
				public void onClick() {
					view.show();
				}
			});
	}

	@Override
	public void onClick() {
		widget.showTable();
	}
}
