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

import java.util.Date;
import java.util.List;

import co.fxl.gui.api.ITextElement;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.template.IFieldType;
import co.fxl.gui.api.template.SimpleDateFormat;
import co.fxl.gui.filter.api.IFilterConstraints;
import co.fxl.gui.form.api.IFormField;
import co.fxl.gui.form.api.IFormWidget;
import co.fxl.gui.mdt.api.IFilterList;
import co.fxl.gui.table.api.IRow;
import co.fxl.gui.table.api.ITableWidget;
import co.fxl.gui.tree.api.ICallback;
import co.fxl.gui.tree.api.IFilterTreeWidget;
import co.fxl.gui.tree.api.ITree;
import co.fxl.gui.tree.api.IFilterTreeWidget.ISource;
import co.fxl.gui.tree.api.ITreeWidget.IDecorator;

class DetailView implements ISource<Object> {

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat();
	private MasterDetailTableWidgetImpl widget;
	private IFilterTreeWidget<Object> tree;
	private IFilterList<Object> filterList;

	@SuppressWarnings("unchecked")
	DetailView(final MasterDetailTableWidgetImpl widget, Object show) {
		this.widget = widget;
		tree = (IFilterTreeWidget<Object>) widget.splitLayout.mainPanel.add()
				.widget(IFilterTreeWidget.class);
		tree.title(widget.title);
		filterList = tree
				.filterList(widget.splitLayout.sidePanel.add().panel());
		for (FilterImpl filter : widget.filterList.filters) {
			if (filter.property != null) {
				IFieldType type = filterList.addFilter().name(
						filter.property.name).type().type(
						filter.property.type.type);
				type.selection(filter.property.type.values);
			} else
				throw new MethodNotImplementedException();
		}
		if (widget.constraints != null)
			filterList.constraints(widget.constraints);
		tree.addHyperlink("Grid").addClickListener(new IClickListener() {
			@Override
			public void onClick() {
				widget.showTableView(tree.selection());
			}
		});
		addDetailViews();
		tree.source(this);
		tree.selection(show);
		tree.expand();
		tree.visible(true);
	}

	private void addDetailViews() {
		for (final PropertyGroupImpl group : widget.propertyGroups) {
			tree.addDetailView(group.name, new IDecorator<Object>() {

				@Override
				public void clear(IVerticalPanel panel) {
					panel.clear();
				}

				@Override
				public void decorate(IVerticalPanel panel, final Object node) {
					panel.clear();
					IBorder border = panel.border();
					border.color().gray();
					border.style().top();
					IFormWidget form = (IFormWidget) panel.add().widget(
							IFormWidget.class);
					for (PropertyImpl property : group.properties) {
						if (property.displayInDetailView) {
							IFormField<?> formField;
							if (property.type.type.equals(String.class)) {
								ITextElement<?> valueElement;
								if (property.type.isLong) {
									formField = form.addTextArea(property.name);
									valueElement = formField.valueElement();
								} else {
									formField = form
											.addTextField(property.name);
									valueElement = formField.valueElement();
								}
								String value = String.valueOf(property.adapter
										.valueOf(node));
								valueElement.text(value);
							} else if (property.type.type.equals(Date.class)) {
								formField = form.addTextField(property.name);
								String value = DATE_FORMAT
										.format((Date) property.adapter
												.valueOf(node));
								formField.valueElement().text(value);
							} else
								throw new MethodNotImplementedException();
							if (property.required)
								formField.required();
						}
					}
					form.visible(true);
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

								@Override
								public void onFail(Throwable throwable) {
									throw new MethodNotImplementedException();
								}

								@Override
								public void onSuccess(List<Object> result) {
									@SuppressWarnings("unchecked")
									ITableWidget<Object> table = (ITableWidget<Object>) panel
											.add().widget(ITableWidget.class);
									for (PropertyImpl property : relation.properties) {
										table.addColumn().name(property.name)
												.type(property.type.type);
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
			});
		}
	}

	@Override
	public void query(IFilterConstraints constraints,
			final ICallback<ITree<Object>> callback) {
		widget.constraints = constraints;
		widget.source.queryTree(constraints, callback);
	}

	void onNew() {
		tree.clickNew();
	}
}
