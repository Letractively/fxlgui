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

import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.api.template.CallbackTemplate;
import co.fxl.gui.api.template.DateFormat;
import co.fxl.gui.api.template.ICallback;
import co.fxl.gui.api.template.IFieldType;
import co.fxl.gui.api.template.IPageListener;
import co.fxl.gui.filter.api.IFilterConstraints;
import co.fxl.gui.filter.api.IFilterWidget.IRelationFilter;
import co.fxl.gui.mdt.api.IMDTFilterList;
import co.fxl.gui.mdt.api.IMasterDetailTableWidget;
import co.fxl.gui.tree.api.IFilterTreeWidget;
import co.fxl.gui.tree.api.IFilterTreeWidget.ISource;
import co.fxl.gui.tree.api.ITree;
import co.fxl.gui.tree.api.ITreeWidget;
import co.fxl.gui.tree.api.ITreeWidget.IDecorator;
import co.fxl.gui.tree.api.ITreeWidget.ITreeClickListener;

class DetailView extends ViewTemplate implements ISource<Object>, IPageListener {

	static final DateFormat DATE_FORMAT = DateFormat.instance;
	IFilterTreeWidget<Object> tree;
	private IMDTFilterList<Object> filterList;
	protected ITree<Object> itree;
	private Object selectionObject;
	private String createType;
	private IPageListener pageListener;
	private boolean create;

	@SuppressWarnings("unchecked")
	DetailView(final MasterDetailTableWidgetImpl widget, Object sshow,
			boolean create, String createType) {
		super(widget);
		widget.pageListener = this;
		this.create = create;
		this.createType = createType;
		// if (widget.splitLayout != null)
		// widget.splitLayout.showSplit(false);
		this.selectionObject = sshow;
		tree = (IFilterTreeWidget<Object>) widget.mainPanel.add().widget(
				IFilterTreeWidget.class);
		tree.addTreeClickListener(new ITreeClickListener<Object>() {

			@Override
			public void onClick(ITree<Object> tree) {
				widget.showTableView(tree.object());
			}
		}).altPressed();
		tree.showRefresh(false);
		if (widget.hideDetailRoot)
			tree.hideRoot();
		tree.allowCreate(widget.allowCreate);
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
		// filterList = tree.filterList(widget.sidePanel.add().panel());
		// setupFilter(widget);
		// tree.addHyperlink("Grid").addClickListener(new IClickListener() {
		// @Override
		// public void onClick() {
		// widget.showTableView(tree.selection());
		// }
		// });
		addDetailViews();
		tree.source(this);
		tree.selection(sshow);
		tree.expand();
		tree.pageListener(this);
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
		List<PropertyGroupImpl> gs = new LinkedList<PropertyGroupImpl>();
		for (final PropertyGroupImpl group : widget.propertyGroups) {
			if (group.asDetail
					&& group.name.equals(IMasterDetailTableWidget.DETAILS))
				gs.add(group);
		}
		DetailViewDecorator decorator = new DetailViewDecorator(gs) {

			@Override
			protected void save(Object node) {
				saveNode(node);
			}
		};
		pageListener = decorator;
		tree.addDetailView(IMasterDetailTableWidget.DETAILS, decorator);
		for (final PropertyGroupImpl group : widget.propertyGroups) {
			if (group.asDetail
					&& !group.name.equals(IMasterDetailTableWidget.DETAILS)) {
				tree.addDetailView(group.name, new DetailViewDecorator(gs) {

					@Override
					protected void save(Object node) {
						saveNode(node);
					}
				});
			}
		}
		for (final RelationImpl relation : widget.relations) {
			tree.addDetailView(relation.name, new RelationDecorator(relation))
					.constrainType(relation.constrainType);
		}
		for (final N2MRelationImpl relation : widget.n2MRelations) {
			tree.addDetailView(relation.name,
					new N2MRelationDecorator(relation)).constrainType(
					relation.constrainType);
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
					relation.dec.decorate(panel.clear().add(), node);
				}
			}).constrainType(relation.constrainType);
		}
	}

	@Override
	public void query(IFilterConstraints constraints,
			final ICallback<ITree<Object>> callback) {
		// widget.constraints = constraints;
		widget.source.queryTree(constraints, selectionObject,
				new CallbackTemplate<ITree<Object>>() {

					@Override
					public void onSuccess(ITree<Object> result) {
						DetailView.this.itree = result;
						callback.onSuccess(result);
						if (create) {
							onNew(createType);
							create = false;
						}
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

	@Override
	public void onUpdate(String value) {
		onRefresh();
	}

	@Override
	public void onRefresh() {
		tree.refresh();
	}

	private void saveNode(final Object node) {
		itree.save(node, new CallbackTemplate<Object>() {

			@Override
			public void onSuccess(Object result) {
				tree.notifyUpdate(node, result);
			}
		});
	}

	@Override
	public void notifyChange(ICallback<Boolean> callback) {
		pageListener.notifyChange(callback);
	}
}
