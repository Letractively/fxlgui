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
package co.fxl.gui.table.bulk.api;

import co.fxl.gui.api.IAlignment;
import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IKeyRecipient;
import co.fxl.gui.api.IMouseWheelListener;
import co.fxl.gui.api.IPoint;

public interface IBulkTableWidget extends IKeyRecipient<Object> {

	public interface IUpdateAdapter<T> {

		boolean isEditable();
	}

	public interface ITableClickListener {

		void onClick(int column, int row, IPoint point);
	}

	public interface ILabelMouseListener {

		void onOver(int column, int row);

		void onOut(int column, int row);

		void onClick(int column, int row);
	}

	public interface IColumn {

		IColumn title(String title);

		IColumn width(double width);

		IColumn width(int width);

		IAlignment<IColumn> align();
	}

	public interface IRow {

		IRow highlight(boolean highlight);

		boolean isHighlight();

		int gridIndex();
	}

	IBulkTableCell cell(int column, int row);

	IRow row(int row);

	IColumn column(int column);

	IClickable.IKey<?> addTableListener(ITableClickListener l);

	int height();

	int tableHeight();

	int rowCount();

	int rowHeight(int row);

	IBulkTableWidget height(int height);

	IBulkTableWidget addMouseWheelListener(IMouseWheelListener l);

	IBulkTableWidget labelMouseListener(int column, ILabelMouseListener l);

	IBulkTableWidget visible(boolean visible);

	IBulkTableWidget css(boolean css);

	IBulkTableWidget indent(int indent);

	void remove();

	IElement<?> element();

	IBulkTableWidget showAsLink(int column, int row, boolean asLink);

	IBulkTableWidget addToContextMenu(boolean addToContextMenu);

	IElement<?> elementAt(int index);
}
