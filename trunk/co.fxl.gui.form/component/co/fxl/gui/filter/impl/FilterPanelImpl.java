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
import co.fxl.gui.api.IComboBox;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDockPanel;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IGridPanel.IGridCell;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ITextField;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.api.template.WidgetTitle;
import co.fxl.gui.form.impl.Heights;

class FilterPanelImpl implements FilterPanel {

	class CellImpl implements ICell {

		private IGridCell cell;

		CellImpl(IGridCell cell) {
			widget.heights.decorate(cell);
			this.cell = cell;
		}

		@Override
		public IComboBox comboBox() {
			return cell.comboBox();
		}

		@Override
		public IHorizontalPanel horizontal() {
			return cell.align().begin().panel().horizontal();
		}

		@Override
		public ITextField textField() {
			return cell.textField();
		}

		@Override
		public IDockPanel dock() {
			return cell.panel().dock();
		}
	}

	class FilterGridImpl implements FilterGrid {

		private IGridPanel grid;

		FilterGridImpl(IContainer gridContainer) {
			grid = gridContainer.panel().vertical().align().end().spacing(5)
					.add().panel().grid().spacing(0);
		}

		@Override
		public ICell cell(int row) {
			return new CellImpl(grid.cell(1, row));
		}

		@Override
		public void title(int filterIndex, String name) {
			IGridCell cell = grid.cell(0, filterIndex).width(50);
			widget.heights.decorate(cell);
			ILabel text = cell.align().end().valign().center().label()
					.autoWrap(true);
			text.text(name).font().color().black();
			int size = 11;
			// if (name.length() > 11)
			// size = 10;
			// if (name.length() > 16)
			size = 10;
			text.font().pixel(size);
		}

		@Override
		public void register(ITextField tf) {
			widget.register(tf);
		}

		@Override
		public Heights heights() {
			return widget.heights;
		}

		@Override
		public void resize(int size) {
			grid.resize(2, size);
		}
	}

	private WidgetTitle title;
	private IVerticalPanel mainPanel;
	private IContainer gridContainer;
	private FilterWidgetImpl widget;

	FilterPanelImpl(FilterWidgetImpl widget, IContainer panel) {
		this.widget = widget;
		title = new WidgetTitle(panel.panel()).grayBackground().space(2);
		mainPanel = title.content().panel().vertical().addSpace(4).add()
				.panel().horizontal().align().end().add().panel().vertical();
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
		return new FilterGridImpl(gridContainer);
	}

	@Override
	public IComboBox addComboBox() {
		IComboBox comboBox = mainPanel.add().comboBox();
		mainPanel.addSpace(4);
		return comboBox;
	}

	@Override
	public void visible() {
	}

}
