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
package co.fxl.gui.api;

public interface IGridPanel extends IPanel<IGridPanel> {

	public interface IGridColumn {

		IGridColumn expand();

		IGridColumn width(int width);
	}

	public interface IGridRow {

		int height();

		IGridPanel remove();

		IGridPanel insert();
	}

	public interface IGridClickListener {

		void onClick(int column, int row);
	}

	interface IGridCell extends IContainer {

		IElement<?> element();

		IGridCell width(int width);

		IGridCell height(int height);

		int height();

		int width();

		IColor color();

		IGridCell visible(boolean visible);

		IAlignment<IGridCell> align();

		IAlignment<IGridCell> valign();

		IGridCell clear();

		IBorder border();
	}

	int columns();

	int rows();

	IGridPanel spacing(int pixel);

	IGridPanel indent(int pixel);

	IGridCell cell(int column, int row);

	IKey<IGridPanel> addGridClickListener(IGridClickListener listener);

	IGridPanel resize(int columns, int rows);

	IBorder cellBorder();

	IGridRow row(int row);

	IGridColumn column(int column);
}
