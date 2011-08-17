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
package co.fxl.gui.table.bulk.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import co.fxl.gui.api.IAlignment;
import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.api.ICheckBox;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IClickable.IKey;
import co.fxl.gui.api.IColored.IColor;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IGridPanel.IGridCell;
import co.fxl.gui.api.IGridPanel.IGridClickListener;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IMouseOverElement.IMouseOverListener;
import co.fxl.gui.api.IMouseWheelListener;
import co.fxl.gui.api.IUpdateable.IUpdateListener;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.impl.AlignmentMemento;
import co.fxl.gui.impl.ContextMenu;
import co.fxl.gui.table.bulk.api.IBulkTableWidget;

class BulkTableWidgetImpl implements IBulkTableWidget {

	private final class ColumnImpl implements IColumn {
		private final int column;
		private ILabel label;
		@SuppressWarnings("unused")
		private double widthDouble;
		private AlignmentMemento<IColumn> align = new AlignmentMemento<IColumn>(
				this);

		private ColumnImpl(int column) {
			this.column = column;
		}

		@Override
		public IColumn title(String title) {
			rowOffset = 1;
			if (label == null) {
				IGridCell cell = grid.cell(column, 0);
				IBorder b = cell.border();
				b.color().black();
				b.style().bottom();
				label = cell.label();
				label.font().pixel(14).weight().bold();
				align.forward(cell.align());
			}
			label.text(title);
			return this;
		}

		@Override
		public IColumn width(double width) {
			// TODO ...
			widthDouble = width;
			return this;
		}

		@Override
		public IColumn width(int width) {
			throw new MethodNotImplementedException();
		}

		@Override
		public IAlignment<IColumn> align() {
			return align;
		}
	}

	private IGridPanel grid;
	private int rowOffset = 0;
	private IVerticalPanel mainPanel;
	private int numRows = 0;
	private List<ColumnImpl> columns = new LinkedList<ColumnImpl>();
	private Map<Integer, Boolean> highlighted = new HashMap<Integer, Boolean>();
	private boolean addToContextMenu = true;

	BulkTableWidgetImpl(IContainer container) {
		mainPanel = container.panel().vertical();
		grid = mainPanel.add().panel().grid();
		grid.spacing(0);
		grid.indent(3);
		grid.addGridClickListener(new IGridClickListener() {

			@Override
			public void onClick(int column, int row) {
				if (addToContextMenu)
					ContextMenu.instance().show();
			}
		}).mouseRight();
	}

	@Override
	public void remove() {
		mainPanel.remove();
	}

	@Override
	public IColumn column(final int column) {
		while (columns.size() <= column) {
			ColumnImpl c = new ColumnImpl(columns.size());
			columns.add(c);
		}
		return columns.get(column);
	}

	@Override
	public ICell cell(final int column, final int row) {
		return new ICell() {

			private ICheckBox checkBox;
			private IUpdateAdapter<Boolean> updateAdapter;

			@Override
			public ICell text(String text) {
				IGridCell cell = grid.cell(column, row + rowOffset);
				align(column, cell);
				cell.label().text(text);
				IBorder b = cell.border();
				b.color().lightgray();
				b.style().bottom();
				if (row + 1 > numRows)
					numRows = row + 1;
				return this;
			}

			@Override
			public ICell checkBox(Boolean value) {
				IGridCell cell = grid.cell(column, row + rowOffset);
				align(column, cell);
				if (value != null) {
					checkBox = cell.checkBox().checked(value).editable(false);
				} else {
					cell.label();
				}
				decorate(column, row, cell);
				return this;
			}

			private void decorate(final int column, final int row,
					IGridCell cell) {
				IBorder b = cell.border();
				b.color().lightgray();
				b.style().bottom();
				if (row + 1 > numRows)
					numRows = row + 1;
			}

			private void align(final int column, IGridCell cell) {
				cell.valign().center();
				columns.get(column).align.forward(cell.align());
			}

			@Override
			public ICell updateListener(
					final IUpdateListener<Boolean> updateListener) {
				if (checkBox != null) {
					checkBox.editable(true);
					checkBox.addUpdateListener(new IUpdateListener<Boolean>() {
						@Override
						public void onUpdate(Boolean value) {
							if (updateAdapter == null)
								updateListener.onUpdate(value);
							else {
								if (!updateAdapter.isEditable())
									checkBox.checked(!value);
								else
									updateListener.onUpdate(value);
							}
						}
					});
				} else
					throw new MethodNotImplementedException();
				return this;
			}

			@Override
			public IContainer container() {
				IGridCell cell = grid.cell(column, row + rowOffset);
				decorate(column, row, cell);
				return cell;
			}

			@Override
			public ICell updateAdapter(IUpdateAdapter<Boolean> updateAdapter) {
				this.updateAdapter = updateAdapter;
				return this;
			}

			@Override
			public ICell html(String text) {
				throw new MethodNotImplementedException();
			}

			@Override
			public ICell color(String color) {
				throw new MethodNotImplementedException();
			}

			@Override
			public ICell image(String image) {
				throw new MethodNotImplementedException();
			}
		};
	}

