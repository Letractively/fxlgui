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
package co.fxl.gui.table.util.api;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IFocusPanel;
import co.fxl.gui.api.IKeyRecipient;
import co.fxl.gui.api.IMouseWheelListener;
import co.fxl.gui.filter.api.IFilterConstraints;
import co.fxl.gui.table.util.api.IDragDropListener.IDragArea;

public interface ILazyScrollPane extends IMouseWheelListener {

	public interface IDecorator {

		IKeyRecipient<Object> decorate(IContainer container, int firstRow,
				int lastRow, boolean isCalibration);

		boolean checkIndex(int rowIndex);

		int headerHeight();

		int rowHeight(int rowIndex);

		IDragArea dragArea(int index);

		IFocusPanel getFocusPanel();
	}

	ILazyScrollPane size(int size);

	ILazyScrollPane rowIndex(int rowIndex);

	ILazyScrollPane minRowHeight(int height);

	ILazyScrollPane decorator(IDecorator decorator);

	ILazyScrollPane hasHeader(boolean hasHeader);

	ILazyScrollPane constraints(IFilterConstraints constraints,
			String filterQueryLabel);

	ILazyScrollPane dragDropListener(boolean allowInsertUnder,
			IDragDropListener l);

	ILazyScrollPane visible(boolean visible);

	ILazyScrollPane width(int width);

	ILazyScrollPane height(int height);

	ILazyScrollPane horizontalScrollPane(boolean horizontalScrollPane);

	ILazyScrollPane refresh();

	ILazyScrollPane adjustHeights(boolean adjustHeights);

	int scrollPosition();

	ILazyScrollPane scrollPosition(int scrollPosition);

	void remove();

	int rowIndex();

	ILazyScrollPane upDownIndex(IUpDownIndex index);

	boolean hasScrollbar();

	ILazyScrollPane useDecoratorReturnForDND(boolean useDecoratorReturnForDND);
}
