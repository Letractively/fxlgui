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

import java.util.HashMap;
import java.util.Map;

import co.fxl.gui.api.ICardPanel;
import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IComboBox;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDockPanel;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ITextField;
import co.fxl.gui.api.IUpdateable.IUpdateListener;
import co.fxl.gui.impl.Heights;
import co.fxl.gui.impl.IToolbar;
import co.fxl.gui.impl.ToolbarImpl;

class MiniFilterPanel implements FilterPanel {

	class ViewComboBoxImpl implements ViewComboBox {

		private IComboBox comboBox;

		ViewComboBoxImpl(IComboBox comboBox) {
			this.comboBox = comboBox;
		}

		@Override
		public void text(String c) {
			if (c != null)
				comboBox.text(c);
		}

		@Override
		public void addUpdateListener(IUpdateListener<String> l) {
			comboBox.addUpdateListener(l);
		}

		@Override
		public void addText(String c) {
			comboBox.addText(c);
		}

	}

	class FilterGridImpl implements FilterGrid, IUpdateListener<String> {

		class CellImpl implements ICell {

			private IContainer container;

			CellImpl() {
				container = cardPanel.add();
			}

			@Override
			public IComboBox comboBox() {
				return container.comboBox();
			}

			@Override
			public IHorizontalPanel horizontal() {
				return container.panel().horizontal();
			}

			@Override
			public ITextField textField() {
				return container.textField();
			}

			@Override
			public IDockPanel dock() {
				return container.panel().dock();
			}
		}

		private IComboBox comboBox;
		private ICardPanel cardPanel;
		private Map<String, Integer> name2index = new HashMap<String, Integer>();
		private Map<Integer, CellImpl> index2cell = new HashMap<Integer, CellImpl>();
		int rowInc = 0;

		FilterGridImpl(IContainer container) {
			IHorizontalPanel panel = container.panel().horizontal();
			comboBox = panel.add().comboBox();
			styleInput(comboBox);
			panel.addSpace(4);
			cardPanel = panel.add().panel().card();
		}

		private void styleInput(IComboBox comboBox) {
			comboBox.border().color().rgb(211, 211, 211);
			comboBox.color().rgb(249, 249, 249);
		}

		void visible() {
			cardPanel.show(index2cell.get(0).container.element());
			comboBox.addUpdateListener(this);
		}

		@Override
		public ICell cell(int row) {
			CellImpl cellImpl = index2cell.get(row + rowInc);
			if (cellImpl == null) {
				cellImpl = new CellImpl();
				index2cell.put(row + rowInc, cellImpl);
			}
			return cellImpl;
		}

		@Override
		public void title(int filterIndex, String name) {
			comboBox.addText(name);
			name2index.put(name, filterIndex + rowInc);
		}

		@Override
		public void onUpdate(String value) {
			cardPanel.show(index2cell.get(name2index.get(value)).container
					.element());
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
		}

		@Override
		public void show(FilterPart<?> firstConstraint) {
			if (firstConstraint != null)
				comboBox.text(firstConstraint.name());
		}

		@Override
		public FilterGrid rowInc(int rowInc) {
			this.rowInc = rowInc;
			return this;
		};
	}

	private IToolbar panel;
	private IToolbar titlePanel;
	private IToolbar mainPanel;
	private IToolbar hyperLinkPanel;
	private IContainer gridContainer;
	private FilterGridImpl grid;
	// private boolean hasHyperlinks = false;
	private MiniFilterWidgetImpl widget;
	private ViewComboBox viewComboBox;

	MiniFilterPanel(MiniFilterWidgetImpl widget, IContainer c) {
		this.widget = widget;
		panel = new ToolbarImpl(c.panel().horizontal().add().panel()
				.horizontal().align().begin().add().panel().horizontal()
				.align().begin().add());
		titlePanel = new ToolbarImpl(panel.add());
		mainPanel = new ToolbarImpl(panel.add().panel().horizontal().add())// .addSpace(4)
		;
		hyperLinkPanel = panel// .addSpace(4)
				.addGroup();
	}

	@Override
	public void visible() {
		grid.visible();
	}

	@Override
	public void addTitle(String string) {
		titlePanel.add().label().text(string.toUpperCase()).font().weight()
				.bold();
	}

	@Override
	public IClickable<?> addHyperlink(String imageResource, String string) {
		// if (hasHyperlinks) {
		// hyperLinkPanel.addSpace(4).add().label().text("|").font().color()
		// .gray();
		// }
		// hasHyperlinks = true;
		return hyperLinkPanel.add().image().resource(imageResource);// .label().text(string).hyperlink();
	}

	@Override
	public FilterGrid filterGrid() {
		if (gridContainer == null) {
			gridContainer = mainPanel.add();
		} else
			gridContainer.clear();
		return grid = new FilterGridImpl(gridContainer);
	}

	@Override
	public ViewComboBox viewComboBox() {
		if (viewComboBox != null)
			return viewComboBox;
		if (grid == null) {
			filterGrid();
		}
		grid.rowInc = 1;
		grid.title(-1, "View");
		return new ViewComboBoxImpl(grid.cell(-1).comboBox());
	}

}
