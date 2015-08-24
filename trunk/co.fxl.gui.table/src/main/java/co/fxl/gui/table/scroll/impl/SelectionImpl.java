/**
 * Copyright (c) 2010-2015 Dangelmayr IT GmbH. All rights reserved.
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

import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IClickable.IKey;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IPoint;
import co.fxl.gui.style.impl.Style;
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
				public void onClick(int column, int row, IPoint p) {
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
			assert widget.preselectedList.isEmpty() : "Only one row can be preselected";
			// widget.preselectedIndex = -1;
			if (!widget.preselectedList.contains(object))
				widget.preselectedList.add(object);
			return SelectionImpl.this;
		}

		ISelection<Object> add(int selectionIndex, Object object) {
			assert widget.preselectedList.isEmpty() : "Only one row can be preselected";
			// widget.preselectedIndex = selectionIndex;
			if (!widget.preselectedList.contains(object))
				widget.preselectedList.add(object);
			return SelectionImpl.this;
		}

		public void clear() {
			widget.rows.clearSelection();
			notifyListeners(-1, null);
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

		private static final boolean USE_CTRL_CLICK_FOR_MULTI_SELECTION = true;
		private static final boolean ALLOW_RANGE_SELECTION = true;
		private List<IChangeListener<Object>> listeners = new LinkedList<IChangeListener<Object>>();
		private IClickable<?> selectAll;
		private IClickable<?> removeSelection;

		@Override
		public IMultiSelection<Object> addChangeListener(
				IMultiSelection.IChangeListener<Object> listener) {
			listeners.add(listener);
			return this;
		}

		private void notifyListeners() {
			List<Object> result = updateButtons();
			for (IChangeListener<Object> l : listeners) {
				l.onChange(result);
			}
		}

		private List<Object> updateButtons() {
			List<Object> result = result();
			int size = widget.rows.size();
			int size2 = result.size();
			if (selectAll != null)
				selectAll.clickable(size != 0 && size != size2);
			if (removeSelection != null)
				removeSelection.clickable(!result.isEmpty());
			return result;
		}

		private Object lastSelection;

		void update() {
			IKey<?> exclusiveSelection = widget.grid
					.addTableListener(new ITableClickListener() {

						@Override
						public void onClick(int column, int row, IPoint p) {
							if (row == 0)
								return;
							row--;
							clearSelection();
							int lastIndex = widget.convert2TableRow(row);
							lastSelection = widget.rows.identifier(lastIndex);
							widget.rows.selected(lastIndex, true);
							IRow r = widget.grid.row(row);
							r.highlight(true);
							widget.highlighted.add(r);
							notifyListeners();
						}
					});
			IKey<?> incrementalSelection = widget.grid
					.addTableListener(new ITableClickListener() {

						@Override
						public void onClick(int column, int row, IPoint p) {
							if (row == 0)
								return;
							row--;
							int lastIndex = widget.convert2TableRow(row);
							lastSelection = widget.rows.identifier(lastIndex);
							int tableRow = lastIndex;
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
			IKey<?> rangeSelection = ALLOW_RANGE_SELECTION ? widget.grid
					.addTableListener(new ITableClickListener() {

						@Override
						public void onClick(int column, int row, IPoint p) {
							if (row == 0)
								return;
							row--;
							int tableRow = widget.convert2TableRow(row);
							System.out.println(lastSelection);
							int r0 = lastSelection == null ? 0 : widget.rows
									.find(lastSelection);
							System.out.println(r0);
							// if (r0 == 0
							// && widget.rows.selectedIdentifiers().size() >= 1)
							// {
							// boolean ok = false;
							// for (int i : widget.rows.selectedIndices()) {
							// if (i == 0) {
							// ok = true;
							// }
							// }
							// if (!ok) {
							// r0 = widget.rows.selectedIndices().get(
							// widget.rows.selectedIndices()
							// .size() - 1);
							// lastIndex = r0;
							// }
							// }
							int r1 = tableRow;
							if (r0 > r1) {
								int tmp = r0;
								r0 = r1;
								r1 = tmp;
							}
							for (int i = r0; i <= r1; i++) {
								widget.rows.selected(i, true);
								int visibleRow = widget.convert2GridRow(i);
								int rowCount = widget.grid.rowCount();
								if (visibleRow >= 0
										&& visibleRow < rowCount - 1) {
									IRow r = widget.grid.row(visibleRow);
									if (!r.isHighlight()) {
										r.highlight(true);
										widget.highlighted.add(r);
									}
								}
							}
							notifyListeners();
						}
					}) : null;// .ctrlPressed();
			decorateKeys(exclusiveSelection, incrementalSelection,
					rangeSelection);
			setUp();
		}

		void decorateKeys(IKey<?> exclusiveSelection,
				IKey<?> incrementalSelection, IKey<?> rangeSelection) {
			if (USE_CTRL_CLICK_FOR_MULTI_SELECTION) {
				incrementalSelection.ctrlPressed();
			} else {
				exclusiveSelection.ctrlPressed();
			}
			if (rangeSelection != null)
				rangeSelection.shiftPressed();
		}

		private void setUp() {
			if (!widget.selectionIsSetup) {
				IHorizontalPanel p = widget.statusPanel().cell(0, 0).valign()
						.center().// width(120).
						panel().horizontal().add().panel().horizontal();
				Style.instance().table().selectAllNoneBackground(p);
				Style.instance().table().statusHeader(p, "Select");
				selectAll = Style.instance().table().selectLink(p, true);
				// Style.instance().table().selectAllNone(selectAll, false);
				selectAll.addClickListener(new SelectAllClickListener());
				if (Style.instance().table().separateSelectAllNone())
					p.add().label().text("|").font().weight().bold()
							.pixel(PIXEL).color().gray();
				removeSelection = Style.instance().table().selectLink(p, false);
				removeSelection
						.addClickListener(new RemoveSelectionClickListener());
				removeSelection.clickable(!widget.rows.selectedIdentifiers()
						.isEmpty());
				widget.selectionIsSetup = true;
			}
		}

		ISelection<Object> add(Object object) {
			widget.preselectedList.add(object);
			lastSelection = object;
			return SelectionImpl.this;
		}

		public ISelection<Object> add(int selectionIndex, Object object) {
			throw new UnsupportedOperationException();
		}

		public void clear() {
			clearSelection();
			notifyListeners();
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

	static final int PIXEL = 10;
	private ScrollTableWidgetImpl widget;
	private SingleSelectionImpl single;
	private MultiSelectionImpl multi;

	SelectionImpl(ScrollTableWidgetImpl widget) {
		this.widget = widget;
	}

	@Override
	public ISingleSelection<Object> single() {
		assert multi == null;
		if (single != null)
			return single;
		return single = new SingleSelectionImpl();
	}

	@Override
	public IMultiSelection<Object> multi() {
		assert single == null;
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

	public void updateButtons() {
		if (single == null) {
			multi.updateButtons();
		}
	}

	@Override
	public void clear() {
		if (single != null)
			single.clear();
		else
			multi.clear();
	}
}
