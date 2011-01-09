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
package co.fxl.gui.table.scroll.impl;

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.table.api.ISelection;
import co.fxl.gui.table.bulk.api.IBulkTableWidget.IRow;
import co.fxl.gui.table.bulk.api.IBulkTableWidget.ITableListener;

class SelectionImpl implements ISelection<Object> {

	class SingleSelectionImpl implements ISingleSelection<Object> {

		@Override
		public ISingleSelection<Object> addSelectionListener(
				ISelectionListener<Object> selectionListener) {
			throw new MethodNotImplementedException();
		}

		void update() {
			widget.grid.addTableListener(new ITableListener() {

				@Override
				public void onClick(int column, int row) {
					if (row == 0)
						return;
					row--;
					clearSelection();
					widget.rows.selected(widget.convert2TableRow(row), true);
					IRow r = widget.grid.row(row);
					r.highlight(true);
					widget.highlighted.add(r);
				}
			});
		}

		ISelection<Object> add(Object object) {
			throw new MethodNotImplementedException();
		}
	}

	class MultiSelectionImpl implements IMultiSelection<Object> {

		// TODO select all | none

		private List<IChangeListener<Object>> listeners = new LinkedList<IChangeListener<Object>>();

		@Override
		public IMultiSelection<Object> addChangeListener(
				IMultiSelection.IChangeListener<Object> listener) {
			listeners.add(listener);
			return this;
		}

		private void notifyListeners() {
			for (IChangeListener<Object> l : listeners)
				l.onChange(result());
		}

		void update() {
			widget.grid.addTableListener(new ITableListener() {

				@Override
				public void onClick(int column, int row) {
					if (row == 0)
						return;
					row--;
					clearSelection();
					widget.rows.selected(widget.convert2TableRow(row), true);
					IRow r = widget.grid.row(row);
					r.highlight(true);
					widget.highlighted.add(r);
					notifyListeners();
				}
			});
			widget.grid.addTableListener(new ITableListener() {

				@Override
				public void onClick(int column, int row) {
					if (row == 0)
						return;
					row--;
					int tableRow = widget.convert2TableRow(row);
					boolean selected = !widget.rows.selected(tableRow);
					widget.rows.selected(tableRow, selected);
					IRow r = widget.grid.row(row);
					r.highlight(selected);
					if (selected) {
						widget.highlighted.add(r);
					} else
						widget.highlighted.remove(r);
					notifyListeners();
				}
			}).ctrlPressed();
		}

		ISelection<Object> add(Object object) {
			throw new MethodNotImplementedException();
		}
	}

	void clearSelection() {
		widget.rows.clearSelection();
		for (IRow r : widget.highlighted)
			r.highlight(false);
	}

	private ScrollTableWidgetImpl widget;
	private SingleSelectionImpl single;
	private MultiSelectionImpl multi;

	SelectionImpl(ScrollTableWidgetImpl widget) {
		this.widget = widget;
	}

	@Override
	public ISingleSelection<Object> single() {
		return single = new SingleSelectionImpl();
	}

	@Override
	public IMultiSelection<Object> multi() {
		return multi = new MultiSelectionImpl();
	}

	@Override
	public List<Object> result() {
		return widget.rows.selectedIdentifiers();
	}

	@Override
	public ISelection<Object> add(Object object) {
		if (single != null)
			return single.add(object);
		else
			return multi.add(object);
	}

	void update() {
		if (single != null)
			single.update();
		if (multi != null)
			multi.update();
	}
}
