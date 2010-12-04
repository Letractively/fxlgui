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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;

class GWTGridPanel extends GWTPanel<Grid, IGridPanel> implements IGridPanel {

	class GridCell extends GWTContainer<Widget> implements IGridCell {

		int column;
		int row;

		GridCell(int column, int row) {
			super((GWTPanel<?, IGridPanel>) GWTGridPanel.this);
			this.column = column;
			this.row = row;
		}

		@Override
		public IElement<?> element() {
			return element;
		}

		private CellFormatter formatter() {
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
				void setColor(String color) {
					DOM.setStyleAttribute(formatter().getElement(row, column),
							"backgroundColor", color);
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
		public IGridCell clear() {
			throw new MethodNotImplementedException();
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

		@Override
		public IBorder border() {
			return new GWTWidgetBorder(formatter().getElement(row, column));
		}
	}

	private Map<Integer, Map<Integer, GridCell>> cells = new HashMap<Integer, Map<Integer, GridCell>>();
	private GridCell gridCell;

	@SuppressWarnings("unchecked")
	GWTGridPanel(GWTContainer<?> container) {
		super((GWTContainer<Grid>) container);
		super.container.setComponent(new Grid());
		super.container.widget.setWidth("100%");
		indent(0);
		spacing(0);
	}

	@Override
	public void add(Widget widget) {
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
		if (gridCell.column >= container.widget.getColumnCount()) {
			container.widget.resizeColumns(gridCell.column + 1);
		}
		if (gridCell.row >= container.widget.getRowCount()) {
			container.widget.resizeRows(gridCell.row + 1);
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
		return cells.size();
	}

	@Override
	public int rows() {
		return cells.get(0).size();
	}

	@Override
	public IKey<IGridPanel> addGridClickListener(
			final IGridClickListener listener) {
		final Grid grid = (Grid) container.widget;
		grid.addStyleName("cursor-pointer");
		grid.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Cell cell = grid.getCellForEvent(event);
				listener.onClick(cell.getCellIndex(), cell.getRowIndex());
			}
		});
		return new IKey<IGridPanel>() {

			@Override
			public IGridPanel altPressed() {
				throw new MethodNotImplementedException();
			}

			@Override
			public IGridPanel ctrlPressed() {
				throw new MethodNotImplementedException();
			}

			@Override
			public IGridPanel mouseLeft() {
				throw new MethodNotImplementedException();
			}

			@Override
			public IGridPanel mouseRight() {
				throw new MethodNotImplementedException();
			}

			@Override
			public IGridPanel shiftPressed() {
				throw new MethodNotImplementedException();
			}

			@Override
			public IGridPanel doubleClick() {
				throw new MethodNotImplementedException();
			}
		};
	}
}
