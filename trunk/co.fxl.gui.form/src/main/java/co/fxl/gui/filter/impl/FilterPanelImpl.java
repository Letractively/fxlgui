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
package co.fxl.gui.filter.impl;

import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.impl.WidgetTitle;

public class FilterPanelImpl implements FilterPanel {

	private WidgetTitle title;
	private IVerticalPanel mainPanel;
	private IContainer gridContainer;
	FilterWidgetImpl widget;

	FilterPanelImpl(FilterWidgetImpl widget, IContainer panel) {
		this.widget = widget;
		title = new WidgetTitle(panel.panel().vertical().addSpace(10).add()
				.panel(), true).sideWidget(true)// .grayBackground()
				.spacing(2);//.spaceBottom(2);
		title.addToContextMenu(true);
		mainPanel = title.content().panel().vertical().align().end()
				.addSpace(2).add().panel().horizontal().align().end().add()
				.panel().vertical();
	}

	@Override
	public void addTitle(String string) {
		title.addTitle(string);
	}

	@Override
	public IClickable<?> addHyperlink(String imageResource, String string) {
		return title.addHyperlink(imageResource, string);
	}

	@Override
	public FilterGrid filterGrid() {
		if (gridContainer == null) {
			gridContainer = mainPanel.align().end().add();
		} else
			gridContainer.clear();
		return new FilterGridImpl(this, gridContainer);
	}

	@Override
	public ViewComboBox viewComboBox() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void visible() {
	}

	@Override
	public WidgetTitle widgetTitle() {
		return title;
	}

}
