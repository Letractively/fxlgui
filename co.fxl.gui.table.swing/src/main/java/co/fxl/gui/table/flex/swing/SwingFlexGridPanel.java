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
package co.fxl.gui.table.flex.swing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.swing.PanelComponent;
import co.fxl.gui.swing.SwingContainer;
import co.fxl.gui.swing.SwingPanel;
import co.fxl.gui.table.flex.api.IFlexGridPanel;

public class SwingFlexGridPanel extends SwingPanel<IFlexGridPanel> implements
		IFlexGridPanel {

	private class FlexCell implements IFlexCell {

		private int x;
		private int y;
		private IContainer c;
		private int width = 1;
		private int height = 1;

		FlexCell(int x, int y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public IFlexCell columnSpan(int columns) {
			assert c == null;
			width = columns;
			return this;
		}

		@Override
		public IFlexCell rowSpan(int rows) {
			assert c == null;
			height = rows;
			return this;
		}

		@Override
		public IContainer container() {
			return c = SwingFlexGridPanel.this.add();
		}

		@Override
		public int width() {
			return container.element().width() / columns;
		}

		@Override
		public int height() {
			return c.element().height();
		}

	}

	private List<FlexCell> unassociatedCells = new LinkedList<FlexCell>();
	private GridBagLayout layout = new GridBagLayout();
	private int rows;
	private int columns;
	private int spacing = 0;

	@SuppressWarnings("unchecked")
	SwingFlexGridPanel(IContainer container) {
		super((SwingContainer<PanelComponent>) container);
		setLayout(layout);
	}

	@Override
	public IFlexCell cell(int x, int y) {
		FlexCell cell = new FlexCell(x, y);
		unassociatedCells.add(cell);
		return cell;
	}

	@Override
	public void add(JComponent component) {
		FlexCell cell = findCell(component);
		unassociatedCells.remove(cell);
		super.add(component);
		GridBagConstraints cellConstraints = newGridBagConstraints(cell.x,
				cell.y, cell.width, cell.height);
		layout.setConstraints(component, cellConstraints);
	}

	private GridBagConstraints newGridBagConstraints(int x, int y, int width,
			int height) {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.LINE_START;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = x;
		constraints.gridy = y;
		constraints.gridwidth = width;
		constraints.gridheight = height;
		constraints.weightx = 1;
		if (spacing != 0)
			constraints.insets = new Insets(spacing / 2, spacing / 2,
					spacing / 2, spacing / 2);
		return constraints;
	}

	@SuppressWarnings("rawtypes")
	private FlexCell findCell(JComponent widget) {
		for (FlexCell cell : unassociatedCells) {
			if (((SwingContainer) cell.c).component == widget)
				return cell;
		}
		throw new RuntimeException(widget + " not found");
	}

	@Override
	public IFlexGridPanel rows(int rows) {
		this.rows = rows;
		return this;
	}

	@Override
	public IFlexGridPanel columns(int columns) {
		this.columns = columns;
		return this;
	}

	@Override
	public IFlexGridPanel spacing(int spacing) {
		this.spacing = spacing;
		return this;
	}

}
