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
package co.fxl.gui.gwt;

import java.util.HashMap;
import java.util.Map;

import co.fxl.gui.api.IAlignment;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IGridPanel;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Widget;

public class GWTGridPanel extends GWTPanel<HTMLTable, IGridPanel> implements
		IGridPanel {

	public class GridCell extends GWTContainer<Widget> implements IGridCell {

		int column;
		int row;

		GridCell(int column, int row) {
			super((GWTPanel<?, IGridPanel>) GWTGridPanel.this);
			this.column = column;
			this.row = row;
		}

		@Override
		public IGridCell clear() {
			gridCell = this;
			GWTGridPanel.this.container.widget.remove(container.widget);
			return this;
		}

		@Override
		public IElement<?> element() {
			return element;
		}

		public CellFormatter formatter() {
			return container.widget.getCellFormatter();
		}

		@Override
		public IGridCell height(int height) {
			formatter().setHeight(row, column, height + "px");
			return this;
		}

		@Override
		public IGridCell width(int width) {
			formatter().setWidth(row, column, width + "px");
			return this;
		}

		@Override
		public IColor color() {
			return new GWTStyleColor(null) {
				@Override
				public void setColor(String color) {
					DOM.setStyleAttribute(formatter().getElement(row, column),
							"backgroundColor", color);
				}

				@Override
				public IColor remove() {
					com.google.gwt.dom.client.Style stylable = formatter()
							.getElement(row, column).getStyle();
					stylable.setBackgroundColor(null);
					return this;
				}
			};
		}

		@Override
		public IGridCell visible(boolean visible) {
			formatter().setVisible(row, column, visible);
			return this;
		}

		@Override
		public IAlignment<IGridCell> align() {
			return new IAlignment<IGridCell>() {

				@Override
				public IGridCell begin() {
					formatter().setHorizontalAlignment(row, column,
							HasHorizontalAlignment.ALIGN_LEFT);
					return GridCell.this;
				}

				@Override
				public IGridCell center() {
					formatter().setHorizontalAlignment(row, column,
							HasHorizontalAlignment.ALIGN_CENTER);
					return GridCell.this;
				}

				@Override
				public IGridCell end() {
					formatter().setHorizontalAlignment(row, column,
							HasHorizontalAlignment.ALIGN_RIGHT);
					return GridCell.this;
				}
			};
		}

		@Override
		public IAlignment<IGridCell> valign() {
			return new IAlignment<IGridCell>() {

				@Override
				public IGridCell begin() {
					formatter().setVerticalAlignment(row, column,
							HasVerticalAlignment.ALIGN_TOP);
					return GridCell.this;
				}

				@Override
				public IGridCell center() {
					formatter().setVerticalAlignment(row, column,
							HasVerticalAlignment.ALIGN_MIDDLE);
					return GridCell.this;
				}

				@Override
				public IGridCell end() {
					formatter().setVerticalAlignment(row, column,
							HasVerticalAlignment.ALIGN_BOTTOM);
					return GridCell.this;
				}
			};
		}

		// TODO optimize, use border for grid lines! 1000ms grid -> 600ms grid

		@Override
		public IBorder border() {
			return new GWTWidgetBorder(formatter().getElement(row, column));
		}

		@Override
		public int height() {
			return element.height();
		}

		@Override
		public int width() {
			throw new MethodNotImplementedException();
		}
	}

	private Map<Integer, Map<Integer, GridCell>> cells = new HashMap<Integer, Map<Integer, GridCell>>();
	private GridCell gridCell;
	private String borderType;
	private String borderConfiguration;

	@SuppressWarnings("unchecked")
	protected GWTGridPanel(GWTContainer<?> container) {
		super((GWTContainer<HTMLTable>) container);
		super.container.setComponent(new Grid());
		super.container.widget.setWidth("100%");
		indent(0);
		spacing(0);
	}

	@Override
	public void add(Widget widget) {
		if (borderType != null) {
			DOM.setStyleAttribute(container.widget.getCellFormatter()
					.getElement(gridCell.row, gridCell.column), borderType,
					borderConfiguration);
		}
		container.widget.setWidget(gridCell.row, gridCell.column, widget);
		CellFormatter formatter = gridCell.formatter();
		formatter.setHeight(gridCell.row, gridCell.column, "100%");
		widget.setWidth("100%");
		widget.setHeight("100%");
	}

	@Override
	public IGridPanel indent(int pixel) {
		container.widget.setCellPadding(pixel);
		return this;
	}

	@Override
	public IGridPanel spacing(int pixel) {
		container.widget.setCellSpacing(pixel);
		return this;
	}

	@Override
	public IGridCell cell(int column, int row) {
		gridCell = getCell(column, row);
		if (gridCell == null) {
			gridCell = new GridCell(column, row);
			putCell(column, row, gridCell);
			resize();
		}
		return gridCell;
	}

	private void resize() {
		Grid grid = (Grid) container.widget;
		if (gridCell.column >= grid.getColumnCount()) {
			grid.resizeColumns(gridCell.column + 1);
		}
		if (gridCell.row >= grid.getRowCount()) {
			grid.resizeRows(gridCell.row + 1);
		}
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

	@Override
	public int columns() {
		Grid grid = (Grid) container.widget;
		return grid.getColumnCount();
	}

	@Override
	public int rows() {
		Grid grid = (Grid) container.widget;
		return grid.getRowCount();
	}

	@Override
	public IKey<IGridPanel> addGridClickListener(IGridClickListener listener) {
		return new GWTGridPanelClickHandler(this, listener);
	}

	@Override
	public IGridPanel prepare(int columns, int rows) {
		Grid grid = (Grid) container.widget;
		grid.resize(rows, columns);
		return this;
	}

	@Override
	public IBorder cellBorder() {
		return new GWTBorder() {

			@Override
			public void remove() {
				GWTGridPanel.this.borderType = null;
			}

			@Override
			protected void update() {
				GWTGridPanel.this.borderType = borderType;
				GWTGridPanel.this.borderConfiguration = width + "px " + color
						+ " " + style;
			}
		};
	}
}
