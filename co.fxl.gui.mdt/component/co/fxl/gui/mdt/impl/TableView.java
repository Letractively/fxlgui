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

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.template.ICallback;
import co.fxl.gui.filter.api.IFilterConstraints;
import co.fxl.gui.filter.api.IFilterWidget.IRelationFilter;
import co.fxl.gui.mdt.api.IDeletableList;
import co.fxl.gui.mdt.api.IProperty.IAdapter;
import co.fxl.gui.table.api.IColumn;
import co.fxl.gui.table.api.IRow;
import co.fxl.gui.table.filter.api.IFilterTableWidget;
import co.fxl.gui.table.filter.api.IFilterTableWidget.IFilterListener;
import co.fxl.gui.table.filter.api.IFilterTableWidget.IRowModel;
import co.fxl.gui.table.filter.api.IFilterTableWidget.ITableFilter;

class TableView extends ViewTemplate implements IFilterListener<Object> {

	private IFilterTableWidget<Object> table;
	private Map<PropertyImpl, IColumn> property2column = new HashMap<PropertyImpl, IColumn>();
	private List<IAdapter<Object, Object>> adapters = new LinkedList<IAdapter<Object, Object>>();
	private IDeletableList<Object> queryList;
	private IClickable<?> delete;
	private IClickable<?> detail;
	private Map<String, IClickable<?>> buttons = new HashMap<String, IClickable<?>>();
	private Object selectionObject;

	TableView(final MasterDetailTableWidgetImpl widget, Object object) {
		super(widget);
		selectionObject = object;
		if (widget.splitLayout != null)
			widget.splitLayout.showSplit(true);
		drawTable(widget);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void drawTable(final MasterDetailTableWidgetImpl widget) {
		table = (IFilterTableWidget<Object>) widget.mainPanel.add().widget(
				IFilterTableWidget.class);
		table.addTitle(widget.title).font().pixel(18);
		addProperties();
		addNavigationLinks();
		final ITableFilter tableFilterList = table.filterPanel(widget.sidePanel
				.add().panel());
		int index = 0;
		for (MDTFilterImpl filter : widget.filterList.filters) {
			if (!filter.inTable)
				continue;
			String config = widget.filterList.configuration2index.get(index++);
			if (config != null)
				tableFilterList.addConfiguration(config);
			if (filter instanceof MDTRelationFilterImpl) {
				MDTRelationFilterImpl rfi = (MDTRelationFilterImpl) filter;
				IRelationFilter<Object, Object> rf = tableFilterList
						.addRelationFilter();
				rf.name(rfi.name);
				rf.adapter(rfi.adapter);
				rf.preset(rfi.preset);
			} else if (filter.property != null) {
				if (filter.property.displayInTable) {
					IColumn column = property2column.get(filter.property);
					tableFilterList.filterable(column,
							filter.property.type.clazz, filter.type.values);
				}
			} else
				throw new MethodNotImplementedException(filter.name);
		}
		if (widget.constraints != null)
			tableFilterList.constraints(widget.constraints);
		table.addFilterListener(this);
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
				List<Object> result = table.selection().result();
				for (Object entity : result) {
					queryList.delete(entity);
				}
				tableFilterList.apply();
			}
		});
		table.addButton("Refresh").addClickListener(new IClickListener() {
			@Override
			public void onClick() {
				tableFilterList.apply();
			}
		});
		detail = table.addButton("Detail");
		detail.addClickListener(new IClickListener() {
			@Override
			public void onClick() {
				Object show = null;
				List<Object> result = table.selection().result();
				if (!result.isEmpty())
					show = result.get(result.size() - 1);
				widget.showDetailView(show);
			}
		});
		table.visible(true);
		if (selectionObject != null) {
			table.selection().add(selectionObject);
		}
	}

	private void addProperties() {
		for (PropertyGroupImpl g : this.widget.propertyGroups) {
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

	@Override
	public void onRefresh(final IRowModel<Object> rows,
			IFilterConstraints constraints) {
		widget.constraints = constraints;
		widget.source.queryList(constraints,
				new ICallback<IDeletableList<Object>>() {

					@Override
					public void onFail(Throwable throwable) {
						rows.onFail();
						throw new MethodNotImplementedException();
					}

					@Override
					public void onSuccess(IDeletableList<Object> queryList) {
						TableView.this.queryList = queryList;
						List<Object> list = queryList.asList();
						for (Object entity : list) {
							IRow<Object> row = rows.addRow();
							row.identifier(entity);
							for (IAdapter<Object, Object> adapter : adapters) {
								Object value = adapter.valueOf(entity);
								row.add((Comparable<?>) value);
							}
						}
						rows.onSuccess();
					}
				});
	}

	@Override
	public void onChange(List<Object> selection) {
		super.onChange(selection);
		boolean clickable = !selection.isEmpty();
		for (Object o : selection) {
			clickable &= queryList.isDeletable(o);
		}
		detail.clickable(selection.size() <= 1);
		delete.clickable(clickable);
		updateCreatable();
	}

	private void updateCreatable() {
		if (selection.size() > 1) {
			for (IClickable<?> c : buttons.values())
				c.clickable(false);
			return;
		}
		Object o = null;
		if (!selection.isEmpty())
			o = selection.get(0);
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
}
