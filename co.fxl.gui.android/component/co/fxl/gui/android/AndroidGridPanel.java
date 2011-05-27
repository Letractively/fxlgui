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
package co.fxl.gui.android;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import co.fxl.gui.api.IGridPanel;

class AndroidGridPanel extends AndroidPanel<TableLayout, IGridPanel> implements
		IGridPanel, Parent {

	private Activity activity;
	private Map<Integer, TableRow> rows = new HashMap<Integer, TableRow>();
	private AndroidDisplay androidDisplay;

	AndroidGridPanel(AndroidContainer container) {
		super(container);
		androidDisplay = container.parent.androidDisplay();
		view = new TableLayout(activity);
		container.parent.add(view);
	}

	@Override
	public IKey<IGridPanel> addGridClickListener(IGridClickListener listener) {
		throw new MethodNotImplementedException();
	}

	@Override
	public IGridCell cell(int columnIndex, int rowIndex) {
		TableRow row = getRow(rowIndex);
		return new AndroidGridCell(androidDisplay, row, columnIndex);
	}

	private TableRow getRow(int rowIndex) {
		TableRow row = rows.get(rowIndex);
		if (row == null) {
			row = new TableRow(activity);
			view.addView(row, rowIndex);
			rows.put(rowIndex, row);
		}
		return row;
	}

	@Override
	public int columns() {
		throw new MethodNotImplementedException();
	}

	@Override
	public IGridPanel indent(int pixel) {
		throw new MethodNotImplementedException();
	}

	@Override
	public int rows() {
		throw new MethodNotImplementedException();
	}

	@Override
	public IGridPanel spacing(int pixel) {
		throw new MethodNotImplementedException();
	}

	@Override
	public void add(View view) {
		throw new MethodNotImplementedException();
	}

	@Override
	public AndroidDisplay androidDisplay() {
		throw new MethodNotImplementedException();
	}

	@Override
	public View element() {
		throw new MethodNotImplementedException();
	}

	@Override
	public void remove(View view) {
		throw new MethodNotImplementedException();
	}

	@Override
	public IGridPanel resize(int columns, int rows) {
		throw new MethodNotImplementedException();
	}

	@Override
	public IBorder cellBorder() {
		throw new MethodNotImplementedException();
	}

	@Override
	public IGridColumn column(int column) {
		throw new MethodNotImplementedException();
	}

	@Override
	public IGridRow row(int row) {
		throw new MethodNotImplementedException();
	}
}
