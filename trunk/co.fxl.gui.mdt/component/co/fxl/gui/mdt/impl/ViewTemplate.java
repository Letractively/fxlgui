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
package co.fxl.gui.mdt.impl;

import java.util.List;

import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.api.template.SplitLayout;
import co.fxl.gui.filter.api.IFilterWidget.IFilterListener;
import co.fxl.gui.table.api.ISelection.IMultiSelection.IChangeListener;

abstract class ViewTemplate implements IChangeListener<Object>, Listener,
		IFilterListener {

	MasterDetailTableWidgetImpl widget;

	ViewTemplate(MasterDetailTableWidgetImpl widget) {
		this.widget = widget;
		widget.listener = this;
		if (widget.mainPanel == null) {
			if (widget.hasFilter) {
				widget.splitLayout = new SplitLayout(widget.layout);
				widget.mainPanel = widget.splitLayout.mainPanel;
				widget.sidePanel = widget.splitLayout.sidePanel;
			} else {
				IVerticalPanel v = widget.layout.vertical();
				widget.mainPanel = v.add().panel().vertical();
				widget.sidePanel = v.add().panel().vertical().visible(false);
				// .grid().cell(1, 0).align()
				// .end().panel().vertical().width(300).spacing(10);
				// widget.sidePanel.visible(false);
				// v.addSpace(10);
				// widget.sidePanel.color().rgb(240, 240, 240);
				// widget.sidePanel.border().color().lightgray();
			}
		}
	}

	@Override
	public void onChange(List<Object> selection) {
		widget.selection = selection;
		updateLinks();
	}

	void updateLinks() {
		for (NavigationLinkImpl label : widget.navigationLinks) {
			label.updateLabel(widget);
		}
	}

	abstract void selection(List<Object> selection);
}
