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
package co.fxl.gui.table.scroll.impl;

import co.fxl.gui.api.IColored.IColor;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IFocusPanel;
import co.fxl.gui.api.IKeyRecipient;
import co.fxl.gui.impl.ColorTemplate;
import co.fxl.gui.impl.Constants;
import co.fxl.gui.table.util.api.IDragDropListener;
import co.fxl.gui.table.util.api.IDragDropListener.IDragArea;
import co.fxl.gui.table.util.api.ILazyScrollPane.IDecorator;
import co.fxl.gui.table.util.impl.DragAndDrop;
import co.fxl.gui.table.util.impl.DragAndDrop.DragAndDropAdapter;

class DragAndDropGridAdapter implements DragAndDropAdapter {

	private static final boolean ALLOW_DRAG_AND_DROP = Constants.get(
			"DragAndDropGridAdapter.ALLOW_DRAG_AND_DROP", true);
	private ScrollTableWidgetImpl widget;

	DragAndDropGridAdapter(ScrollTableWidgetImpl widget) {
		this.widget = widget;
		if (ALLOW_DRAG_AND_DROP && widget.grid.focusPanel() != null)
			new DragAndDrop(this, widget.grid.focusPanel());
	}

	@Override
	public IDragDropListener dragDropListener() {
		return widget.dragDropListener;
	}

	@Override
	public IDecorator decorator() {
		return new IDecorator() {

			@Override
			public IKeyRecipient<Object> decorate(IContainer container,
					int firstRow, int lastRow, boolean isCalibration) {
				throw new UnsupportedOperationException();
			}

			@Override
			public boolean checkIndex(int rowIndex) {
				throw new UnsupportedOperationException();
			}

			@Override
			public int headerHeight() {
				return widget.grid.rowHeight(0);
			}

			@Override
			public int rowHeight(int rowIndex) {
				return widget.grid.rowHeight(rowIndex + 1);
			}

			@Override
			public IDragArea dragArea(final int index) {
				return new IDragArea() {

					@Override
					public IColor color() {
						return new ColorTemplate() {

							@Override
							public IColor remove() {
								widget.grid.row(index).removeBackground();
								return this;
							}

							@Override
							protected IColor setRGB(int r, int g, int b) {
								widget.grid.row(index).background(r, g, b);
								return this;
							}
						};
					}

					@Override
					public IElement<?> imageElement() {
						return widget.grid.elementAt(0, index);
					}
				};
			}

			@Override
			public IFocusPanel getFocusPanel() {
				throw new UnsupportedOperationException();
			}

		};
	}

	@Override
	public int rowIndex() {
		return 0;
	}

	@Override
	public int lastIndex() {
		return widget.rows.size() - 1;
	}

	@Override
	public boolean hasHeader() {
		return true;
	}

	@Override
	public int rowHeight(int index) {
		return widget.grid.rowHeight(index);
	}

	@Override
	public void refreshNow() {
		widget.refresh();
	}

	@Override
	public boolean allowInsertUnder() {
		return false;
	}

}
