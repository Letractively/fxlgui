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

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.api.IButton;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.api.template.ICallback;
import co.fxl.gui.api.template.IFieldType;
import co.fxl.gui.api.template.SimpleDateFormat;
import co.fxl.gui.filter.api.IFilterConstraints;
import co.fxl.gui.filter.api.IFilterWidget.IRelationFilter;
import co.fxl.gui.mdt.api.IMDTFilterList;
import co.fxl.gui.n2m.api.IN2MWidget;
import co.fxl.gui.n2m.api.IN2MWidget.IN2MRelationListener;
import co.fxl.gui.table.api.IColumn;
import co.fxl.gui.table.api.IColumn.IColumnUpdateListener;
import co.fxl.gui.table.api.IRow;
import co.fxl.gui.table.api.ISelection;
import co.fxl.gui.table.api.ISelection.ISingleSelection;
import co.fxl.gui.table.api.ITableWidget;
import co.fxl.gui.tree.api.IFilterTreeWidget;
import co.fxl.gui.tree.api.IFilterTreeWidget.ISource;
import co.fxl.gui.tree.api.ITree;
import co.fxl.gui.tree.api.ITreeWidget;
import co.fxl.gui.tree.api.ITreeWidget.IDecorator;

class DetailView extends ViewTemplate implements ISource<Object> {

	static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat();
	IFilterTreeWidget<Object> tree;
	private IMDTFilterList<Object> filterList;
	protected ITree<Object> itree;
	private Object selectionObject;

	@SuppressWarnings("unchecked")
	DetailView(final MasterDetailTableWidgetImpl widget, Object sshow) {
		super(widget);
		if (widget.splitLayout != null)
			widget.splitLayout.showSplit(false);
		this.selectionObject = sshow;
		tree = (IFilterTreeWidget<Object>) widget.mainPanel.add().widget(
				IFilterTreeWidget.class);
		if (widget.hideDetailRoot)
			tree.hideRoot();
		for (String s : widget.creatableTypes) {
			tree.addCreatableType(s);
		}
		tree.title(widget.title);
		tree.addSelectionListener(new ITreeWidget.ISelectionListener<Object>() {

			@Override
			public void onChange(Object selection) {
				selectionObject = selection;
				List<Object> l = new LinkedList<Object>();
				l.add(selection);
				DetailView.this.onChange(l);
			}
		});
		addNavigationLinks();
		// filterList = tree.filterList(widget.sidePanel.add().panel());
		// setupFilter(widget);
		tree.addHyperlink("Grid").addClickListener(new IClickListener() {
			@Override
			public void onClick() {
				widget.showTableView(tree.selection());
			}
		});
		addDetailViews();
		tree.source(this);
		tree.selection(sshow);
		tree.expand();
		tree.visible(true);
	}

	@SuppressWarnings("unchecked")
	void setupFilter(final MasterDetailTableWidgetImpl widget) {
		int index = 0;
		for (MDTFilterImpl filter : widget.filterList.filters) {
			if (!filter.asDetail)
				continue;
			String config = widget.filterList.configuration2index.get(index++);
			if (config != null)
				filterList.addConfiguration(config);
			if (filter instanceof MDTRelationFilterImpl) {
				MDTRelationFilterImpl rfi = (MDTRelationFilterImpl) filter;
				IRelationFilter<Object, Object> rf = (IRelationFilter<Object, Object>) filterList
						.addRelationFilter();
				rf.name(rfi.name);
				rf.adapter(rfi.adapter);
				rf.preset(rfi.preset);
			} else if (filter.property != null) {
				IFieldType type = filterList.addFilter()
						.name(filter.property.name).type()
						.type(filter.property.type.clazz);
				for (Object o : filter.property.type.values)
					type.addConstraint(o);
			} else
				throw new MethodNotImplementedException();
		}
		if (widget.constraints != null)
			filterList.constraints(widget.constraints);
	}

