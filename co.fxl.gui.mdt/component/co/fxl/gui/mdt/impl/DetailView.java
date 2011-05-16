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

import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.api.template.CallbackTemplate;
import co.fxl.gui.api.template.DateFormat;
import co.fxl.gui.api.template.ICallback;
import co.fxl.gui.api.template.IFieldType;
import co.fxl.gui.filter.api.IFilterConstraints;
import co.fxl.gui.filter.api.IFilterWidget.IRelationFilter;
import co.fxl.gui.mdt.api.IMDTFilterList;
import co.fxl.gui.mdt.api.IMasterDetailTableWidget;
import co.fxl.gui.mdt.impl.DetailViewDecorator.DeleteListener;
import co.fxl.gui.tree.api.IFilterTreeWidget;
import co.fxl.gui.tree.api.IFilterTreeWidget.ISource;
import co.fxl.gui.tree.api.ITree;
import co.fxl.gui.tree.api.ITreeWidget;
import co.fxl.gui.tree.api.ITreeWidget.IDecorator;
import co.fxl.gui.tree.api.ITreeWidget.IView;

class DetailView extends ViewTemplate implements ISource<Object>,
		DeleteListener {

	static final DateFormat DATE_FORMAT = DateFormat.instance;
	IFilterTreeWidget<Object> tree;
	private IMDTFilterList<Object> filterList;
	protected ITree<Object> itree;
	private Object selectionObject;
	private String createType;
	private boolean create;

	DetailView(final MasterDetailTableWidgetImpl widget, Object sshow,
			boolean create, String createType) {
		super(widget);
		this.create = create;
		this.createType = createType;
		this.selectionObject = sshow;
	}

	@SuppressWarnings("unchecked")
	void refresh() {
		widget.mainPanel.clear();
		tree = (IFilterTreeWidget<Object>) widget.mainPanel.add().widget(
				IFilterTreeWidget.class);
		tree.showCommands(widget.showCommands);
		// tree.addTreeClickListener(new ITreeClickListener<Object>() {
		//
		// @Override
		// public void onClick(ITree<Object> tree) {
		// if (tree.isLeaf())
		// widget.showTableView(tree.object());
		// }
		// }).doubleClick();
		tree.showRefresh(false);
		if (widget.hideDetailRoot)
			tree.hideRoot();
		tree.allowCreate(widget.allowCreate);
		for (String s : widget.creatableTypes) {
			tree.addCreatableType(s, widget.creatableTypeIcons.get(s));
		}
		tree.title(widget.title != null ? widget.title : widget.configuration);
		tree.addSelectionListener(new ITreeWidget.ISelectionListener<Object>() {

			@Override
			public void onChange(Object selection) {
				selectionObject = selection;
				List<Object> l = new LinkedList<Object>();
				if (selection != null)
					l.add(selection);
				DetailView.this.onChange(l);
			}
		});
		addDetailViews();
		if (widget.selectedRegister != null) {
			tree.activeDetailView(widget.selectedRegister);
			widget.selectedRegister = null;
		}
		tree.allowCutPaste(widget.allowCutPaste);
		tree.allowReorder(widget.allowReorder);
		tree.expand(true);
		tree.source(this);
		tree.selection(getSelectionObject());
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

	private final class PropertyPageDecorator implements IDecorator<Object> {

		private PropertyPageImpl propertyPage;

		PropertyPageDecorator(PropertyPageImpl propertyPage) {
			this.propertyPage = propertyPage;
		}

		@Override
		public void clear(IVerticalPanel panel) {
			panel.clear();
		}

		@Override
		public void decorate(IVerticalPanel panel, IVerticalPanel bottom,
				ITree<Object> tree) {
			decorate(panel, bottom, tree.object());
		}

		@Override
		public void decorate(final IVerticalPanel panel, IVerticalPanel bottom,
				final Object node) {
			propertyPage.dec.decorate(panel.clear().add(), node);
		}
	}

	private class Register {

		String name;
		IDecorator<Object> dec;
		Class<?>[] c;

		private Register(String name, IDecorator<Object> dec,
				Class<?>[] constraint) {
			this.name = name;
			this.dec = dec;
			this.c = constraint;
		}

		public Register(String name, IDecorator<Object> dec2,
				Class<?> constrainType) {
			this.name = name;
			this.dec = dec2;
			this.c = new Class<?>[] { constrainType };
		}
	}

	private void addDetailViews() {
		if (widget.overviewPage != null) {
			IDecorator<Object> d = new PropertyPageDecorator(
					widget.overviewPage);
			IView dv = tree.addDetailView(widget.overviewPage.name, d);
			if (widget.overviewPage.constrainType != null)
				dv.constrainType(widget.overviewPage.constrainType);
		}
		List<PropertyGroupImpl> gs = new LinkedList<PropertyGroupImpl>();
		for (final PropertyGroupImpl group : widget.propertyGroups) {
			if (group.asDetail
					&& group.name.equals(IMasterDetailTableWidget.DETAILS))
				gs.add(group);
		}
		DetailViewDecorator decorator = new DetailViewDecorator(gs) {

			@Override
			protected void save(ITree<Object> node, ICallback<Boolean> cb) {
				widget.queryList = null;
				saveNode(node, cb);
			}
		}.refreshListener(this);
		Map<Object, Register> registers = new HashMap<Object, Register>();
		tree.addDetailView(IMasterDetailTableWidget.DETAILS, decorator);
		for (final PropertyGroupImpl group : widget.propertyGroups) {
			if (group.asDetail
					&& !group.name.equals(IMasterDetailTableWidget.DETAILS)) {
				registers.put(group, new Register(group.name,
						new DetailViewDecorator(gs) {

							@Override
							protected void save(ITree<Object> node,
									ICallback<Boolean> cb) {
								saveNode(node, cb);
							}
						}.refreshListener(this), (Class<?>) null));
			}
		}
		for (final PropertyPageImpl relation : widget.propertyPages) {
			registers.put(relation,
					new Register(relation.name, new PropertyPageDecorator(
							relation), relation.constrainType));
		}
		for (final N2MRelationImpl relation : widget.n2MRelations) {
			registers
					.put(relation, new Register(relation.name,
							new N2MRelationDecorator(relation),
							relation.constrainType));
		}
		for (final RelationImpl relation : widget.relations) {
			registers.put(relation, new Register(relation.name,
					new RelationDecorator(this, relation),
					relation.constrainType));
		}
		for (Object r : widget.registerOrder) {
			Register reg = registers.get(r);
			if (reg != null) {
				IView dv = tree.addDetailView(reg.name, reg.dec);
				if (reg.c != null)
					dv.constrainType(reg.c);
			}
		}
	}

	@Override
	public void query(IFilterConstraints constraints,
			final ICallback<ITree<Object>> callback) {
		// widget.constraints = constraints;
		widget.source.queryTree(widget.constraints, getSelectionObject(),
				new CallbackTemplate<ITree<Object>>(callback) {

					@Override
					public void onSuccess(final ITree<Object> result) {
						DetailView.this.itree = result;
						// if (!create) {
						callback.onSuccess(result);
						if (create) {
							DetailView.this.widget.mainPanel.display()
									.invokeLater(new Runnable() {

										@Override
										public void run() {
											create = false;
											tree.clickNew(createType);
											// itree.createNew(
											// createType,
											// new
											// CallbackTemplate<ITree<Object>>(
											// callback) {
											//
											// @Override
											// public void onSuccess(
											// ITree<Object> result1) {
											// tree.selection(result1
											// .object());
											// callback.onSuccess(result);
											// }
											// });
										}
									});
							// } else {

						}
					}
				});
	}

	protected Object getSelectionObject() {
		if (selectionObject != null)
			return selectionObject;
		else if (!widget.selection.isEmpty()) {
			return widget.selection.get(widget.selection.size() - 1);
		} else
			return null;
	}

	@Override
	public void onUpdate(String value) {
		onDelete(null);
	}

	@Override
	public void onDelete(final ITree<Object> tree, final ICallback<Boolean> cb) {
		if (tree != null && tree.isNew() && tree.parent() != null)
			DetailView.this.tree.selection(tree.parent().object());
		this.tree.refresh(new CallbackTemplate<Boolean>(cb) {

			@Override
			public void onSuccess(Boolean result) {
				if (cb != null)
					cb.onSuccess(true);
			}
		});
	}

	private void saveNode(final ITree<Object> node, final ICallback<Boolean> cb) {
		itree.save(node, new CallbackTemplate<Object>(cb) {

			@Override
			public void onSuccess(Object result) {
				tree.notifyUpdate(result);
				cb.onSuccess(true);
			}
		});
	}

	@Override
	public void onDelete(ICallback<Boolean> cb) {
		onDelete(null, cb);
	}

	@Override
	public void onApply(IFilterConstraints constraints) {
		widget.constraints = constraints;
		refresh();
	}

	@Override
	public void onDelete(Object tree, ICallback<Boolean> cb) {
		refresh();
		cb.onSuccess(true);
	}

	@Override
	void selection(List<Object> selection) {
		if (selection.size() > 1) {
			throw new MethodNotImplementedException();
		}
		tree.selection(selection.get(0));
	}
}
