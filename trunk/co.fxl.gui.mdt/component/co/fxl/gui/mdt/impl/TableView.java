/**
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
 *
 * Copyright (c) 2010 Dangelmayr IT GmbH. All rights reserved.
 */
package co.fxl.gui.mdt.impl;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IDialog.IQuestionDialog.IQuestionDialogListener;
import co.fxl.gui.api.IDisplay.IResizeListener;
import co.fxl.gui.api.template.CallbackTemplate;
import co.fxl.gui.api.template.ResizeListener;
import co.fxl.gui.filter.api.IFilterConstraints;
import co.fxl.gui.filter.api.IFilterWidget.IFilterListener;
import co.fxl.gui.mdt.api.IDeletableList;
import co.fxl.gui.mdt.api.IProperty.IAdapter;
import co.fxl.gui.table.api.IColumn;
import co.fxl.gui.table.api.ISelection.ISingleSelection.ISelectionListener;
import co.fxl.gui.table.bulk.api.IBulkTableWidget.ITableClickListener;
import co.fxl.gui.table.scroll.api.IRows;
import co.fxl.gui.table.scroll.api.IScrollTableColumn;
import co.fxl.gui.table.scroll.api.IScrollTableWidget;
import co.fxl.gui.table.scroll.api.IScrollTableWidget.ISortListener;