	private void addDetailViews() {
		for (final PropertyGroupImpl group : widget.propertyGroups) {
			tree.addDetailView(group.name, new DetailViewDecorator(group) {

				@Override
				protected void save(Object node) {
					DetailView.this.itree.save(node);
					DetailView.this.tree.notifyUpdate(node);
				}
			});
		}
		for (final RelationImpl relation : widget.relations) {
			tree.addDetailView(relation.name, new IDecorator<Object>() {

				@Override
				public void clear(IVerticalPanel panel) {
					panel.clear();
				}

				@Override
				public void decorate(final IVerticalPanel panel, Object node) {
					panel.clear();
					IBorder border = panel.border();
					border.color().gray();
					border.style().top();
					relation.adapter.valueOf(node,
							new ICallback<List<Object>>() {

								private IButton details;
								private IButton add;
								private IButton remove;

								@Override
								public void onFail(Throwable throwable) {
									throw new MethodNotImplementedException();
								}

								@Override
								public void onSuccess(List<Object> result) {
									@SuppressWarnings("unchecked")
									ITableWidget<Object> table = (ITableWidget<Object>) panel
											.add().widget(ITableWidget.class);
									final ISelection<Object> selection0 = table
											.selection();
									ISingleSelection<Object> selection = selection0
											.single();
									IHorizontalPanel buttonPanel = panel.add()
											.panel().horizontal().add().panel()
											.horizontal();
									if (relation.addRemoveListener != null) {
										if (relation.addRemoveListener
												.isDetailedAdd()) {
											add = relation.addRemoveListener
													.decorateAdd(buttonPanel
															.add());
										} else
											add = buttonPanel.add().button()
													.text("Add");
										remove = buttonPanel.addSpace(10).add()
												.button().text("Remove")
												.clickable(false);
									}
									if (relation.showListener != null)
										details = buttonPanel.addSpace(10)
												.add().button().text("Show")
												.clickable(false);
									co.fxl.gui.table.api.ISelection.ISingleSelection.ISelectionListener<Object> listener = new co.fxl.gui.table.api.ISelection.ISingleSelection.ISelectionListener<Object>() {
										@Override
										public void onSelection(Object selection) {
											if (remove != null) {
												remove.clickable(selection != null);
											}
											if (details != null)
												details.clickable(selection != null);
										}
									};
									if (add != null)
										add.addClickListener(new IClickListener() {
											@Override
											public void onClick() {
												List<Object> r = selection0
														.result();
												Object c = null;
												if (r.size() > 0)
													c = r.get(0);
												relation.addRemoveListener
														.onAdd(c);
											}
										});
									if (remove != null)
										remove.addClickListener(new IClickListener() {
											@Override
											public void onClick() {
												List<Object> r = selection0
														.result();
												relation.addRemoveListener
														.onRemove(r.get(0));
											}
										});
									if (details != null)
										details.addClickListener(new IClickListener() {
											@Override
											public void onClick() {
												List<Object> r = selection0
														.result();
												relation.showListener.onShow(r);
											}
										});
									selection.addSelectionListener(listener);
									for (final PropertyImpl property : relation.properties) {
										IColumn c = table.addColumn()
												.name(property.name)
												.type(property.type.clazz)
												.sortable();
										if (property.type.clazz
												.equals(Boolean.class)) {
											c.updateListener(new IColumnUpdateListener<Object, Boolean>() {

												@Override
												public void onUpdate(Object o,
														Boolean value) {
													property.adapter.valueOf(o,
															value);
												}
											});
										}
									}
									for (Object e : result) {
										IRow<Object> row = table.addRow();
										row.identifier(e);
										for (PropertyImpl p : relation.properties) {
											row.add((Comparable<?>) p.adapter
													.valueOf(e));
										}
									}
									table.visible(true);
								}
							});
				}

				@Override
				public void decorate(IVerticalPanel panel, ITree<Object> tree) {
					decorate(panel, tree.object());
				}
			});
		}
		for (final N2MRelationImpl relation : widget.n2MRelations) {
			tree.addDetailView(relation.name, new IDecorator<Object>() {

				@Override
				public void clear(IVerticalPanel panel) {
					panel.clear();
				}

				@Override
				public void decorate(IVerticalPanel panel, ITree<Object> tree) {
					decorate(panel, tree.object());
				}

				@Override
				public void decorate(final IVerticalPanel panel,
						final Object node) {
					panel.clear();
					IBorder border = panel.border();
					border.color().gray();
					border.style().top();
					relation.adapter.valueOf(node,
							new ICallback<List<Object>>() {

								@Override
								public void onFail(Throwable throwable) {
									throw new MethodNotImplementedException();
								}

								@Override
								public void onSuccess(final List<Object> result) {
									@SuppressWarnings("unchecked")
									IN2MWidget<Object> table = (IN2MWidget<Object>) panel
											.add().widget(IN2MWidget.class);
									table.domain(relation.domain);
									table.selection(result);
									table.listener(new IN2MRelationListener<Object>() {
										@Override
										public void onChange(
												List<Object> selection) {
											relation.adapter
													.valueOf(
															node,
															selection,
															new ICallback<List<Object>>() {

																@Override
																public void onSuccess(
																		List<Object> result) {
																}

																@Override
																public void onFail(
																		Throwable throwable) {
																	throw new MethodNotImplementedException();
																}
															});
										}
									});
								}
							});
				}
			});
		}
		for (final PropertyPageImpl relation : widget.propertyPages) {
			tree.addDetailView(relation.name, new IDecorator<Object>() {

				@Override
				public void clear(IVerticalPanel panel) {
					panel.clear();
				}

				@Override
				public void decorate(IVerticalPanel panel, ITree<Object> tree) {
					decorate(panel, tree.object());
				}

				@Override
				public void decorate(final IVerticalPanel panel,
						final Object node) {
					relation.dec.decorate(panel.add(), node);
				}
			});
		}
		;
	}

	@Override
	public void query(IFilterConstraints constraints,
			final ICallback<ITree<Object>> callback) {
		widget.constraints = constraints;
		widget.source.queryTree(constraints, selectionObject,
				new ICallback<ITree<Object>>() {

					@Override
					public void onSuccess(ITree<Object> result) {
						DetailView.this.itree = result;
						callback.onSuccess(result);
					}

					@Override
					public void onFail(Throwable throwable) {
						callback.onFail(throwable);
					}
				});
	}

	void onNew(String type) {
		if (type == null)
			tree.clickNew();
		else
			tree.clickNew(type);
	}

	@Override
	boolean isRelevant(NavigationLinkImpl link) {
		return link.asDetail;
	}
}
