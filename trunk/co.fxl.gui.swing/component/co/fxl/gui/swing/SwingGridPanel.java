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
import java.awt.Component;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPanel;

import co.fxl.gui.api.IAlignment;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.template.KeyTemplate;

class SwingGridPanel extends SwingPanel<IGridPanel> implements IGridPanel {

	class GridClickListenerAdapter extends KeyTemplate<IGridPanel> {

		private IGridClickListener listener;

		protected GridClickListenerAdapter(IGridPanel element,
				IGridClickListener listener) {
			super(element);
			this.listener = listener;
		}

		public void onClick(MouseEvent e, int column, int row) {
			for (KeyType pressedKey : pressedKeys.keySet()) {
				Boolean check = pressedKeys.get(pressedKey);
				if (ClickListenerMouseAdapter.keyMatches(pressedKey, e) != check)
					return;
			}
			if (e.getButton() != getButton())
				return;
			if (isDoubleClick && e.getClickCount() < 2)
				return;
			SwingDisplay d = (SwingDisplay) ((IElement<?>) element).display();
			if (d.waiting)
				return;
			listener.onClick(column, row);
		}

		private int getButton() {
			if (buttonType == ButtonType.LEFT)
				return MouseEvent.BUTTON1;
			else
				return MouseEvent.BUTTON3;
		}
	}

	class GridCell extends SwingContainer<JComponent> implements IGridCell {

		private GridBagConstraints constraints;
		private VerticalLayoutManager vlayout = new VerticalLayoutManager(true);
		private JPanel panel = new JPanel(vlayout);

		GridCell(GridBagConstraints constraints) {
			super(SwingGridPanel.this);
			this.constraints = (GridBagConstraints) constraints.clone();
			panel.setOpaque(false);
		}

