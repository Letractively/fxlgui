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
package co.fxl.gui.swing;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPanel;

import co.fxl.gui.api.IAlignment;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IGridPanel;

class SwingGridPanel extends SwingPanel<IGridPanel> implements IGridPanel {

	class GridCell extends SwingContainer<JComponent> implements IGridCell {

		private GridBagConstraints constraints;
		private VerticalLayoutManager vlayout = new VerticalLayoutManager(true);
		private JPanel panel = new JPanel(vlayout);

		GridCell(GridBagConstraints constraints) {
			super(SwingGridPanel.this);
			this.constraints = (GridBagConstraints) constraints.clone();
			panel.setOpaque(false);
		}

		private void update() {
			if (component != null)
				layout.setConstraints(panel, constraints);
		}

		public IGridCell weightX(double weight) {
			constraints.weightx = weight;
			update();
			return this;
		}

		public IGridCell weightY(double weight) {
			constraints.weighty = weight;
			update();
			return this;
		}

		@Override
		public IElement<?> element() {
			return new SwingElement<JComponent, IElement<?>>(this);
		}

		@Override
		public IGridCell height(int height) {
			throw new MethodNotImplementedException();
		}

		@Override
		public IGridCell width(int width) {
			vlayout.width = width;
			constraints.weightx = 0;
			update();
			return this;
		}

		@Override
		public IColor color() {
			return new SwingColor() {

				@Override
				protected void setColor(Color color) {
					panel.setOpaque(true);
					panel.setBackground(color);
				}
			};
		}

		@Override
		public IGridCell visible(boolean visible) {
			panel.setVisible(visible);
			return this;
		}

		@Override
		public IAlignment<IGridCell> align() {
			return new IAlignment<IGridCell>() {

				@Override
				public IGridCell begin() {
					vlayout.stretch = false;
					vlayout.alignment = VerticalLayoutManager.LEFT;
					return GridCell.this;
				}

				@Override
				public IGridCell center() {
					vlayout.stretch = false;
					vlayout.alignment = VerticalLayoutManager.CENTER;
					return GridCell.this;
				}

				@Override
				public IGridCell end() {
					vlayout.stretch = false;
					vlayout.alignment = VerticalLayoutManager.RIGHT;
					return GridCell.this;
				}
			};
		}

		@Override
		public IAlignment<IGridCell> valign() {
			return new IAlignment<IGridCell>() {

				@Override
				public IGridCell begin() {
					vlayout.anchor = VerticalLayoutManager.TOP;
					return GridCell.this;
				}

				@Override
				public IGridCell center() {
					vlayout.anchor = VerticalLayoutManager.CENTER;
					return GridCell.this;
				}

				@Override
				public IGridCell end() {
					vlayout.anchor = VerticalLayoutManager.BOTTOM;
					return GridCell.this;
				}
			};
		}

		@Override
		public IGridCell clear() {
			gridCell = this;
			SwingGridPanel.this.container.component.remove(panel);
			Color background = panel.getBackground();
			panel = new JPanel(vlayout);
			panel.setBackground(background);
			panel.setOpaque(false);
			return this;
		}

		@Override
		public IBorder border() {
			return new SwingBorder(panel);
		}
	}

	private GridBagConstraints constraints = new GridBagConstraints();
	private Map<Integer, Map<Integer, GridCell>> cells = new HashMap<Integer, Map<Integer, GridCell>>();
	private GridCell gridCell;
	private GridBagLayout layout;
	private Insets insets = new Insets(0, 0, 0, 0);

	SwingGridPanel(SwingContainer<JPanel> container) {
		super(container);
		setLayout(layout = new GridBagLayout());
		constraints.anchor = GridBagConstraints.LINE_START;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 1;
	}

	@Override
	public IGridPanel spacing(int pixel) {
		constraints.insets = new Insets(pixel, pixel, pixel, pixel);
		return this;
	}

	@Override
	public IGridPanel indent(int indent) {
		insets = new Insets(indent, indent, indent, indent);
		return this;
	}

	@Override
	public IGridCell cell(int column, int row) {
		gridCell = getCell(column, row);
		if (gridCell == null) {
			constraints.gridx = column;
			constraints.gridy = row;
			gridCell = new GridCell(constraints);
			putCell(column, row, gridCell);
		}
		return gridCell;
	}

	private void putCell(int columnIndex, int rowIndex, GridCell gridCell) {
		Map<Integer, GridCell> row = cells.get(columnIndex);
		if (row == null) {
			row = new HashMap<Integer, GridCell>();
			cells.put(columnIndex, row);
		}
		row.put(rowIndex, gridCell);
	}

	private GridCell getCell(int columnIndex, int rowIndex) {
		Map<Integer, GridCell> row = cells.get(columnIndex);
		if (row == null)
			return null;
		return row.get(rowIndex);
	}

	void add(JComponent component) {
		gridCell.vlayout.insets = insets;
		gridCell.panel.add(component);
		container.component.add(gridCell.panel, gridCell.constraints);
	}

	@Override
	public int columns() {
		return cells.size();
	}

	@Override
	public int rows() {
		return cells.get(0).size();
	}
}
