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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.table.api.ISelection;
import co.fxl.gui.table.bulk.api.IBulkTableWidget.IRow;
import co.fxl.gui.table.bulk.api.IBulkTableWidget.ITableClickListener;

class SelectionImpl implements ISelection<Object> {

	class SingleSelectionImpl implements ISingleSelection<Object> {

		private List<ISelectionListener<Object>> listeners = new LinkedList<ISelectionListener<Object>>();

		@Override
		public ISingleSelection<Object> addSelectionListener(
				ISelectionListener<Object> selectionListener) {
			listeners.add(selectionListener);
			return this;
		}

		void update() {
			widget.grid.addTableListener(new ITableClickListener() {

				@Override
				public void onClick(int column, int row) {
					if (row == 0)
						return;
					row--;
					int convert2TableRow = widget.convert2TableRow(row);
					boolean alreadySelected = widget.rows
							.selected(convert2TableRow);
					clearSelection();
					if (alreadySelected) {
						for (ISelectionListener<Object> l : listeners) {
							l.onSelection(-1, null);
						}
						return;
					}
					widget.rows.selected(convert2TableRow, true);
					IRow r = widget.grid.row(row);
					r.highlight(true);
					widget.highlighted.add(r);
					notifyListeners(convert2TableRow,
							widget.rows.identifier(convert2TableRow));
				}
			});
		}

		private void notifyListeners(int convert2TableRow, Object o) {
			for (ISelectionListener<Object> l : listeners) {
				l.onSelection(convert2TableRow, o);
			}
		}

		ISelection<Object> add(Object object) {
			assert widget.preselected == null : "Only one row can be preselected";
			widget.preselectedIndex = -1;
			widget.preselected = object;
			return SelectionImpl.this;
		}

		ISelection<Object> add(int selectionIndex, Object object) {
			assert widget.preselected == null : "Only one row can be preselected";
			widget.preselectedIndex = selectionIndex;
			widget.preselected = object;
			return SelectionImpl.this;
		}
	}

	class MultiSelectionImpl implements IMultiSelection<Object> {

		private class SelectAllClickListener implements IClickListener {

			@Override
			public void onClick() {
				boolean changed = false;
				for (int i = 0; i < widget.rows.size(); i++) {
					if (!widget.rows.selected(i)) {
						widget.rows.selected(i, true);
						changed = true;
					}
				}
				if (changed) {
					widget.highlightAll();
					notifyListeners();
				}
			}
		}

		private class RemoveSelectionClickListener implements IClickListener {

			@Override
			public void onClick() {
				clearSelection();
				notifyListeners();
			}
		}

		private static final int PIXEL = 12;
		private List<IChangeListener<Object>> listeners = new LinkedList<IChangeListener<Object>>();
		private ILabel selectAll;
		private ILabel removeSelection;

		@Override
		public IMultiSelection<Object> addChangeListener(
				IMultiSelection.IChangeListener<Object> listener) {
			listeners.add(listener);
			return this;
		}

		private void notifyListeners() {
			List<Object> result = result();
			selectAll.clickable(widget.rows.size() != 0
					&& widget.rows.size() != result.size());
			removeSelection.clickable(!result.isEmpty());
			for (IChangeListener<Object> l : listeners) {
				l.onChange(result);
			}
		}

		void update() {
			widget.grid.addTableListener(new ITableClickListener() {

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
			}).ctrlPressed();
			widget.grid.addTableListener(new ITableClickListener() {

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
			});// .ctrlPressed();
			if (!widget.selectionIsSetup) {
				IHorizontalPanel p = widget.statusPanel().cell(0, 0).valign()
						.center().width(120).panel().horizontal().add().panel()
						.horizontal().spacing(5);
				p.add().label().text("Select").font().pixel(PIXEL);
				selectAll = p.add().label();
				selectAll.text("All").font().pixel(PIXEL);
				selectAll.hyperlink().addClickListener(
						new SelectAllClickListener());
				p.add().label().text("|").font().pixel(PIXEL).color().gray();
				removeSelection = p.add().label();
				removeSelection.text("None").font().pixel(PIXEL);
				removeSelection.hyperlink().addClickListener(
						new RemoveSelectionClickListener());
				removeSelection.clickable(!widget.rows.selectedIdentifiers()
						.isEmpty());
				widget.selectionIsSetup = true;
			}
		}

		ISelection<Object> add(Object object) {
			assert widget.preselected == null : "Selection has already been applied";
			widget.preselected = object;
			return SelectionImpl.this;
		}

		public ISelection<Object> add(int selectionIndex, Object object) {
			throw new MethodNotImplementedException();
		}
	}

	void clearSelection() {
		widget.rows.clearSelection();
		for (IRow r : widget.highlighted) {
			// int gridIndex = r.gridIndex();
			r.highlight(false);
			// widget.editable(gridIndex, false);
		}
	}

	private ScrollTableWidgetImpl widget;
	private SingleSelectionImpl single;
	private MultiSelectionImpl multi;

	SelectionImpl(ScrollTableWidgetImpl widget) {
		this.widget = widget;
	}

	@Override
	public ISingleSelection<Object> single() {
		if (single != null)
			return single;
		return single = new SingleSelectionImpl();
	}

	@Override
	public IMultiSelection<Object> multi() {
		if (multi != null)
			return multi;
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

	@Override
	public ISelection<Object> add(int selectionIndex, Object object) {
		if (single != null)
			return single.add(selectionIndex, object);
		else
			return multi.add(selectionIndex, object);
	}

	void update() {
		if (single != null)
			single.update();
		if (multi != null)
			multi.update();
	}

	@Override
	public Map<Integer, Object> indexedResult() {
		List<Integer> indices = widget.rows.selectedIndices();
		Map<Integer, Object> result = new HashMap<Integer, Object>();
		for (Integer i : indices) {
			result.put(i, widget.rows.identifier(i));
		}
		return result;
	}

	void notifySelection(int selectionIndex, Object selection2) {
		if (single != null) {
			clearSelection();
			widget.rows.selected(selectionIndex, selection2);
			single.notifyListeners(selectionIndex, selection2);
		} else
			multi.notifyListeners();
	}
}
