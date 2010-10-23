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
package co.fxl.gui.table.masterdetail.impl;

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.template.SplitLayout;
import co.fxl.gui.filter.api.IFilterConstraints;
import co.fxl.gui.table.api.IColumn;
import co.fxl.gui.table.api.IRow;
import co.fxl.gui.table.api.ISelection.IMultiSelection;
import co.fxl.gui.table.api.ISelection.IMultiSelection.IChangeListener;
import co.fxl.gui.table.api.ISelection.ISingleSelection.ISelectionListener;
import co.fxl.gui.table.filter.api.IFilterTableWidget;
import co.fxl.gui.table.filter.api.IFilterTableWidget.IFilterListener;
import co.fxl.gui.table.filter.api.IFilterTableWidget.IRowModel;
import co.fxl.gui.table.filter.api.IFilterTableWidget.ITableFilter;
import co.fxl.gui.table.masterdetail.api.ITableView;

class TableViewImpl implements ITableView<Object>, IFilterListener<Object> {

	class RowModel implements IRowModel<Object> {

		private IRowModel<Object> rows;

		RowModel(IRowModel<Object> rows) {
			this.rows = rows;
		}

		@Override
		public IRow<Object> addRow() {
			return rows.addRow();
		}

		@Override
		public void onSuccess() {
			holdRefreshClick = false;
			rows.onSuccess();
		}

		@Override
		public IFilterTableWidget<Object> notifyServerCall() {
			holdRefreshClick = true;
			return rows.notifyServerCall();
		}

		@Override
		public void onFail() {
			rows.onFail();
		}
	}

	private static boolean CRUD_ALLOWED = false;
	private MasterDetailTableWidgetImpl widget;
	private IFilterTableWidget<Object> table;
	private ITableFilter filter;
	private NavigationGadget navigationView;
	private List<ILabel> labels = new LinkedList<ILabel>();
	@SuppressWarnings("unused")
	private IClickable<?> newButton;
	private IClickable<?> editButton;
	private IClickable<?> deleteButton;
	private IClickable<?> refreshButton;
	private IFilterListener<Object> filterListener;
	private IFilterConstraints constraints;
	private SplitLayout splitLayout;
	private List<Object> selection = new LinkedList<Object>();
	private String constrainedOn;
	private IClickListener removeListener;
	private boolean init = false;
	boolean isMaster = false;
	private boolean holdRefreshClick = false;
	private List<ILabel> titleLabels = new LinkedList<ILabel>();

	TableViewImpl(MasterDetailTableWidgetImpl widget, ILayout layout) {
		this.widget = widget;
		splitLayout = new SplitLayout(layout);
		splitLayout.mainPanel.visible(false);
	}

	TableViewImpl(MasterDetailTableWidgetImpl widget, DetailViewImpl detailView) {
		this.widget = widget;
		splitLayout = detailView.panel;
	}

	@Override
	public IRow<Object> addRow() {
		init();
		return table.addRow();
	}

	@SuppressWarnings("unchecked")
	private void init() {
		if (init)
			return;
		show();
		if (constrainedOn == null) {
			table = (IFilterTableWidget<Object>) splitLayout.mainPanel.add()
					.widget(IFilterTableWidget.class);
			((IFilterTableWidget<Object>) table).addFilterListener(this);
		} else {
			table = (IFilterTableWidget<Object>) splitLayout.mainPanel.add()
					.widget(IFilterTableWidget.class);
		}
		if (CRUD_ALLOWED) {
			newButton = table.addButton("New");
			deleteButton = table.addButton("Delete");
		}
		init = true;
	}

	void fillRowModel(IRowModel<Object> rows) {
		filterListener.onRefresh(rows, constraints);
	}

	private ITableFilter filter() {
		if (filter == null) {
			ILayout filterPanel = splitLayout.sidePanel.add().panel();
			filter = table.filterPanel(filterPanel);
			addSelection();
		}
		return filter;
	}

