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
import co.fxl.gui.api.template.IFieldType;
import co.fxl.gui.api.template.ResizeListener;
import co.fxl.gui.filter.api.IFilterConstraints;
import co.fxl.gui.filter.api.IFilterWidget;
import co.fxl.gui.filter.api.IFilterWidget.IFilter;
import co.fxl.gui.filter.api.IFilterWidget.IFilterListener;
import co.fxl.gui.filter.api.IFilterWidget.IRelationFilter;
import co.fxl.gui.mdt.api.IDeletableList;
import co.fxl.gui.mdt.api.IProperty.IAdapter;
import co.fxl.gui.table.api.IColumn;
import co.fxl.gui.table.scroll.api.IRows;
import co.fxl.gui.table.scroll.api.IScrollTableWidget;
import co.fxl.gui.tree.impl.CallbackTemplate;

class TableView extends ViewTemplate implements IFilterListener,
		IResizeListener {

	private IScrollTableWidget<Object> table;
	private Map<PropertyImpl, IColumn> property2column = new HashMap<PropertyImpl, IColumn>();
	private List<IAdapter<Object, Object>> adapters = new LinkedList<IAdapter<Object, Object>>();
	private IDeletableList<Object> queryList;
	private IClickable<?> delete;
	// private IClickable<?> detail;
	private Map<String, IClickable<?>> buttons = new HashMap<String, IClickable<?>>();
	private Object selectionObject;
	private IFilterWidget filterWidget;

	TableView(final MasterDetailTableWidgetImpl widget, Object object,
			String configuration) {
		super(widget);
		selectionObject = object;
		if (widget.splitLayout != null)
			widget.splitLayout.showSplit(true);
		setUpFilter(configuration);
		onRefresh();
	}

	private void setUpFilter(String configuration) {
		if (widget.filterList.filters.isEmpty())
			return;
		widget.sidePanel.addSpace(widget.addSpacing);
		filterWidget = (IFilterWidget) widget.sidePanel.add().widget(
				IFilterWidget.class);
		filterWidget.showConfiguration(false);
		int index = 0;
		for (MDTFilterImpl filter : widget.filterList.filters) {
			if (!filter.inTable)
				continue;
			String config = widget.filterList.configuration2index.get(index++);
			if (config != null)
				filterWidget.addConfiguration(config);
			if (filter instanceof MDTRelationFilterImpl) {
				MDTRelationFilterImpl rfi = (MDTRelationFilterImpl) filter;
				@SuppressWarnings("unchecked")
				IRelationFilter<Object, Object> rf = (IRelationFilter<Object, Object>) filterWidget
						.addRelationFilter();
				rf.name(rfi.name);
				rf.adapter(rfi.adapter);
				rf.preset(rfi.preset);
			} else if (filter.property != null) {
				if (filter.property.displayInTable) {
					IFilter ftr = filterWidget.addFilter().name(
							filter.property.name);
					IFieldType f = ftr.type().type(filter.property.type.clazz);
					if (!filter.property.type.values.isEmpty())
						for (Object o : filter.property.type.values)
							f.addConstraint(o);
				}
			} else
				throw new MethodNotImplementedException(filter.name);
		}
		if (widget.constraints != null)
			filterWidget.constraints(widget.constraints);
		filterWidget.addSizeFilter();
		filterWidget.addFilterListener(this);
		filterWidget.visible(true);
		if (configuration != null)
			filterWidget.setConfiguration(configuration);
		// filterWidget.apply();
	}

	@SuppressWarnings({ "unchecked" })
	private void drawTable() {
		buttons.clear();
		table = (IScrollTableWidget<Object>) widget.mainPanel.add().widget(
				IScrollTableWidget.class);
		table.addTitle(widget.title).font().pixel(18);
		addProperties();
		table.selection().multi().addChangeListener(this);
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
					widget.showDetailView(show).onNew(type);
				}
			});
		}
		delete = table.addButton("Delete");
		delete.addClickListener(new IClickListener() {
			@Override
			public void onClick() {
				widget.mainPanel.display().showDialog().question()
						.question("Delete Entity?")
						.addQuestionListener(new IQuestionDialogListener() {

							@Override
							public void onYes() {
								List<Object> result = table.selection()
										.result();
								for (Object entity : result) {
									queryList.delete(entity);
								}
								onRefresh();
							}

							@Override
							public void onNo() {
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
		for (PropertyGroupImpl g : this.widget.propertyGroups) {
			if (config != null && !g.name.equals(config))
				continue;
			for (PropertyImpl p : g.properties) {
				if (!p.displayInTable)
					continue;
				adapters.add(p.adapter);
				IColumn column = table.addColumn().name(p.name)
						.type(p.type.clazz);
				if (p.sortable)
					column.sortable();
				property2column.put(p, column);
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

	private void updateCreatable() {
		if (widget.selection.size() > 1) {
			for (IClickable<?> c : buttons.values())
				c.clickable(false);
			return;
		}
		Object o = null;
		if (!widget.selection.isEmpty())
			o = widget.selection.get(0);
		String[] creatableTypes = widget.source.getCreatableTypes(o);
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
	public void onApply(IFilterConstraints constraints) {
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
						if (queryList.asList().isEmpty()) {
							// TODO ...
						}
						drawTable();
						long s0 = System.currentTimeMillis();
						TableView.this.queryList = queryList;
						final List<Object> list = queryList.asList();
						long s = System.currentTimeMillis();
						IRows<Object> rows = new IRows<Object>() {

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
						if (selectionObject != null) {
							table.selection().add(selectionObject);
						}
						ResizeListener.setup(widget.mainPanel.display(),
								TableView.this);
						onResize(-1, widget.mainPanel.display().height());
						updateCreatable();
						time = System.currentTimeMillis() - s0;
						out.println("TableView: created table in " + time
								+ "ms");
						table.visible(true);
					}
				});
	}

	private int lastHeight = -1;

	@Override
	public void onResize(int width, int height) {
		int offsetY = table.offsetY();
		// TODO ... un-hard-code
		if (offsetY == 0)
			offsetY = 139;
		int maxFromDisplay = height - offsetY - 96;
		if (lastHeight != height) {
			lastHeight = height;
			table.height(maxFromDisplay);
		}
	}

	@Override
	public void onUpdate(String value) {
		if (filterWidget != null)
			filterWidget.setConfiguration(value);
		onRefresh();
	}

	@Override
	public void onRefresh() {
		if (filterWidget != null)
			filterWidget.apply();
		else
			onApply(widget.constraints);
	}
}
