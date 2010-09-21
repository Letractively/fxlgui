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
package co.fxl.gui.table.impl;

import java.util.Date;

import co.fxl.gui.api.IFontElement;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.api.IGridPanel.IGridCell;

abstract class CellFactory<R, T> {

	static ImageCellFactory IMAGE_FACTORY = new ImageCellFactory();
	static BooleanCellFactory BOOLEAN_FACTORY = new BooleanCellFactory();
	static DateCellFactory DATE_FACTORY = new DateCellFactory();
	static ComparableCellFactory<String> STRING_FACTORY = new ComparableCellFactory<String>();
	static ComparableCellFactory<Comparable<?>> COMPARABLE_FACTORY = new ComparableCellFactory<Comparable<?>>();

	abstract Cell<R> create(TableWidgetImpl table, RowImpl row,
			int columnIndex, IGridCell cell, T value);

	IPanel<?> panel(IGridCell cell) {
		IHorizontalPanel panel = cell.panel().horizontal().spacing(2);
		panel.align().center();
		return panel;

	}

	void font(IFontElement fontElement) {
		fontElement.font().pixel(12);
	}

	static Cell<?> createCellContent(TableWidgetImpl table, RowImpl row,
			int columnIndex, IGridCell cell, Object value) {
		Class<?> type = getType(table, columnIndex, value);
		if (type.equals(IImage.class)) {
			return IMAGE_FACTORY.create(table, row, columnIndex, cell,
					(String) value);
		} else if (type.equals(Boolean.class)) {
			return BOOLEAN_FACTORY.create(table, row, columnIndex, cell,
					(Boolean) value);
		} else if (type.equals(String.class)) {
			return STRING_FACTORY.create(table, row, columnIndex, cell,
					(String) value);
		} else if (type.equals(Date.class)) {
			return DATE_FACTORY.create(table, row, columnIndex, cell,
					(Date) value);
		} else if (type.equals(Boolean.class)) {
			return BOOLEAN_FACTORY.create(table, row, columnIndex, cell,
					(Boolean) value);
		} else {
			return COMPARABLE_FACTORY.create(table, row, columnIndex, cell,
					(Comparable<?>) value);
		}
	}

	private static Class<?> getType(TableWidgetImpl table, int columnIndex,
			Object value) {
		ColumnImpl column = table.columns.get(columnIndex);
		if (column.contentType != null)
			return column.contentType;
		return value.getClass();
	}
}
