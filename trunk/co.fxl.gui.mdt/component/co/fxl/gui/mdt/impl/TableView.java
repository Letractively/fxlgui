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
import co.fxl.gui.api.IDialog;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IDisplay.IResizeListener;
import co.fxl.gui.api.template.CallbackTemplate;
import co.fxl.gui.api.template.ICallback;
import co.fxl.gui.api.template.ResizeListener;
import co.fxl.gui.api.template.WidgetTitle;
import co.fxl.gui.filter.api.IFilterConstraints;
import co.fxl.gui.filter.api.IFilterWidget.IFilterListener;
import co.fxl.gui.mdt.api.IDeletableList;
import co.fxl.gui.mdt.api.IProperty.IAdapter;
import co.fxl.gui.table.api.ISelection;
import co.fxl.gui.table.api.ISelection.ISingleSelection.ISelectionListener;
import co.fxl.gui.table.scroll.api.IRows;
import co.fxl.gui.table.scroll.api.IScrollTableColumn;
import co.fxl.gui.table.scroll.api.IScrollTableWidget;
import co.fxl.gui.table.scroll.api.IScrollTableWidget.IScrollTableClickListener;
import co.fxl.gui.table.scroll.api.IScrollTableWidget.ISortListener;
import co.fxl.gui.tree.impl.TreeWidgetImpl;