	@Override
	public IRow row(final int row) {
		return new IRow() {

			@Override
			public IRow highlight(boolean highlight) {
				for (int c = 0; c < grid.columns(); c++) {
					IColor color = grid.cell(c, row + rowOffset).color();
					if (highlight) {
						color.mix().white().lightgray();
					} else
						color.remove();
					highlighted.put(row, highlight);
				}
				return this;
			}

			@Override
			public boolean isHighlight() {
				Boolean highlight = highlighted.get(row);
				return highlight != null && highlight;
			}

			@Override
			public int gridIndex() {
				return row + 1;
			}
		};
	}

	@Override
	public IKey<?> addTableListener(final ITableClickListener l) {
		return grid.addGridClickListener(new IGridClickListener() {

			@Override
			public void onClick(int column, int row) {
				l.onClick(column, row);
			}
		});
	}

	@Override
	public int height() {
		return mainPanel.height();
	}

	@Override
	public IBulkTableWidget visible(boolean visible) {
		if (!visible)
			throw new MethodNotImplementedException();
		return this;
	}

	@Override
	public IBulkTableWidget height(int height) {
		mainPanel.height(height);
		return this;
	}

	@Override
	public IElement<?> element() {
		return mainPanel;
	}

	@Override
	public int tableHeight() {
		return grid.height();
	}

	@Override
	public IBulkTableWidget addMouseWheelListener(IMouseWheelListener l) {
		// TODO ...
		return this;
	}

	@Override
	public IBulkTableWidget labelMouseListener(final int column,
			final ILabelMouseListener listener) {
		for (int row = 0; row < grid.rows(); row++) {
			final int rowF = row;
			IGridCell cell = grid.cell(column, row);
			ILabel l = (ILabel) cell.element();
			l.addMouseOverListener(new IMouseOverListener() {

				@Override
				public void onMouseOver() {
					listener.onOver(column, rowF);
				}

				@Override
				public void onMouseOut() {
					listener.onOut(column, rowF);
				}
			});
			l.addClickListener(new IClickListener() {

				@Override
				public void onClick() {
					listener.onClick(column, rowF);
				}
			});
		}
		return this;
	}

	@Override
	public IBulkTableWidget showAsLink(int column, int row, boolean asLink) {
		ILabel l = (ILabel) grid.cell(column, row).element();
		l.font().underline(asLink);
		if (asLink)
			l.font().color().blue();
		else
			l.font().color().black();
		return this;
	}

	@Override
	public int rowCount() {
		return grid.rows();
	}

	@Override
	public int rowHeight(int row) {
		return grid.row(row).height();
	}

	@Override
	public IBulkTableWidget css(boolean css) {
		throw new MethodNotImplementedException();
	}

	@Override
	public IBulkTableWidget addToContextMenu(boolean addToContextMenu) {
		this.addToContextMenu = addToContextMenu;
		return this;
	}
}
