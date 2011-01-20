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

class MiniFilterPanel implements FilterPanel {

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

		FilterGridImpl(IContainer container) {
			IHorizontalPanel panel = container.panel().horizontal();
			comboBox = panel.add().comboBox();
			panel.addSpace(4);
			cardPanel = panel.add().panel().card();
		}

		void visible() {
			cardPanel.show(index2cell.get(0).container.element());
			comboBox.addUpdateListener(this);
		}

		@Override
		public ICell cell(int row) {
			CellImpl cellImpl = index2cell.get(row);
			if (cellImpl == null) {
				cellImpl = new CellImpl();
				index2cell.put(row, cellImpl);
			}
			return cellImpl;
		}

		@Override
		public void title(int filterIndex, String name) {
			comboBox.addText(name);
			name2index.put(name, filterIndex);
		}

		@Override
		public void onUpdate(String value) {
			cardPanel.show(index2cell.get(name2index.get(value)).container
					.element());
		}

		@Override
		public void register(ITextField tf) {
			widget.register(tf);
		};
	}

	private IHorizontalPanel panel;
	private IHorizontalPanel titlePanel;
	private IHorizontalPanel mainPanel;
	private IHorizontalPanel hyperLinkPanel;
	private IContainer gridContainer;
	private FilterGridImpl grid;
	private boolean hasHyperlinks = false;
	private MiniFilterWidgetImpl widget;

	MiniFilterPanel(MiniFilterWidgetImpl widget, IContainer c) {
		this.widget = widget;
		panel = c.panel().horizontal().add().panel().horizontal();
		titlePanel = panel.add().panel().horizontal();
		mainPanel = panel.addSpace(4).add().panel().horizontal();
		hyperLinkPanel = panel.addSpace(4).add().panel().horizontal();
	}

	@Override
	public void visible() {
		grid.visible();
	}

	@Override
	public void addTitle(String string) {
		titlePanel.add().label().text(string).font().weight().bold();
	}

	@Override
	public IClickable<?> addHyperlink(String string) {
		if (hasHyperlinks) {
			hyperLinkPanel.addSpace(4).add().label().text("|").font().color()
					.gray();
		}
		hasHyperlinks = true;
		return hyperLinkPanel.addSpace(4).add().label().text(string)
				.hyperlink();
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
	public IComboBox addComboBox() {
		IComboBox comboBox = mainPanel.add().comboBox();
		mainPanel.addSpace(4);
		return comboBox;
	}

}