class TableView extends ViewTemplate implements IResizeListener, ISortListener,
		ISelectionListener<Object> {

	IScrollTableWidget<Object> table;
	Map<PropertyImpl, IScrollTableColumn<Object>> property2column = new HashMap<PropertyImpl, IScrollTableColumn<Object>>();
	private List<IAdapter<Object, Object>> adapters = new LinkedList<IAdapter<Object, Object>>();
	private IClickable<?> delete;
	// private IClickable<?> detail;
	private Map<String, IClickable<?>> buttons = new HashMap<String, IClickable<?>>();
	// private Object selectionObject;
	private boolean painting = false;

	TableView(final MasterDetailTableWidgetImpl widget, Object object) {
		super(widget);
		// selectionObject = object;
		if (widget.splitLayout != null)
			widget.splitLayout.showSplit(true);
	}

	@SuppressWarnings({ "unchecked" })
	private void drawTable() {
		buttons.clear();
		table = (IScrollTableWidget<Object>) widget.mainPanel.add().widget(
				IScrollTableWidget.class);
		table.addTitle(
				widget.title != null ? widget.title : widget.configuration)
				.font().pixel(WidgetTitle.LARGE_FONT);
		addColumns();
		if (widget.allowMultiSelection)
			table.selection().multi().addChangeListener(this);
		else
			table.selection().single().addSelectionListener(this);
		if (widget.showCommands && widget.allowCreate) {
			if (widget.creatableTypes.isEmpty())
				widget.creatableTypes.add(null);
			for (final String type : widget.creatableTypes) {
				IClickable<?> button = table.addButton(type == null ? "New"
						: "New " + type, type == null ? Icons.NEW
						: widget.creatableTypeIcons.get(type));
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
		if (widget.showCommands) {
			delete = table.addButton("Delete",
					co.fxl.gui.api.template.Icons.CANCEL);
			delete.addClickListener(new IClickListener() {
				@Override
				public void onClick() {
					final Map<Integer, Object> result = table.selection()
							.indexedResult();
					IDisplay display = widget.mainPanel.display();
					IDialog dl = TreeWidgetImpl.queryDeleteEntity(display,
							result.size() > 1);
					dl.addButton().yes().addClickListener(new IClickListener() {

						@Override
						public void onClick() {
							List<Integer> indices = new LinkedList<Integer>();
							List<Object> entities = new LinkedList<Object>();
							for (Integer i : result.keySet()) {
								Object entity = result.get(i);
								indices.add(i);
								entities.add(entity);
							}
							widget.queryList
									.delete(indices,
											entities,
											new CallbackTemplate<IDeletableList<Object>>() {

												@Override
												public void onSuccess(
														IDeletableList<Object> result) {
													onDelete(null);
												}
											});
						}
					});
					dl.addButton().no().addClickListener(new IClickListener() {

						@Override
						public void onClick() {
						}
					});
					dl.visible(true);
				}
			});
			delete.clickable(false);
		}
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

	private void addColumns() {
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
				if (widget.hiddenColumns.contains(p.name)) {
					column.visible(false);
				}
				index++;
			}
		}
		widget.hiddenColumns.clear();
	}

	private String getConfig() {
		if (widget.configuration != null)
			return widget.configuration;
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
			assert o != null;
			clickable &= widget.queryList.isDeletable(o);
		}
		// detail.clickable(selection.size() == 1);
		if (widget.showCommands) {
			delete.clickable(clickable);
			updateCreatable();
		}
	}

	@Override
	public void onSelection(int index, Object selection) {
		List<Object> s = new LinkedList<Object>();
		if (selection != null)
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
	public void onApply(final IFilterConstraints constraints) {
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
		queryList(constraints, new CallbackTemplate<IDeletableList<Object>>() {

			private long time;

			@Override
			public void onSuccess(final IDeletableList<Object> queryList) {
				if (painting)
					return;
				// if (true) {
				// new Test().run(widget.mainPanel.clear());
				// return;
				// }
				painting = true;
				final long s = System.currentTimeMillis();
				widget.mainPanel.clear();
				drawTable();
				widget.queryList = queryList;
				widget.rowsInTable = queryList.size();
				final IRows<Object> rows = new IRows<Object>() {

					@Override
					public Object[] row(int i) {
						return queryList.tableValues(queryList.get(i));
					}

					@Override
					public Object identifier(int i) {
						return queryList.get(i);
					}

					@Override
					public int size() {
						return queryList.size();
					}

					@Override
					public boolean deletable(int i) {
						return false;
					}
				};
				ISelection<Object> tableSelection = table.selection();
				for (Object o : widget.selection)
					tableSelection.add(o);
				for (Object o : widget.preselection)
					tableSelection.add(o);
				widget.preselection.clear();
				table.rows(rows);
				time = System.currentTimeMillis() - s;
				final PrintStream out = time > 500 ? System.err : System.out;
				out.println("TableView: added " + queryList.size()
						+ " rows in " + time + "ms");
				ResizeListener.setup(widget.mainPanel.display(), TableView.this);
				widget.mainPanel.display().invokeLater(new Runnable() {

					@Override
					public void run() {
						onHeightChange(widget.mainPanel.display().height());
						updateCreatable();
						time = System.currentTimeMillis() - s;
						IScrollTableClickListener showClickListener = new IScrollTableClickListener() {

							@Override
							public void onClick(Object identifier, int rowIndex) {
								widget.r2.checked(true);
								widget.showDetailView(identifier);
							}
						};
						table.addTableClickListener(showClickListener)
								.doubleClick();
						table.addTooltip("Double-Click to show Details.");
						table.sortListener(TableView.this);
						table.constraints(constraints);
						table.addFilterListener(new IFilterListener() {

							@Override
							public void onApply(IFilterConstraints constraints) {
								TableView.this.onApply(constraints);
							}
						});
						table.visible(true);
						widget.selection = table.selection().result();
						updateLinks();
						out.println("TableView: created table in " + time
								+ "ms");
						onChange(table.selection().result());
						painting = false;
					}
				});
			}
		});
	}

	private void queryList(IFilterConstraints constraints,
			CallbackTemplate<IDeletableList<Object>> callback) {
		if (widget.switch2grid && !widget.refreshOnSwitch2Grid
				&& widget.queryList != null) {
			widget.switch2grid = false;
			callback.onSuccess(widget.queryList);
		} else {
			widget.switch2grid = false;
			widget.source.queryList(constraints, callback);
		}
	}

	@Override
	public void onSort(String columnName, boolean up, boolean update) {
		widget.constraints.sortOrder(columnName);
		widget.constraints.sortDirection(up);
		widget.filterWidget.constraints(widget.constraints());
		if (update)
			onApply(widget.constraints);
	}

	@Override
	public void onResize(int width, final int height) {
		if (!table.visible()) {
			ResizeListener.remove(this);
			return;
		}
		onHeightChange(height);
	}

	protected void onHeightChange(int height) {
		int offsetY = Math.max(table.offsetY(), 140);
		int tableHeight = height - offsetY - 84
				+ (widget.rowsInTable == 0 ? 50 : 0);
		tableHeight = Math.max(tableHeight, 60);
		if (table != null)
			table.height(tableHeight);
	}

	@Override
	public void onUpdate(String value) {
		if (widget.filterWidget != null)
			widget.filterWidget.setConfiguration(value);
		onDelete(null);
	}

	@Override
	public void onDelete(ICallback<Boolean> cb) {
		if (widget.filterWidget != null)
			widget.filterWidget.apply();
		else
			onApply(widget.constraints);
	}

	@Override
	void selection(List<Object> selection) {
		throw new MethodNotImplementedException();
	}

	// private class Test implements IDecorator, IClickListener {
	// private ILazyScrollPane widget;
	// private IHorizontalPanel[] buttons = new IHorizontalPanel[1000];
	//
	// void run(IVerticalPanel panel) {
	// panel.display().register(new LazyScrollPanelImplWidgetProvider());
	// widget = (ILazyScrollPane) panel.add()
	// .widget(ILazyScrollPane.class);
	// widget.size(1000);
	// widget.minRowHeight(20);
	// widget.height(900);
	// widget.decorator(this);
	// widget.visible(true);
	// }
	//
	// @Override
	// public void decorate(IContainer c, int firstRow, int lastRow) {
	// long t = System.currentTimeMillis();
	// IVerticalPanel v = c.panel().vertical();
	// for (int i = firstRow; i <= lastRow; i++) {
	// IHorizontalPanel container = v.add().panel().horizontal();
	// container.height(22);
	// IClickable<?> clickable = container;
	// clickable.addClickListener(this);
	// IHorizontalPanel content = container.add().panel().horizontal()
	// .spacing(2);
	// content.addSpace((i % 3) * 10);
	// IImage image = content.add().image();
	// image.resource("closed.png");
	// image.addClickListener(this);
	// IImage icon = content.add().image().resource("export.png");
	// icon.addClickListener(this);
	// content.addSpace(2);
	// String name = "Tree Node " + i;
	// boolean isNull = name == null || name.trim().equals("");
	// ILabel label = content.add().label()
	// .text(isNull ? "unnamed" : name);
	// label.font().pixel(12);
	// if (isNull)
	// label.font().weight().italic().color().gray();
	// label.addClickListener(this);
	// content.addSpace(10);
	// buttons[i] = container;
	// }
	// c.display().title((System.currentTimeMillis() - t) + "ms");
	// }
	//
	// @Override
	// public void onClick() {
	// throw new MethodNotImplementedException();
	// }
	//
	// @Override
	// public int rowHeight(int rowIndex) {
	// return buttons[rowIndex].height();
	// }
	// }
}