	private void addSelection() {
		if (navigationView != null) {
			IMultiSelection<Object> multipleSelection = table.selection()
					.multi();
			multipleSelection.addChangeListener(new IChangeListener<Object>() {
				@Override
				public void onChange(List<Object> selection) {
					TableViewImpl.this.selection = selection;
					for (ILabel label : labels) {
						label.clickable(!selection.isEmpty());
					}
					if (editButton != null)
						editButton.clickable(selection.size() == 1);
					if (deleteButton != null)
						deleteButton.clickable(!selection.isEmpty());
				}
			});
		} else {
			table.selection().single().addSelectionListener(
					new ISelectionListener<Object>() {

						@Override
						public void onSelection(Object selection) {
							widget.showDetails(selection);
						}
					});
		}
	}

	private NavigationGadget navigationView() {
		if (navigationView == null) {
			navigationView = new NavigationGadget(splitLayout.sidePanel.add()
					.panel());
		}
		return navigationView;
	}

	@Override
	public IClickable<?> addNavigationLink(String name) {
		ILabel label = navigationView().addHyperlink().text(name).clickable(
				false);
		labels.add(label);
		return label;
	}

	@Override
	public IColumn addColumn(String name, Class<?> type) {
		init();
		IColumn column = table.addColumn();
		column.name(name).sortable();
		if (!type.equals(Boolean.class))
			filter().filterable(column, type);
		return column;
	}

	@Override
	public IColumn addColumn(String name, String[] texts) {
		IColumn column = table.addColumn();
		column.name(name).sortable();
		filter().filterable(column, String.class, (Object[]) texts);
		return column;
	}

	@Override
	public IColumn addImageColumn(String name, String[] images) {
		IColumn column = table.addColumn();
		column.name(name).type(IImage.class).sortable();
		filter().filterable(column, String.class, (Object[]) images);
		return column;
	}

	@Override
	public ILabel addTitle(String title) {
		if (!isMaster)
			return null;
		init();
		if (!titleLabels.isEmpty()) {
			ILabel last = titleLabels.get(titleLabels.size() - 1);
			last.text(last.text() + ".");
			last.font().weight().plain().pixel(18).color().gray();
		}
		ILabel label = table.addTitle(title);
		label.font().pixel(18).color().mix().black().red();
		titleLabels.add(label);
		return label;
	}

	@Override
	public TableViewImpl filterListener(IFilterListener<Object> listener) {
		this.filterListener = listener;
		return this;
	}

	@Override
	public void onRefresh(IRowModel<Object> rows, IFilterConstraints constraints) {
		this.constraints = constraints;
		if (filterListener != null)
			filterListener.onRefresh(rows, constraints);
	}

	@Override
	public ITableView<Object> visible(boolean visible) {
		if (isMaster && !widget.views.isEmpty()) {
			if (navigationView != null) {
				editButton = table.addButton("Details");
				editButton.addClickListener(new IClickListener() {
					@Override
					public void onClick() {
						widget.showDetails(selection.get(0));
					}
				});
				editButton.clickable(false);
			}
		}
		if (constrainedOn == null) {
			refreshButton = table.addButton("Refresh");
			refreshButton.addClickListener(new IClickListener() {
				@Override
				public void onClick() {
					if (holdRefreshClick)
						return;
					IRowModel<Object> rows = ((IFilterTableWidget<Object>) table)
							.resetRowModel();
					fillRowModel(new RowModel(rows));
				}
			});
		} else if (isMaster) {
			IVerticalPanel vPanel = splitLayout.sidePanel.add().panel()
					.vertical();
			vPanel.add().label().text(
					"Constraint on " + constrainedOn + " is active.");
			IHorizontalPanel hPanel = vPanel.add().panel().horizontal().add()
					.panel().horizontal();
			hPanel.add().label().text("Click");
			hPanel.addSpace(6);
			hPanel.add().label().hyperlink().text("here").addClickListener(
					removeListener);
			hPanel.addSpace(6);
			hPanel.add().label().text("to remove.");
		}
		splitLayout.mainPanel.visible(true);
		table.visible(true);
		return this;
	}

	void show() {
		widget.cardPanel.show(splitLayout.panel);
	}

	@Override
	public List<Object> selection() {
		return selection;
	}

	@Override
	public ITableView<Object> nameColumn(int columnIndex) {
		throw new MethodNotImplementedException();
	}

	@Override
	public ITableView<Object> contrainedOn(String constraint,
			IClickListener removeListener) {
		constrainedOn = constraint;
		this.removeListener = removeListener;
		return this;
	}

	void reset() {
		splitLayout.reset();
	}
}