class TableView extends ViewTemplate implements IResizeListener, ISortListener,
		ISelectionListener<Object> {

	private IScrollTableWidget<Object> table;
	private Map<PropertyImpl, IColumn<Object>> property2column = new HashMap<PropertyImpl, IColumn<Object>>();
	private List<IAdapter<Object, Object>> adapters = new LinkedList<IAdapter<Object, Object>>();
	private IDeletableList<Object> queryList;
	private IClickable<?> delete;
	// private IClickable<?> detail;
	private Map<String, IClickable<?>> buttons = new HashMap<String, IClickable<?>>();
	private Object selectionObject;

	TableView(final MasterDetailTableWidgetImpl widget, Object object,
			String configuration) {
		super(widget);
		selectionObject = object;
		if (widget.splitLayout != null)
			widget.splitLayout.showSplit(true);
		setUpFilter(configuration);
		onDelete();
	}

	@SuppressWarnings({ "unchecked" })
	private void drawTable() {
		buttons.clear();
		table = (IScrollTableWidget<Object>) widget.mainPanel.add().widget(
				IScrollTableWidget.class);
		table.addTitle(widget.title).font().pixel(18);
		addProperties();
		if (widget.allowMultiSelection)
			table.selection().multi().addChangeListener(this);
		else
			table.selection().single().addSelectionListener(this);
		if (widget.allowCreate) {
			if (widget.creatableTypes.isEmpty())
				widget.creatableTypes.add(null);
			for (final String type : widget.creatableTypes) {
				IClickable<?> button = table.addButton(type == null ? "New"
						: "New " + type);
				buttons.put(type, button);
				button.addClickListener(new IClickListener() {
					@Override
					public void onClick() {
						widget.r2.checked(true);
						Object show = null;
						List<Object> result = table.selection().result();
						if (!result.isEmpty())
							show = result.get(result.size() - 1);
						widget.showDetailView(show, true, type);
					}
				});
			}
		}
		delete = table.addButton("Delete");
		delete.addClickListener(new IClickListener() {
			@Override
			public void onClick() {
				final List<Object> result = table.selection().result();
				String msg = result.size() == 1 ? "Delete Entity?"
						: "Delete Entities?";
				widget.mainPanel.display().showDialog().question()
						.question(msg).title("Warning")
						.addQuestionListener(new IQuestionDialogListener() {

							@Override
							public void onYes() {
								for (Object entity : result) {
									queryList
											.delete(entity,
													new CallbackTemplate<IDeletableList<Object>>() {

														@Override
														public void onSuccess(
																IDeletableList<Object> result) {
															onDelete();
														}
													});
								}
							}

							@Override
							public void onNo() {
							}

							@Override
							public void onCancel() {
								throw new MethodNotImplementedException();
							}
						});
			}
		});
		delete.clickable(false);
		// table.addButton("Refresh").addClickListener(new IClickListener() {
		// @Override
		// public void onClick() {
		// filterWidget.apply();
		// }
		// });
		// detail = table.addButton("Details");
		// detail.addClickListener(new IClickListener() {
		// @Override
		// public void onClick() {
		// Object show = null;
		// List<Object> result = table.selection().result();
		// if (!result.isEmpty())
		// show = result.get(result.size() - 1);
		// widget.r2.checked(true);
		// widget.showDetailView(show);
		// }
		// });
	}

	private void addProperties() {
		adapters.clear();
		property2column.clear();
		String config = getConfig();
		int index = 0;
		for (PropertyGroupImpl g : this.widget.propertyGroups) {
			if (config != null && !g.name.equals(config))
				continue;
			for (PropertyImpl p : g.properties) {
				if (!p.displayInTable)
					continue;
				adapters.add(p.adapter);
				IScrollTableColumn<Object> column = table.addColumn();
				column.name(p.name).type(p.type);
				if (p.sortable) {
					column.sortable();
					if (widget.constraints != null
							&& widget.constraints.sortOrder() != null
							&& widget.constraints.sortOrder().equals(p.name))
						column.tagSortOrder(widget.constraints.sortDirection());
				}
				property2column.put(p, column);
				// if (index == 0) {
				// column.addClickListener(new IScrollTableListener<Object>() {
				//
				// @Override
				// public void onClick(Object identifier) {
				// widget.r2.checked(true);
				// widget.showDetailView(identifier);
				// }
				// });
				// }
				index++;
			}
		}
	}

	private String getConfig() {
		if (widget.constraints == null)
			return null;
		String configuration = widget.constraints.configuration();
		if (configuration.equals(IFilterConstraints.COMMON))
			return null;
		return configuration;
	}

	@Override
	public void onChange(List<Object> selection) {
		super.onChange(selection);
		boolean clickable = !selection.isEmpty();
		for (Object o : selection) {
			clickable &= queryList.isDeletable(o);
		}
		// detail.clickable(selection.size() == 1);
		delete.clickable(clickable);
		updateCreatable();
	}

	@Override
	public void onSelection(Object selection) {
		List<Object> s = new LinkedList<Object>();
		s.add(selection);
		onChange(s);
	}

	private void updateCreatable() {
		if (widget.selection.size() > 1) {
			for (IClickable<?> c : buttons.values())
				c.clickable(false);
			return;
		}
		Object o = null;
		if (!widget.selection.isEmpty())
			o = widget.selection.get(0);
		String[] creatableTypes = o == null ? widget.source
				.getDefaultCreatableTypes() : widget.source
				.getCreatableTypes(o);
		List<String> ctypes = creatableTypes != null ? Arrays
				.asList(creatableTypes) : null;
		for (String c : buttons.keySet()) {
			boolean b = ctypes == null || ctypes.contains(c);
			buttons.get(c).clickable(b);
		}
	}

	@Override
	boolean isRelevant(NavigationLinkImpl link) {
		return link.inTable;
	}

	@Override
	public void onApply(final IFilterConstraints constraints) {
		widget.mainPanel.clear();
		widget.constraints = constraints;
		Iterator<MDTFilterImpl> it = widget.filterList.filters.iterator();
		while (it.hasNext()) {
			MDTFilterImpl filter = it.next();
			if (filter instanceof MDTRelationFilterImpl) {
				if (!constraints.isRelationConstrained(filter.name)) {
					it.remove();
				}
			}
		}
		widget.source.queryList(constraints,
				new CallbackTemplate<IDeletableList<Object>>() {

					@Override
					public void onSuccess(final IDeletableList<Object> queryList) {
						long s = System.currentTimeMillis();
						drawTable();
						TableView.this.queryList = queryList;
						final List<Object> list = queryList.asList();
						final IRows<Object> rows = new IRows<Object>() {

							@Override
							public Object[] row(int i) {
								return queryList.tableValues(list.get(i));
							}

							@Override
							public Object identifier(int i) {
								return list.get(i);
							}

							@Override
							public int size() {
								return list.size();
							}
						};
						table.rows(rows);
						PrintStream out = System.out;
						long time = System.currentTimeMillis() - s;
						if (time > 500)
							out = System.err;
						out.println("TableView: added " + list.size()
								+ " rows in " + time + "ms");
						ResizeListener.setup(widget.mainPanel.display(),
								TableView.this);
						onResize(-1, widget.mainPanel.display().height());
						updateCreatable();
						time = System.currentTimeMillis() - s;
						ITableClickListener showClickListener = new ITableClickListener() {

							@Override
							public void onClick(int column, int row) {
								if (row == 0)
									return;
								row--;
								Object show = rows.identifier(row);
								widget.r2.checked(true);
								widget.showDetailView(show);
							}
						};
						table.addTableClickListener(showClickListener).doubleClick();
						table.addFilterListener(new IFilterListener() {

							@Override
							public void onApply(IFilterConstraints constraints) {
								TableView.this.onApply(constraints);
							}
						});
						table.addTooltip("Double click to switch views.");
						table.sortListener(TableView.this);
						table.constraints(constraints);
						if (selectionObject != null) {
							table.selection().add(selectionObject);
						}
						table.visible(true);
						out.println("TableView: created table in " + time
								+ "ms");
						onChange(table.selection().result());
					}
				});
	}

	@Override
	public void onSort(String columnName, boolean up) {
		widget.constraints.sortOrder(columnName);
		widget.constraints.sortDirection(up);
		filterWidget.constraints(widget.constraints());
		onApply(widget.constraints);
	}

	@Override
	public void onResize(int width, int height) {
		int offsetY = table.offsetY();
		// TODO ... un-hard-code
		if (offsetY == 0)
			offsetY = 139;
		int maxFromDisplay = height - offsetY - 70;
		if (maxFromDisplay > 10)
			table.height(maxFromDisplay);
	}

	@Override
	public void onUpdate(String value) {
		if (filterWidget != null)
			filterWidget.setConfiguration(value);
		onDelete();
	}

	@Override
	public void onDelete() {
		if (filterWidget != null)
			filterWidget.apply();
		else
			onApply(widget.constraints);
	}
}
