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

import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.template.ViewWidget;

class ViewGadget extends ViewWidget implements IClickListener {

	private MasterDetailTableWidgetImpl widget;

	ViewGadget(MasterDetailTableWidgetImpl widget, ILayout layout) {
		super(layout, "Views");
		this.widget = widget;
	}

	void addView(final DetailViewImpl view, boolean active) {
		IClickListener clickListener = new IClickListener() {

			@Override
			public void onClick() {
				view.show();
			}
		};
		addView(view.name, clickListener, active);
	}

	@Override
	public void onClick() {
		widget.showTable();
	}
}
