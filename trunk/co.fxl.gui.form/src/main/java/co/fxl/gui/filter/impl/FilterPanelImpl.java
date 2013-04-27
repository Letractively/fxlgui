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
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.impl.WidgetTitle;

public class FilterPanelImpl implements FilterPanel {

	private WidgetTitle title;
	private IVerticalPanel mainPanel;
	private IContainer gridContainer;
	FilterWidgetImpl widget;
	private IVerticalPanel top;

	FilterPanelImpl(FilterWidgetImpl widget, IContainer panel) {
		this.widget = widget;
		title = new WidgetTitle(panel.panel().vertical().addSpace(10).add()
				.panel(), true).sideWidget(true)// .grayBackground()
				.spacing(2);// .spaceBottom(2);
		title.addToContextMenu(true);
		IVerticalPanel vertical = title.content().panel().vertical();
		top = vertical.add().panel().vertical().visible(false);
		mainPanel = vertical.align().end().addSpace(2).add().panel()
				.horizontal().align().end().add().panel().vertical();
	}

	@Override
	public ILabel addTitle(String string) {
		return title.addTitle(string);
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

	@Override
	public void clearRowIterator() {
		widget.clearRowIndex();
	}

	@Override
	public IContainer top() {
		IGridPanel grid = top.visible(true).add().panel().grid();
		// grid.column(0).expand();
		grid.cell(1, 0).image().resource("empty_8x8.png");
		return grid.cell(0, 0);
	}

}