		@Override
		void setComponent(final JComponent component) {
			if (component instanceof TextFieldComponent) {
				super.setComponent(component);
				final TextFieldComponent tfc = (TextFieldComponent) component;
				ComponentAdapter adp = new ComponentAdapter() {
					@Override
					public void componentResized(ComponentEvent e) {
						int width = panel.getPreferredSize().width;
						tfc.setPreferredWidth(width);
					}
				};
				panel.addComponentListener(adp);
			} else
				super.setComponent(component);
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
		public IGridCell height(int height) {
			vlayout.height = height;
			constraints.weighty = 0;
			update();
			return this;
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
					panel.setOpaque(color != null);
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

		@Override
		public int height() {
			return panel.getPreferredSize().height;
		}

		@Override
		public int width() {
			throw new MethodNotImplementedException();
		}

		void remove() {
			SwingGridPanel.this.container.component.remove(panel);
			SwingGridPanel.this
					.removeCell(constraints.gridx, constraints.gridy);
		}
	}

	private GridBagConstraints constraints = new GridBagConstraints();
	private Map<Integer, Map<Integer, GridCell>> cells = new HashMap<Integer, Map<Integer, GridCell>>();
	private GridCell gridCell;
	private GridBagLayout layout;
	private Insets insets = new Insets(0, 0, 0, 0);
	private boolean hasClickListener = false;
	private List<GridClickListenerAdapter> gridClickListeners = new LinkedList<GridClickListenerAdapter>();
	private int spacing;
	private int sizeColumns = -1;
	private int sizeRows = -1;

	SwingGridPanel(SwingContainer<PanelComponent> container) {
		super(container);
		setLayout(layout = new GridBagLayout());
		constraints.anchor = GridBagConstraints.LINE_START;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 1;
	}

	private void setupClickListener() {
		if (hasClickListener)
			return;
		hasClickListener = true;
		container.component.setCursor(new Cursor(Cursor.HAND_CURSOR));
		container.component.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				Point p = e.getPoint();
				Component c = container.component.getComponentAt(p);
				if (c == null)
					return;
				GridBagConstraints gbc = layout.getConstraints(c);
				int column = gbc.gridx;
				int row = gbc.gridy;
				for (GridClickListenerAdapter cl : gridClickListeners) {
					cl.onClick(e, column, row);
				}
			}
		});
	}

	@Override
	public IKey<IGridPanel> addGridClickListener(IGridClickListener listener) {
		setupClickListener();
		GridClickListenerAdapter adp = new GridClickListenerAdapter(this,
				listener);
		gridClickListeners.add(adp);
		return adp;
	}

	@Override
	public IGridPanel spacing(int pixel) {
		// container.component.getInsets().set(pixel / 2, pixel / 2, pixel / 2,
		// pixel / 2);
		spacing = pixel;
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
			if (spacing != 0) {
				assert sizeColumns != -1;
				int top = row == 0 ? spacing : spacing / 2;
				int left = column == 0 ? spacing : spacing / 2;
				int bottom = row == sizeRows - 1 ? spacing : spacing / 2;
				int right = column == sizeColumns - 1 ? spacing : spacing / 2;
				constraints.insets = new Insets(top, left, bottom, right);
			}
			gridCell = new GridCell(constraints);
			putCell(column, row, gridCell);
		}
		return gridCell;
	}

	private void putCell(int columnIndex, int rowIndex, GridCell gridCell) {
		Map<Integer, GridCell> row = cells.get(rowIndex);
		if (row == null) {
			row = new HashMap<Integer, GridCell>();
			cells.put(rowIndex, row);
		}
		row.put(columnIndex, gridCell);
	}

	private GridCell getCell(int columnIndex, int rowIndex) {
		Map<Integer, GridCell> row = cells.get(rowIndex);
		if (row == null)
			return null;
		return row.get(columnIndex);
	}

	private GridCell removeCell(int columnIndex, int rowIndex) {
		Map<Integer, GridCell> row = cells.get(rowIndex);
		if (row == null)
			return null;
		GridCell remove = row.remove(columnIndex);
		if (row.isEmpty())
			cells.remove(rowIndex);
		return remove;
	}

	@Override
	public void add(JComponent component) {
		gridCell.vlayout.insets = insets;
		gridCell.panel.add(component);
		container.component.add(gridCell.panel, gridCell.constraints);
	}

	@Override
	public int columns() {
		if (cells == null || cells.isEmpty())
			return 0;
		return cells.get(0).size();
	}

	@Override
	public int rows() {
		if (cells == null)
			return 0;
		return cells.size();
	}

	@Override
	public IGridPanel resize(int columns, int rows) {
		sizeColumns = columns;
		sizeRows = rows;
		if (columns < columns())
			throw new MethodNotImplementedException();
		if (rows < rows()) {
			for (int r = rows; r < rows(); r++)
				row(r).remove();
		}
		return this;
	}

	@Override
	public IBorder cellBorder() {
		throw new MethodNotImplementedException();
	}

	@Override
	public IGridRow row(final int row) {
		return new IGridRow() {

			@Override
			public int height() {
				int max = 0;
				for (int column = 0; column < columns(); column++) {
					max = Math.max(max, cell(column, row).height());
				}
				return max;
			}

			@Override
			public IGridPanel remove() {
				for (int c = 0; c < columns(); c++) {
					removeCell(c, row).remove();
				}
				// TODO adjust indices constraints.gridy & cells map 
				if (true) {
					throw new MethodNotImplementedException();
				}
				return update();
			}

			@Override
			public IGridPanel insert() {
				for (int r = rows() - 1; r >= row; r--) {
					for (int c = 0; c < columns(); c++) {
						GridCell cell = getCell(c, r);
						cell.constraints.gridy++;
						layout.setConstraints(cell.panel, cell.constraints);
					}
					Map<Integer, GridCell> row = cells.remove(r);
					cells.put(r + 1, row);
				}
				return update();
			}

			private IGridPanel update() {
				SwingGridPanel.this.container.component.updateUI();
				return SwingGridPanel.this;
			}
		};
	}

	@Override
	public IGridColumn column(int column) {
		return new IGridColumn() {

			@Override
			public IGridColumn expand() {
				// TODO ...
				return this;
			}
		};
	}
}
