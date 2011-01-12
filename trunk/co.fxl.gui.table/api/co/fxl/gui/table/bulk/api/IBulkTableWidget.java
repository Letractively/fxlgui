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

import co.fxl.gui.api.IClickable.IKey;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IUpdateable.IUpdateListener;

public interface IBulkTableWidget {

	public interface IMouseWheelListener {

		void onDown(int turns);

		void onUp(int turns);
	}

	public interface ITableListener {

		void onClick(int column, int row);
	}

	public interface IColumn {

		IColumn title(String title);

		IColumn width(double width);

		IColumn width(int width);
	}

	public interface IRow {

		IRow highlight(boolean highlight);

		boolean isHighlight();
	}

	public interface ICell {

		ICell text(String text);

		ICell checkBox(Boolean value);

		ICell updateListener(IUpdateListener<Boolean> updateListener);
	}

	ICell cell(int column, int row);

	IRow row(int row);

	IColumn column(int column);

	IKey<?> addTableListener(ITableListener l);

	int height();

	IBulkTableWidget height(int height);

	IBulkTableWidget addMouseWheelListener(IMouseWheelListener l);

	IBulkTableWidget visible(boolean visible);

	void remove();

	IElement<?> element();
}
