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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.filter.api.IFilterConstraints;
import co.fxl.gui.mdt.api.IList;
import co.fxl.gui.mdt.api.IProperty.IAdapter;
import co.fxl.gui.table.api.IColumn;
import co.fxl.gui.table.api.IRow;
import co.fxl.gui.table.filter.api.IFilterTableWidget;
import co.fxl.gui.table.filter.api.IFilterTableWidget.IFilterListener;
import co.fxl.gui.table.filter.api.IFilterTableWidget.IRowModel;
import co.fxl.gui.table.filter.api.IFilterTableWidget.ITableFilter;
import co.fxl.gui.tree.api.ICallback;

class TableView extends ViewTemplate implements IFilterListener<Object> {

	private IFilterTableWidget<Object> table;
	private Map<PropertyImpl, IColumn> property2column = new HashMap<PropertyImpl, IColumn>();
	private List<IAdapter<Object, Object>> adapters = new LinkedList<IAdapter<Object, Object>>();
	private IList<Object> queryList;
	private IClickable<?> delete;
	private IClickable<?> detail;

	@SuppressWarnings("unchecked")
	TableView(final MasterDetailTableWidgetImpl widget, Object object) {
		super(widget);
		table = (IFilterTableWidget<Object>) splitLayout.mainPanel.add()
				.widget(IFilterTableWidget.class);
		table.addTitle(widget.title).font().pixel(18);
		addProperties();
		addNavigationLinks();
		final ITableFilter tableFilterList = table
				.filterPanel(splitLayout.sidePanel.add().panel());
		for (FilterImpl filter : widget.filterList.filters) {
			if (filter.property != null) {
				IColumn column = property2column.get(filter.property);
				tableFilterList.filterable(column, filter.property.type.type,
						filter.type.values);
			} else
				throw new MethodNotImplementedException();
		}
		if (widget.constraints != null)
			tableFilterList.constraints(widget.constraints);
		table.addFilterListener(this);
		table.selection().multi().addChangeListener(this);
		table.addButton("New").addClickListener(new IClickListener() {
			@Override
			public void onClick() {
				Object show = null;
				List<Object> result = table.selection().result();
				if (!result.isEmpty())
					show = result.get(result.size() - 1);
				widget.showDetailView(show).onNew();
			}
		});
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
		if (object != null) {
			table.selection().add(object);
		}
	}

	private void addProperties() {
		for (PropertyGroupImpl g : this.widget.propertyGroups) {
			for (PropertyImpl p : g.properties) {
				if (p.displayInTable) {
					adapters.add(p.adapter);
					IColumn column = table.addColumn().name(p.name).type(
							p.type.type);
					if (p.sortable)
						column.sortable();
					property2column.put(p, column);
				}
			}
		}
	}

	@Override
	public void onRefresh(final IRowModel<Object> rows,
			IFilterConstraints constraints) {
		widget.constraints = constraints;
		widget.source.queryList(constraints, new ICallback<IList<Object>>() {

			@Override
			public void onFail(Throwable throwable) {
				rows.onFail();
				throw new MethodNotImplementedException();
			}

			@Override
			public void onSuccess(IList<Object> queryList) {
				TableView.this.queryList = queryList;
				List<Object> list = queryList.jdkList();
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
		delete.clickable(!selection.isEmpty());
	}

	@Override
	boolean isRelevant(NavigationLinkImpl link) {
		return link.inTable;
	}
}
