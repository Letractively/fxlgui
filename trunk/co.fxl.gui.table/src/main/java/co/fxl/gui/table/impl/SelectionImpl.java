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

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.table.api.ISelection;

class SelectionImpl implements ISelection<Object> {

	class SingleSelection implements ISingleSelection<Object>, RowListener {

		private List<ISelectionListener<Object>> listeners = new LinkedList<ISelectionListener<Object>>();
		private RowImpl selectedRow = null;

		@Override
		public ISingleSelection<Object> addSelectionListener(
				ISelectionListener<Object> selectionListener) {
			listeners.add(selectionListener);
			return this;
		}

		@Override
		public void notifyClick(RowImpl row) {
			if (selectedRow != null) {
				selectedRow.selected(false);
				if (selectedRow == row) {
					for (ISelectionListener<Object> l : listeners) {
						l.onSelection(-1, null);
					}
					selection.clear();
					selectedRow = null;
					return;
				}
			}
			selectedRow = row;
			selectedRow.selected(true);
			selection.clear();
			selection.add(row.content.identifier);
			for (ISelectionListener<Object> l : listeners) {
				l.onSelection(row.rowIndex, row.content.identifier);
			}
		}

		@Override
		public void notifyCtrlClick(RowImpl rowImpl) {
			notifyClick(rowImpl);
		}

		@Override
		public void notifyShiftClick(RowImpl rowImpl) {
			notifyClick(rowImpl);
		}

		@Override
		public void decorate(IClickable<?> c) {
		}

		@Override
		public void update() {
		}

		@Override
		public void visible() {
		}

		@Override
		public void clear() {
		}
	}

	class MultiSelection implements IMultiSelection<Object>, RowListener {

		private static final int PIXEL = 12;

		private class SelectAllClickListener implements IClickListener {

			@Override
			public void onClick() {
				boolean changed = false;
				for (RowImpl row : widget.rows) {
					if (row.content.visible
							&& !selection.contains(row.content.identifier)) {
						selection.add(row.content.identifier);
						row.selected(true);
						changed = true;
					}
				}
				if (changed)
					notifyChange();
			}
		}

		private class RemoveSelectionClickListener implements IClickListener {

			@Override
			public void onClick() {
				clear();
			}
		}

		private List<IChangeListener<Object>> listeners = new LinkedList<IChangeListener<Object>>();
		private ILabel selectAll;
		private ILabel removeSelection;
		private RowImpl lastRow = null;

		MultiSelection(TableWidgetImpl widget) {
		}

		@Override
		public IMultiSelection<Object> addChangeListener(
				IChangeListener<Object> listener) {
			listeners.add(listener);
			return this;
		}

		@Override
		public void notifyClick(RowImpl row) {
			clear();
			notifyCtrlClick(row);
		}

		@Override
		public void notifyCtrlClick(RowImpl row) {
			Object identifier = row.content.identifier;
			boolean removed = selection.remove(identifier);
			if (!removed) {
				selection.add(identifier);
				lastRow = row;
			} else {
				lastRow = null;
			}
			row.selected(!removed);
			notifyChange();
		}

		@Override
		public void notifyShiftClick(RowImpl row) {
			if (lastRow == null) {
				notifyCtrlClick(row);
			} else {
				int min = Math.min(row.rowIndex, lastRow.rowIndex);
				int max = Math.max(row.rowIndex, lastRow.rowIndex);
				for (int i = min; i <= max; i++) {
					RowImpl r = widget.rows.get(i - 1);
					if (!selection.contains(r.content.identifier)) {
						r.selected(true);
						selection.add(r.content.identifier);
					}
				}
				lastRow = row;
				notifyChange();
			}
		}

		private void notifyChange() {
			for (IChangeListener<Object> l : listeners) {
				l.onChange(selection);
			}
			int size = selection.size();
			int all = widget.rows.size();
			selectAll.clickable(size != all);
			removeSelection.clickable(!selection.isEmpty());
		}

		@Override
		public void decorate(IClickable<?> c) {
		}

		@Override
		public void visible() {
			IGridPanel selectionPanel = widget.selectionPanel();
			IHorizontalPanel p = selectionPanel.cell(0, 0).panel().horizontal()
					.add().panel().horizontal().spacing(5);
			p.add().label().text("Select").font().pixel(PIXEL);
			selectAll = p.add().label();
			selectAll.text("All").font().pixel(PIXEL);
			selectAll.hyperlink()
					.addClickListener(new SelectAllClickListener());
			p.add().label().text("|").font().pixel(PIXEL).color().gray();
			removeSelection = p.add().label();
			removeSelection.text("None").font().pixel(PIXEL);
			removeSelection.hyperlink().addClickListener(
					new RemoveSelectionClickListener());
			removeSelection.clickable(false);
		}

		@Override
		public void update() {
			notifyChange();
		}

		@Override
		public void clear() {
			boolean changed = false;
			for (RowImpl row : widget.rows) {
				if (selection.contains(row.content.identifier)) {
					selection.remove(row.content.identifier);
					row.selected(false);
					changed = true;
				}
			}
			if (changed)
				notifyChange();
		}
	}

	private TableWidgetImpl widget;
	List<Object> selection = new LinkedList<Object>();

	SelectionImpl(TableWidgetImpl widget) {
		this.widget = widget;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ISelection.IMultiSelection<Object> multi() {
		if (widget.rowListener == null) {
			widget.rowListener = new MultiSelection(widget);
		}
		return (ISelection.IMultiSelection<Object>) widget.rowListener;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ISelection.ISingleSelection<Object> single() {
		if (widget.rowListener == null) {
			widget.rowListener = new SingleSelection();
		}
		return (ISelection.ISingleSelection<Object>) widget.rowListener;
	}

	@Override
	public List<Object> result() {
		return selection;
	}

	void add(RowImpl row, Object identifier) {
		selection.add(identifier);
		widget.rowListener.update();
	}

	@Override
	public ISelection<Object> add(Object object) {
		RowImpl rowImpl = widget.object2row.get(object);
		if (rowImpl != null)
			widget.rowListener.notifyClick(rowImpl);
		return this;
	}

	@Override
	public Map<Integer, Object> indexedResult() {
		throw new MethodNotImplementedException();
	}

	@Override
	public ISelection<Object> add(int selectionIndex, Object selection) {
		throw new MethodNotImplementedException();
	}
}
